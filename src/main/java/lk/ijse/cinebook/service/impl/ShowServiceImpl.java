package lk.ijse.cinebook.service.impl;

import lk.ijse.cinebook.dto.request.ShowRequest;
import lk.ijse.cinebook.dto.response.ShowResponse;
import lk.ijse.cinebook.entity.Movie;
import lk.ijse.cinebook.entity.Seat;
import lk.ijse.cinebook.entity.Show;
import lk.ijse.cinebook.repository.MovieRepository;
import lk.ijse.cinebook.repository.SeatRepository;
import lk.ijse.cinebook.repository.ShowRepository;
import lk.ijse.cinebook.service.ShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;

    @Override
    public List<ShowResponse> getAllShows() {
        return showRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowResponse> getShowsByMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return showRepository.findByMovie(movie).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowResponse> getUpcomingShows() {
        return showRepository.findUpcomingShows(java.time.LocalDateTime.now()).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ShowResponse getShowById(Long id) {
        Show show = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Show not found"));
        return convertToResponse(show);
    }

    @Override
    @Transactional
    public ShowResponse createShow(ShowRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Show show = new Show();
        show.setMovie(movie);
        show.setShowDate(request.getShowDate());
        show.setShowTime(request.getShowTime());
        show.setPrice(request.getPrice());

        show = showRepository.save(show);

        // Create seats for this show
        createSeatsForShow(show);

        return convertToResponse(show);
    }

    private void createSeatsForShow(Show show) {
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        for (String row : rows) {
            for (int i = 1; i <= 10; i++) {
                Seat seat = new Seat();
                seat.setShow(show);
                seat.setSeatNumber(row + i);
                seatRepository.save(seat);
            }
        }
    }

    @Override
    @Transactional
    public void deleteShow(Long id) {
        showRepository.deleteById(id);
    }

    private ShowResponse convertToResponse(Show show) {
        return ShowResponse.builder()
                .id(show.getId())
                .movieId(show.getMovie().getId())
                .movieTitle(show.getMovie().getTitle())
                .showDate(show.getShowDate())
                .showTime(show.getShowTime())
                .price(show.getPrice())
                .availableSeats(show.getAvailableSeats())
                .totalSeats(show.getTotalSeats())
                .build();
    }
}