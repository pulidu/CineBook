package lk.ijse.cinebook.service.impl;

import lk.ijse.cinebook.dto.request.FeedbackRequest;
import lk.ijse.cinebook.dto.response.FeedbackResponse;
import lk.ijse.cinebook.entity.Feedback;
import lk.ijse.cinebook.entity.Movie;
import lk.ijse.cinebook.entity.User;
import lk.ijse.cinebook.repository.FeedbackRepository;
import lk.ijse.cinebook.repository.MovieRepository;
import lk.ijse.cinebook.repository.UserRepository;
import lk.ijse.cinebook.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Override
    public List<FeedbackResponse> getFeedbacksByMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        return feedbackRepository.findByMovie(movie).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FeedbackResponse getUserFeedbackForMovie(Long movieId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Feedback feedback = feedbackRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        return convertToResponse(feedback);
    }

    @Override
    @Transactional
    public FeedbackResponse submitFeedback(FeedbackRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        // Check if user already gave feedback
        if (feedbackRepository.findByUserAndMovie(user, movie).isPresent()) {
            throw new RuntimeException("You have already given feedback for this movie");
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setMovie(movie);
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());

        feedback = feedbackRepository.save(feedback);

        // Update movie average rating
        updateMovieRating(movie);

        return convertToResponse(feedback);
    }

    @Override
    @Transactional
    public FeedbackResponse updateFeedback(Long feedbackId, FeedbackRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        if (!feedback.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only update your own feedback");
        }

        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment());

        feedback = feedbackRepository.save(feedback);

        // Update movie average rating
        updateMovieRating(feedback.getMovie());

        return convertToResponse(feedback);
    }

    private void updateMovieRating(Movie movie) {
        Double avgRating = feedbackRepository.getAverageRatingByMovie(movie.getId());
        movie.setRating(avgRating != null ? avgRating : 0.0);
        movieRepository.save(movie);
    }

    private FeedbackResponse convertToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .userId(feedback.getUser().getId())
                .userName(feedback.getUser().getName())
                .movieId(feedback.getMovie().getId())
                .movieTitle(feedback.getMovie().getTitle())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdDate(feedback.getCreatedDate())
                .build();
    }
}