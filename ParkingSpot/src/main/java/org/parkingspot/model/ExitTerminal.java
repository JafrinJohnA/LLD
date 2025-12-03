package org.parkingspot.model;

import org.parkingspot.constants.TerminalType;
import org.parkingspot.interfaces.ParkingSpot;
import org.parkingspot.interfaces.Terminal;

import java.time.LocalDateTime;

public class ExitTerminal extends Terminal {

    public ExitTerminal(String terminalId, MonitoringSystem monitoringSystem) {
        super(terminalId, TerminalType.EXIT, monitoringSystem);
    }

    public void processVehicleDeparture(String parkingSpotId) {
        MonitoringSystem system = getMonitoringSystem();

        // 1. Retrieve the spot and the active ticket
        ParkingSpot spot = system.getParkingSpotById(parkingSpotId);

        if (spot == null) {
            displayMessage("Error: Spot ID " + parkingSpotId + " not found in the system.");
            return;
        }

        ParkingTicket ticket = system.getTicketBySpotId(parkingSpotId);

        if (ticket == null) {
            displayMessage("Error: No active ticket found for Spot ID " + parkingSpotId + ". Spot is already FREE.");
            system.releaseSpot(spot); // Ensure spot is marked ACTIVE if it wasn't
            return;
        }


        ticket.setExitTime(LocalDateTime.now().plusHours(2).plusMinutes(30));

        double pricePerHour = spot.getPricePerHour();
        double charge = ticket.calculateFee(pricePerHour);

        displayMessage("Vehicle " + ticket.getVehicleNumber() + " departing from spot " + parkingSpotId);

        long durationHours = java.time.Duration.between(ticket.getIssueTime(), ticket.getExitTime()).toHours();
        long durationMinutes = java.time.Duration.between(ticket.getIssueTime(), ticket.getExitTime()).toMinutes() % 60;

        displayMessage(String.format("Parking duration: %d hours and %d minutes. Hourly Rate: $%.2f",
                durationHours, durationMinutes, pricePerHour));
        displayMessage(String.format("TOTAL PARKING CHARGE: $%.2f", charge));

        // 3. Simulate payment (assuming successful payment)
        ticket.setPaymentStatus(org.parkingspot.constants.PaymentStatus.PAID);
        displayMessage("Payment received. Gate open.");

        // 4. Complete the process: release spot and archive ticket
        system.releaseSpot(spot);
        system.completeTicket(ticket.getParkingTicketId());

        displayMessage("Departure complete. Have a nice day!");
    }
}