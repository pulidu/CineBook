package lk.ijse.cinebook.controllers;

import lk.ijse.cinebook.dto.request.FeedbackRequest;
import lk.ijse.cinebook.dto.response.FeedbackResponse;
import lk.ijse.cinebook.service.impl.FeedbackServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackServiceImpl feedbackService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbacksByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByMovie(movieId));
    }

    @GetMapping("/movie/{movieId}/my-feedback")
    public ResponseEntity<FeedbackResponse> getUserFeedback(@PathVariable Long movieId) {
        return ResponseEntity.ok(feedbackService.getUserFeedbackForMovie(movieId));
    }

    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@RequestBody FeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.submitFeedback(request));
    }

    @PutMapping("/{feedbackId}")
    public ResponseEntity<FeedbackResponse> updateFeedback(@PathVariable Long feedbackId,
                                                           @RequestBody FeedbackRequest request) {
        return ResponseEntity.ok(feedbackService.updateFeedback(feedbackId, request));
    }
}