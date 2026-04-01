package lk.ijse.cinebook.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieRequest {
    private String title;
    private String description;
    private Integer duration;
    private String genre;
    private String language;
    private String posterUrl;
    private String trailerUrl;
    private LocalDateTime releaseDate;
}