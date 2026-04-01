package lk.ijse.cinebook.dto.request;

import lombok.Data;

@Data
public class FeedbackRequest {
    private Long movieId;
    private Integer rating;
    private String comment;
}