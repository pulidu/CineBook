package lk.ijse.cinebook.repository;

import lk.ijse.cinebook.entity.Booking;
import lk.ijse.cinebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user);

    List<Booking> findByUserOrderByBookingTimeDesc(User user);

    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findByShowId(Long showId);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.show.id = :showId")
    Long countBookingsByShow(@Param("showId") Long showId);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.bookingTime BETWEEN :startDate AND :endDate AND b.status = 'CONFIRMED'")
    Double getRevenueBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}