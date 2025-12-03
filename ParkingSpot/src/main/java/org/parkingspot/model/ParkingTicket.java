package org.parkingspot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.parkingspot.constants.PaymentStatus;
import org.parkingspot.constants.TicketStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParkingTicket {
    private String parkingTicketId;
    private String parkingSpotId;
    private LocalDateTime issueTime;
    private LocalDateTime exitTime;
    private String vehicleNumber;
    private BigDecimal parkingFee;
    private double parkingCharge;
    private PaymentStatus paymentStatus;
    private TicketStatus ticketStatus;

    public double calculateFee(double pricePerHour) {
        if (exitTime == null) {
            // Should be set by ExitTerminal, but setting a safe default if missed.
            exitTime = LocalDateTime.now();
        }

        long secondsParked = java.time.Duration.between(issueTime, exitTime).getSeconds();
        // Calculate hours and ensure a minimum of 1 hour charge
        double hours = Math.max(1.0, (double) secondsParked / 3600.0);

        this.parkingCharge = hours * pricePerHour;
        return this.parkingCharge;
    }

}
