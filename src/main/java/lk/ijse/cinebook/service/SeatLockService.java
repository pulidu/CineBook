package lk.ijse.cinebook.service;

import lk.ijse.cinebook.dto.request.SeatLockRequest;
import lk.ijse.cinebook.dto.response.SeatResponse;

import java.util.List;

public interface SeatLockService {
    List<SeatResponse> getAvailableSeats(Long showId);
    List<SeatResponse> lockSeats(SeatLockRequest request);
    void releaseExpiredLocks();

}
