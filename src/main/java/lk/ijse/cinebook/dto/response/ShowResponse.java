package lk.ijse.cinebook.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ShowResponse {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private LocalDateTime showDate;
    private String showTime;
    private Double price;
    private Integer availableSeats;
    private Integer totalSeats;
}