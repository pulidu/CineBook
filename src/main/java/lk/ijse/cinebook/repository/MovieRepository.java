package lk.ijse.cinebook.repository;

import lk.ijse.cinebook.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT m FROM Movie m ORDER BY m.popularityScore DESC")
    List<Movie> findTopPopularMovies();

    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= CURRENT_TIMESTAMP ORDER BY m.releaseDate DESC")
    List<Movie> findNowShowing();
}