package lk.ijse.cinebook.service.impl;

import lk.ijse.cinebook.dto.request.SeatLockRequest;
import lk.ijse.cinebook.dto.response.SeatResponse;
import lk.ijse.cinebook.entity.Seat;
import lk.ijse.cinebook.entity.Show;
import lk.ijse.cinebook.entity.User;
import lk.ijse.cinebook.enums.SeatStatus;
import lk.ijse.cinebook.repository.SeatRepository;
import lk.ijse.cinebook.repository.ShowRepository;
import lk.ijse.cinebook.repository.UserRepository;
import lk.ijse.cinebook.service.SeatLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeatLockServiceImpl implements SeatLockService {

    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;

    private static final int LOCK_DURATION_MINUTES = 5;

    @Override
    public List<SeatResponse> getAvailableSeats(Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Show not found"));

        // Release expired locks before fetching
        releaseExpiredLocks();

        List<Seat> seats = seatRepository.findByShowAndStatusIn(show,
                List.of(SeatStatus.AVAILABLE, SeatStatus.LOCKED));

        return seats.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SeatResponse> lockSeats(SeatLockRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Show show = showRepository.findById(request.getShowId())
                .orElseThrow(() -> new RuntimeException("Show not found"));

        LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(LOCK_DURATION_MINUTES);

        for (String seatNumber : request.getSeatNumbers()) {
            Seat seat = seatRepository.findByShowAndSeatNumber(show, seatNumber)
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seatNumber));

            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                throw new RuntimeException("Seat " + seatNumber + " is not available");
            }

            int updated = seatRepository.lockSeat(seat.getId(), SeatStatus.LOCKED,
                    user.getId(), lockedUntil);

            if (updated == 0) {
                throw new RuntimeException("Failed to lock seat: " + seatNumber);
            }
        }

        return getAvailableSeats(request.getShowId());
    }

    @Override
    @Scheduled(fixedDelay = 60000) // Run every minute
    @Transactional
    public void releaseExpiredLocks() {
        int released = seatRepository.releaseExpiredLocks(LocalDateTime.now());
        if (released > 0) {
            log.info("Released {} expired seat locks", released);
        }
    }

    private SeatResponse convertToResponse(Seat seat) {
        return SeatResponse.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus().name())
                .locked(seat.isLocked())
                .lockedBy(seat.getLockedBy())
                .lockedUntil(seat.getLockedUntil() != null ? seat.getLockedUntil().toString() : null)
                .build();
    }
}