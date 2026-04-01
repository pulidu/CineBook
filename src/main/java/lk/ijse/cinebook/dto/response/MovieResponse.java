package lk.ijse.cinebook.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MovieResponse {
    private Long id;
    private String title;
    private String description;
    private Integer duration;
    private String genre;
    private String language;
    private String posterUrl;
    private String trailerUrl;
    private Double rating;
    private Integer totalBookings;
    private Double popularityScore;
    private LocalDateTime releaseDate;
}