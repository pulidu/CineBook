package lk.ijse.cinebook.service;

import lk.ijse.cinebook.dto.request.FeedbackRequest;
import lk.ijse.cinebook.dto.response.FeedbackResponse;

import java.util.List;

public interface FeedbackService {
    List<FeedbackResponse> getFeedbacksByMovie(Long movieId);
    FeedbackResponse getUserFeedbackForMovie(Long movieId);
    FeedbackResponse submitFeedback(FeedbackRequest request);
    FeedbackResponse updateFeedback(Long feedbackId, FeedbackRequest request);

}
