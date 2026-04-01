package lk.ijse.cinebook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "show_date", nullable = false)
    private LocalDateTime showDate;

    @Column(name = "show_time", nullable = false)
    private String showTime;

    @Column(nullable = false)
    private Double price;

    @Column(name = "available_seats")
    private Integer availableSeats = 0;

    @Column(name = "total_seats")
    private Integer totalSeats = 100; // Default 100 seats per show

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    private List<Seat> seats;

    @OneToMany(mappedBy = "show", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        totalSeats = 100;
        availableSeats = 100;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}