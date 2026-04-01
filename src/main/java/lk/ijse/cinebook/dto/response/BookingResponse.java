package lk.ijse.cinebook.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private String bookingReference;
    private String movieTitle;
    private LocalDateTime showDate;
    private String showTime;
    private Double totalAmount;
    private String status;
    private List<String> seatNumbers;
    private LocalDateTime bookingTime;
    private String qrCode;
}