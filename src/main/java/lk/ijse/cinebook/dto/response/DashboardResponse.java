package lk.ijse.cinebook.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardResponse {
    private Long totalUsers;
    private Long totalMovies;
    private Long totalShows;
    private Long totalBookings;
    private Double totalRevenue;
    private Map<String, Double> revenueByMonth;
    private List<MovieResponse> popularMovies;
    private List<FeedbackResponse> recentFeedbacks;
}