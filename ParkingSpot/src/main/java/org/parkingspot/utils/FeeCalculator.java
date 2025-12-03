package org.parkingspot.utils;

import org.parkingspot.interfaces.ParkingSpot;
import org.parkingspot.model.MonitoringSystem;
import org.parkingspot.model.ParkingTicket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FeeCalculator {
    public static BigDecimal calculateFee(ParkingTicket ticket, LocalDateTime exitTime, MonitoringSystem monitoringSystem) {
        long hoursParked = java.time.Duration.between(ticket.getIssueTime(), exitTime).toHours();
        if (hoursParked == 0) {
            hoursParked = 1;
        }
        ParkingSpot parkingSpot = monitoringSystem.getParkingSpotById(ticket.getParkingSpotId());
        BigDecimal ratePerHour = getRatePerHour(parkingSpot);
        return ratePerHour.multiply(BigDecimal.valueOf(hoursParked));
    }

    private static BigDecimal getRatePerHour(ParkingSpot parkingSpot) {
        return BigDecimal.valueOf(parkingSpot.getPricePerHour());
    }

}
