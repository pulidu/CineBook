package lk.ijse.cinebook.repository;

import lk.ijse.cinebook.entity.Seat;
import lk.ijse.cinebook.entity.Show;
import lk.ijse.cinebook.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowAndStatus(Show show, SeatStatus status);

    List<Seat> findByShowAndStatusIn(Show show, List<SeatStatus> statuses);

    Optional<Seat> findByShowAndSeatNumber(Show show, String seatNumber);

    @Modifying
    @Transactional
    @Query("UPDATE Seat s SET s.status = :status, s.lockedBy = :userId, s.lockedUntil = :lockedUntil WHERE s.id = :seatId AND s.status = 'AVAILABLE'")
    int lockSeat(@Param("seatId") Long seatId, @Param("status") SeatStatus status,
                 @Param("userId") Long userId, @Param("lockedUntil") LocalDateTime lockedUntil);

    @Modifying
    @Transactional
    @Query("UPDATE Seat s SET s.status = 'AVAILABLE', s.lockedBy = NULL, s.lockedUntil = NULL WHERE s.status = 'LOCKED' AND s.lockedUntil < :currentTime")
    int releaseExpiredLocks(@Param("currentTime") LocalDateTime currentTime);
}