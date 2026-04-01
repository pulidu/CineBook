package lk.ijse.cinebook.repository;

import lk.ijse.cinebook.entity.Movie;
import lk.ijse.cinebook.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovie(Movie movie);

    List<Show> findByShowDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT s FROM Show s WHERE s.showDate >= :date ORDER BY s.showDate ASC")
    List<Show> findUpcomingShows(@Param("date") LocalDateTime date);

    @Query("SELECT s FROM Show s WHERE s.movie.id = :movieId AND s.showDate >= :date")
    List<Show> findShowsByMovieAndDate(@Param("movieId") Long movieId, @Param("date") LocalDateTime date);
}