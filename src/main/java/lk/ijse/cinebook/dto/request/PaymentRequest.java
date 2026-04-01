package lk.ijse.cinebook.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long bookingId;
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;
    private Double amount;
}