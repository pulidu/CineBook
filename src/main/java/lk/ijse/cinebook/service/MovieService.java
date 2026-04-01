package lk.ijse.cinebook.service;

import lk.ijse.cinebook.dto.request.MovieRequest;
import lk.ijse.cinebook.dto.response.MovieResponse;

import java.util.List;

public interface MovieService {
    List<MovieResponse> getAllMovies();
    List<MovieResponse> getNowShowing();
    List<MovieResponse> getPopularMovies();
    MovieResponse getMovieById(Long id);
    MovieResponse createMovie(MovieRequest request);
    MovieResponse updateMovie(Long id, MovieRequest request);
    void deleteMovie(Long id);

}
