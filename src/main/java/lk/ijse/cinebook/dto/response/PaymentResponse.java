package lk.ijse.cinebook.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private Long paymentId;
    private String paymentStatus;
    private String transactionId;
    private String message;
    private Double amount;
    private String paymentDate;
    private String redirectUrl;
}