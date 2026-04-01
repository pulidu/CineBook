package lk.ijse.cinebook.service;

import lk.ijse.cinebook.dto.request.ShowRequest;
import lk.ijse.cinebook.dto.response.ShowResponse;

import java.util.List;

public interface ShowService {
    List<ShowResponse> getAllShows();
    List<ShowResponse> getShowsByMovie(Long movieId);
    List<ShowResponse> getUpcomingShows();
    ShowResponse getShowById(Long id);
    ShowResponse createShow(ShowRequest request);
    void deleteShow(Long id);
}
