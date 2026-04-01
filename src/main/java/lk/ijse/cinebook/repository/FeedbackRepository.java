package lk.ijse.cinebook.repository;

import lk.ijse.cinebook.entity.Feedback;
import lk.ijse.cinebook.entity.Movie;
import lk.ijse.cinebook.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByMovie(Movie movie);

    List<Feedback> findByUser(User user);

    Optional<Feedback> findByUserAndMovie(User user, Movie movie);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.movie.id = :movieId")
    Double getAverageRatingByMovie(@Param("movieId") Long movieId);

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.movie.id = :movieId")
    Long getFeedbackCountByMovie(@Param("movieId") Long movieId);
}