package lk.ijse.cinebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableScheduling  // Enable scheduled tasks for seat lock release
@Slf4j
public class CineBookApplication {
    public static void main(String[] args) {
        SpringApplication.run(CineBookApplication.class, args);
        log.info("🚀 CineBook Backend Started Successfully!");
        log.info("📝 API Documentation: http://localhost:8080/api");
        log.info("🔐 Authentication, Movie, Booking, Feedback, and Admin Endpoints are active.");
    }
}