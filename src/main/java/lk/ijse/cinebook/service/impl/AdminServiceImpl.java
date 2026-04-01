package lk.ijse.cinebook.service.impl;

import lk.ijse.cinebook.dto.response.DashboardResponse;
import lk.ijse.cinebook.dto.response.FeedbackResponse;
import lk.ijse.cinebook.dto.response.MovieResponse;
import lk.ijse.cinebook.dto.response.UserResponse;
import lk.ijse.cinebook.entity.Payment;
import lk.ijse.cinebook.repository.*;
import lk.ijse.cinebook.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final FeedbackRepository feedbackRepository;
    private final PaymentRepository paymentRepository; // ✅ MISSING FIELD ADDED

    @Override
    public DashboardResponse getDashboardStats() {

        LocalDateTime now = LocalDateTime.now();

        Map<String, Double> revenueByMonth = new HashMap<>();

        for (int i = 0; i < 6; i++) {
            YearMonth month = YearMonth.now().minusMonths(i);

            String monthName = month.getMonth().toString() + " " + month.getYear();

            Double revenue = bookingRepository.getRevenueBetweenDates(
                    month.atDay(1).atStartOfDay(),
                    month.atEndOfMonth().atTime(23, 59, 59)
            );

            revenueByMonth.put(monthName, revenue != null ? revenue : 0.0);
        }

        List<MovieResponse> popularMovies = movieRepository.findTopPopularMovies()
                .stream()
                .limit(5)
                .map(movie -> MovieResponse.builder()
                        .id(movie.getId())
                        .title(movie.getTitle())
                        .rating(movie.getRating())
                        .totalBookings(movie.getTotalBookings())
                        .popularityScore(movie.getPopularityScore())
                        .build())
                .collect(Collectors.toList());

        List<FeedbackResponse> recentFeedbacks = feedbackRepository.findAll()
                .stream()
                .limit(10)
                .map(feedback -> FeedbackResponse.builder()
                        .id(feedback.getId())
                        .userName(feedback.getUser().getName())
                        .movieTitle(feedback.getMovie().getTitle())
                        .rating(feedback.getRating())
                        .comment(feedback.getComment())
                        .createdDate(feedback.getCreatedDate())
                        .build())
                .collect(Collectors.toList());

        // ✅ IMPORTANT: RETURN OBJECT (THIS WAS MISSING)
        return DashboardResponse.builder()
                .totalUsers(userRepository.count())
                .totalMovies(movieRepository.count())
                .totalShows(showRepository.count())
                .totalBookings(bookingRepository.count())
                .revenueByMonth(revenueByMonth)
                .popularMovies(popularMovies)
                .recentFeedbacks(recentFeedbacks)
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
        lk.ijse.cinebook.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            lk.ijse.cinebook.enums.Role role =
                    lk.ijse.cinebook.enums.Role.valueOf(roleName.toUpperCase());

            user.setRole(role);
            userRepository.save(user);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + roleName);
        }
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}