package lk.ijse.cinebook.controllers;

import lk.ijse.cinebook.dto.request.ShowRequest;
import lk.ijse.cinebook.dto.response.ShowResponse;
import lk.ijse.cinebook.service.impl.ShowServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shows")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ShowController {

    private final ShowServiceImpl showService;

    @GetMapping
    public ResponseEntity<List<ShowResponse>> getAllShows() {
        return ResponseEntity.ok(showService.getAllShows());
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ShowResponse>> getShowsByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(showService.getShowsByMovie(movieId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<ShowResponse>> getUpcomingShows() {
        return ResponseEntity.ok(showService.getUpcomingShows());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowResponse> getShowById(@PathVariable Long id) {
        return ResponseEntity.ok(showService.getShowById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowResponse> createShow(@RequestBody ShowRequest request) {
        return ResponseEntity.ok(showService.createShow(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.ok().build();
    }
}