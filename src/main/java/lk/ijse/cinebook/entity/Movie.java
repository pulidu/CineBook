package lk.ijse.cinebook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer duration; // in minutes

    private String genre;

    private String language;

    private String posterUrl;

    private String trailerUrl;

    private Double rating; // average rating

    @Column(name = "total_bookings")
    private Integer totalBookings = 0;

    @Column(name = "popularity_score")
    private Double popularityScore = 0.0;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Show> shows;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}