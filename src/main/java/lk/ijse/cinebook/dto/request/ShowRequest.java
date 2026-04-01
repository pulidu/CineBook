package lk.ijse.cinebook.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowRequest {
    private Long movieId;
    private LocalDateTime showDate;
    private String showTime;
    private Double price;
}