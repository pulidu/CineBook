package lk.ijse.cinebook.service.impl;

import jakarta.mail.internet.MimeMessage;
import lk.ijse.cinebook.entity.Booking;
import lk.ijse.cinebook.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.base-url:http://localhost:8081}")
    private String baseUrl;

    @Override
    public void sendBookingConfirmation(Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(booking.getUser().getEmail());
            helper.setSubject("CineBook - Booking Confirmation: " + booking.getBookingReference());

            Context context = new Context();
            context.setVariable("name", booking.getUser().getName());
            context.setVariable("bookingReference", booking.getBookingReference());
            context.setVariable("movieTitle", booking.getShow().getMovie().getTitle());
            context.setVariable("showDate", booking.getShow().getShowDate());
            context.setVariable("showTime", booking.getShow().getShowTime());
            context.setVariable("seats", booking.getSeatNumbers());
            context.setVariable("totalAmount", booking.getTotalAmount());
            context.setVariable("qrCode", booking.getQrCode());
            context.setVariable("bookingUrl", baseUrl + "/bookings/" + booking.getBookingReference());

            String htmlContent = templateEngine.process("booking-confirmation", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Booking confirmation email sent to: {}", booking.getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send booking confirmation email: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("CineBook - Password Reset Request");

            Context context = new Context();
            context.setVariable("resetLink", resetLink);
            context.setVariable("email", to);

            String htmlContent = templateEngine.process("password-reset", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Password reset email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send password reset email: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendPaymentConfirmation(Booking booking) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(booking.getUser().getEmail());
            helper.setSubject("CineBook - Payment Confirmation");

            Context context = new Context();
            context.setVariable("name", booking.getUser().getName());
            context.setVariable("bookingReference", booking.getBookingReference());
            context.setVariable("bookingUrl", baseUrl + "/bookings/" + booking.getBookingReference());

            String htmlContent = templateEngine.process("payment-confirmation", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Payment confirmation email sent to: {}", booking.getUser().getEmail());
        } catch (Exception e) {
            log.error("Failed to send payment confirmation: {}", e.getMessage(), e);
        }
    }
}