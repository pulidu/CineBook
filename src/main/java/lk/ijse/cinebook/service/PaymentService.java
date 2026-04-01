package lk.ijse.cinebook.service;

import lk.ijse.cinebook.dto.request.PaymentRequest;
import lk.ijse.cinebook.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
    PaymentResponse createPayPalOrder(Long bookingId);
    PaymentResponse capturePayPalOrder(String orderId, Long bookingId);
}
