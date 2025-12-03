package org.parkingspot.model;

import org.parkingspot.constants.SpotType;
import org.parkingspot.constants.TerminalType;
import org.parkingspot.interfaces.ParkingSpot;
import org.parkingspot.interfaces.Terminal;

import java.util.Scanner;

public class EntryTerminal extends Terminal {

    public EntryTerminal(String terminalId, MonitoringSystem monitoringSystem) {
        super(terminalId, TerminalType.ENTRY, monitoringSystem);
    }

    private SpotType promptForVehicleType(Scanner scanner) {
        displayMessage("Please enter the vehicle type (1: LARGE, 2: COMPACT, 3: MOTORCYCLE):");

        // Input validation loop
        while (!scanner.hasNextInt()) {
            displayMessage("Invalid input. Please enter a number (1, 2, or 3):");
            scanner.next(); // Consume invalid input
        }
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the remaining newline

        return switch (choice) {
            case 1 -> SpotType.LARGE;
            case 2 -> SpotType.COMPACT;
            case 3 -> SpotType.MOTORCYCLE;
            default -> {
                displayMessage("Invalid choice (" + choice + "). Defaulting to LARGE.");
                yield SpotType.LARGE;
            }
        };
    }

    public void processVehicleArrival(Scanner scanner) {
        displayMessage("--- Starting New Vehicle Entry ---");

        displayMessage("Please enter your vehicle's license plate number:");
        String vehicleNumber = scanner.nextLine().toUpperCase().trim(); // Use nextLine() for full plate

        displayMessage("Vehicle " + vehicleNumber + " attempting entry.");

        SpotType requiredType = promptForVehicleType(scanner);

        // Min-Heap allocation happens here
        ParkingSpot allocatedSpot = getMonitoringSystem().allocateSpot(requiredType);

        if (allocatedSpot == null) {
            displayMessage("PARKING LOT IS FULL for " + requiredType + " vehicles! Denying entry.");
            return;
        }

        // Issue the ticket and track it
        ParkingTicket ticket = getMonitoringSystem().issueTicket(vehicleNumber, allocatedSpot);

        displayMessage("Entry granted! Ticket ID: " + ticket.getParkingTicketId());
        displayMessage("Please proceed to Spot ID: " + allocatedSpot.getParkingSpotId()
                + " (Type: " + allocatedSpot.getSpotType() + ", Dist: " + String.format("%.1f", allocatedSpot.calculateDistanceToTerminal()) + "m)");

    }
}