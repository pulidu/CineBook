package lk.ijse.cinebook.service.impl;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import lk.ijse.cinebook.dto.request.PaymentRequest;
import lk.ijse.cinebook.dto.response.PaymentResponse;
import lk.ijse.cinebook.entity.Booking;
import lk.ijse.cinebook.entity.Payment;
import lk.ijse.cinebook.repository.BookingRepository;
import lk.ijse.cinebook.repository.PaymentRepository;
import lk.ijse.cinebook.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final BookingServiceImpl bookingService; // ✅ ADDED FIELD
    private final EmailServiceImpl emailService;
    private final PayPalHttpClient payPalHttpClient;

    @Override
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Validate amount
        if (!booking.getTotalAmount().equals(request.getAmount())) {
            throw new RuntimeException("Payment amount does not match booking amount");
        }

        // Process payment with PayHere (simulated)
        PaymentResponse paymentResponse = processWithPayHere(request);

        if ("SUCCESS".equals(paymentResponse.getPaymentStatus())) {
            // ✅ CONSOLIDATED CONFIRMATION LOGIC
            bookingService.confirmBooking(request.getBookingId());

            // Create payment record
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setAmount(request.getAmount());
            payment.setTransactionId(paymentResponse.getTransactionId());
            payment.setStatus("COMPLETED");
            payment.setPaymentMethod("CARD");
            paymentRepository.save(payment);

            // Send confirmation email with QR code
            emailService.sendBookingConfirmation(booking);
        }

        return paymentResponse;
    }

    @Override
    @Transactional
    public PaymentResponse createPayPalOrder(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest()
                .referenceId(booking.getBookingReference())
                .description("CineBook Ticket Booking: " + booking.getBookingReference())
                .amountWithBreakdown(new AmountWithBreakdown().currencyCode("USD").value(String.format("%.2f", booking.getTotalAmount() / 300.0))); // Simple conversion since PayPal USD only in sandbox for some regions
        purchaseUnitRequests.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnitRequests);

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl("http://localhost:5500/payment-success.html?bookingId=" + bookingId)
                .cancelUrl("http://localhost:5500/payment-failed.html?bookingId=" + bookingId);
        orderRequest.applicationContext(applicationContext);

        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            Order order = response.result();
            String redirectUrl = order.links().stream()
                    .filter(link -> "approve".equals(link.rel()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No approval link found"))
                    .href();

            return PaymentResponse.builder()
                    .paymentStatus("PENDING")
                    .transactionId(order.id())
                    .redirectUrl(redirectUrl)
                    .amount(booking.getTotalAmount())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create PayPal order", e);
        }
    }

    @Override
    @Transactional
    public PaymentResponse capturePayPalOrder(String orderId, Long bookingId) {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        try {
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            Order order = response.result();

            if ("COMPLETED".equals(order.status())) {
                // ✅ CONSOLIDATED CONFIRMATION LOGIC
                bookingService.confirmBooking(bookingId);

                Booking booking = bookingRepository.findById(bookingId)
                        .orElseThrow(() -> new RuntimeException("Booking not found"));

                Payment payment = new Payment();
                payment.setBooking(booking);
                payment.setAmount(booking.getTotalAmount());
                payment.setTransactionId(order.id());
                payment.setStatus("COMPLETED");
                payment.setPaymentMethod("PAYPAL");
                paymentRepository.save(payment);

                emailService.sendBookingConfirmation(booking);

                return PaymentResponse.builder()
                        .paymentStatus("SUCCESS")
                        .transactionId(order.id())
                        .amount(booking.getTotalAmount())
                        .paymentDate(LocalDateTime.now().toString())
                        .build();
            } else {
                return PaymentResponse.builder()
                        .paymentStatus("FAILED")
                        .message("Payment not completed: " + order.status())
                        .build();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to capture PayPal order", e);
        }
    }

    private PaymentResponse processWithPayHere(PaymentRequest request) {
        // This is a simulation. In production, integrate with PayHere API

        // Validate card (simple validation)
        if (request.getCardNumber() == null || request.getCardNumber().length() < 16) {
            return PaymentResponse.builder()
                    .paymentStatus("FAILED")
                    .message("Invalid card number")
                    .build();
        }

        if (request.getCvv() == null || request.getCvv().length() < 3) {
            return PaymentResponse.builder()
                    .paymentStatus("FAILED")
                    .message("Invalid CVV")
                    .build();
        }

        // Simulate successful payment
        String transactionId = "PAY" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8);

        return PaymentResponse.builder()
                .paymentId(System.currentTimeMillis())
                .paymentStatus("SUCCESS")
                .transactionId(transactionId)
                .message("Payment successful")
                .amount(request.getAmount())
                .paymentDate(LocalDateTime.now().toString())
                .build();
    }

    public PaymentResponse getPaymentStatus(Long bookingId) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .paymentStatus(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate().toString())
                .build();
    }
}