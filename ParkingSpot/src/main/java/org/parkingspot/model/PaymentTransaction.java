package org.parkingspot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTransaction {
    private String transactionId;
    private String parkingTicketId; // Link to the ticket

    // Financial Details
    private BigDecimal feeAmount;
    private LocalDateTime transactionTime;
    private String terminalId;
}
