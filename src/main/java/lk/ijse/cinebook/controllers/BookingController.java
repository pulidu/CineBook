package lk.ijse.cinebook.controllers;

import lk.ijse.cinebook.dto.request.BookingRequest;
import lk.ijse.cinebook.dto.request.SeatLockRequest;
import lk.ijse.cinebook.dto.response.BookingResponse;
import lk.ijse.cinebook.dto.response.PaymentResponse;
import lk.ijse.cinebook.dto.response.SeatResponse;
import lk.ijse.cinebook.service.impl.BookingServiceImpl;
import lk.ijse.cinebook.service.impl.PaymentServiceImpl;
import lk.ijse.cinebook.service.impl.SeatLockServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BookingController {

    private final BookingServiceImpl bookingService;
    private final SeatLockServiceImpl seatLockService;
    private final PaymentServiceImpl paymentService;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getUserBookings() {
        return ResponseEntity.ok(bookingService.getUserBookings());
    }

    @GetMapping("/{reference}")
    public ResponseEntity<BookingResponse> getBookingByReference(@PathVariable String reference) {
        return ResponseEntity.ok(bookingService.getBookingByReference(reference));
    }

    @GetMapping("/seats/{showId}")
    public ResponseEntity<List<SeatResponse>> getAvailableSeats(@PathVariable Long showId) {
        return ResponseEntity.ok(seatLockService.getAvailableSeats(showId));
    }

    @PostMapping("/seats/lock")
    public ResponseEntity<List<SeatResponse>> lockSeats(@RequestBody SeatLockRequest request) {
        return ResponseEntity.ok(seatLockService.lockSeats(request));
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok().build();
    }

    // PayPal Endpoints
    @PostMapping("/{bookingId}/paypal/create")
    public ResponseEntity<PaymentResponse> createPayPalOrder(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.createPayPalOrder(bookingId));
    }

    @PostMapping("/{bookingId}/paypal/capture")
    public ResponseEntity<PaymentResponse> capturePayPalOrder(@PathVariable Long bookingId,
                                                              @RequestParam String orderId) {
        return ResponseEntity.ok(paymentService.capturePayPalOrder(orderId, bookingId));
    }

    @GetMapping("/{bookingId}/payment/status")
    public ResponseEntity<PaymentResponse> getPaymentStatus(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentStatus(bookingId));
    }
}