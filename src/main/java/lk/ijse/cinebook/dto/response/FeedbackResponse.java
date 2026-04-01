package lk.ijse.cinebook.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackResponse {
    private Long id;
    private Long userId;
    private String userName;
    private Long movieId;
    private String movieTitle;
    private Integer rating;
    private String comment;
    private LocalDateTime createdDate;
}