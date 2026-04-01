package lk.ijse.cinebook.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lk.ijse.cinebook.dto.request.BookingRequest;
import lk.ijse.cinebook.dto.response.BookingResponse;
import lk.ijse.cinebook.entity.*;
import lk.ijse.cinebook.enums.BookingStatus;
import lk.ijse.cinebook.enums.SeatStatus;
import lk.ijse.cinebook.repository.*;
import lk.ijse.cinebook.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Override
    public List<BookingResponse> getUserBookings() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return bookingRepository.findByUserOrderByBookingTimeDesc(user)
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse getBookingByReference(String reference) {
        Booking booking = bookingRepository.findByBookingReference(reference)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        return convertToResponse(booking);
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found"));

        // ✅ Check availability + LOCK seats
        for (String seatNumber : request.getSeatNumbers()) {

            Seat seat = seatRepository.findByShowAndSeatNumber(show, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatNumber));

            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new RuntimeException("Seat " + seatNumber + " is not available");
            }

            // 🔒 LOCK seat for 5 minutes
            seat.setStatus(SeatStatus.LOCKED);
            seat.setLockedBy(user.getId());
            seat.setLockedUntil(LocalDateTime.now().plusMinutes(5));

            seatRepository.save(seat);
        }

        double totalAmount = request.getSeatNumbers().size() * show.getPrice();

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setSeatNumbers(request.getSeatNumbers());
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING);

        String reference = Booking.generateBookingReference();
        booking.setBookingReference(reference);

        String qrCode = generateQRCode(reference);
        booking.setQrCode(qrCode);

        booking = bookingRepository.save(booking);

        return convertToResponse(booking);
    }

    // ✅ Call this after payment success
    @Override
    @Transactional
    public void confirmBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Show show = booking.getShow();

        for (String seatNumber : booking.getSeatNumbers()) {
            Seat seat = seatRepository.findByShowAndSeatNumber(show, seatNumber).get();

            seat.setStatus(SeatStatus.BOOKED);
            seat.setLockedBy(null);
            seat.setLockedUntil(null);

            seatRepository.save(seat);
        }

        show.setAvailableSeats(show.getAvailableSeats() - booking.getSeatNumbers().size());
        showRepository.save(show);

        Movie movie = show.getMovie();
        movie.setTotalBookings(movie.getTotalBookings() + booking.getSeatNumbers().size());
        updateMoviePopularity(movie);
        movieRepository.save(movie);

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Show show = booking.getShow();

        for (String seatNumber : booking.getSeatNumbers()) {
            Seat seat = seatRepository.findByShowAndSeatNumber(show, seatNumber).get();

            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedBy(null);
            seat.setLockedUntil(null);

            seatRepository.save(seat);
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private void updateMoviePopularity(Movie movie) {
        double score = (movie.getTotalBookings() * 0.6) +
                (movie.getRating() != null ? movie.getRating() * 0.4 : 0);

        movie.setPopularityScore(score);
    }

    private String generateQRCode(String text) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);

            return Base64.getEncoder().encodeToString(out.toByteArray());

        } catch (WriterException | java.io.IOException e) {
            throw new RuntimeException("QR generation failed", e);
        }
    }

    private BookingResponse convertToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .bookingReference(booking.getBookingReference())
                .movieTitle(booking.getShow().getMovie().getTitle())
                .showDate(booking.getShow().getShowDate())
                .showTime(booking.getShow().getShowTime())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus().name())
                .seatNumbers(booking.getSeatNumbers())
                .bookingTime(booking.getBookingTime())
                .qrCode(booking.getQrCode())
                .build();
    }
}