package lk.ijse.cinebook.service;

import lk.ijse.cinebook.entity.Booking;

public interface EmailService {
    void sendBookingConfirmation(Booking booking);
    void sendPasswordResetEmail(String to, String resetLink);
    void sendPaymentConfirmation(Booking booking);

}
