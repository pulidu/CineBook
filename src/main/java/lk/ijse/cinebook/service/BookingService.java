package lk.ijse.cinebook.service;

import lk.ijse.cinebook.dto.request.BookingRequest;
import lk.ijse.cinebook.dto.response.BookingResponse;

import java.util.List;

public interface BookingService {
    List<BookingResponse> getUserBookings();
    BookingResponse getBookingByReference(String reference);
    BookingResponse createBooking(BookingRequest request);
    void confirmBooking(Long bookingId);
    void cancelBooking(Long bookingId);


}
