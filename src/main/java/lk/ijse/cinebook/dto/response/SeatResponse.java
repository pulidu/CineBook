package lk.ijse.cinebook.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatResponse {
    private Long id;
    private String seatNumber;
    private String status;
    private Double price;
    private Boolean locked;
    private Long lockedBy;
    private String lockedUntil;
}