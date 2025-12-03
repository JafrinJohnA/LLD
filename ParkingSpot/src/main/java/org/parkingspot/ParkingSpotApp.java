package org.parkingspot;

import org.parkingspot.model.*;

import java.util.Scanner;


public class ParkingSpotApp {
    private static void initializeParkingSpots(MonitoringSystem system) {
        System.out.println("--- Initializing Parking Spots ---");

        system.registerSpot(new MotorCycleParkingSpot("MC-101", 5.5)); // Closest
        system.registerSpot(new MotorCycleParkingSpot("MC-102", 15.0));
        system.registerSpot(new MotorCycleParkingSpot("MC-103", 8.2));

        system.registerSpot(new CompactParkingSpot("C-201", 20.0));
        system.registerSpot(new CompactParkingSpot("C-202", 12.5));
        system.registerSpot(new CompactParkingSpot("C-203", 35.0));
        system.registerSpot(new CompactParkingSpot("C-204", 10.1)); // Closest

        system.registerSpot(new LargeParkingSpot("L-301", 50.0));
        system.registerSpot(new LargeParkingSpot("L-302", 40.5)); // Closest
        system.registerSpot(new LargeParkingSpot("L-303", 62.0));

        System.out.println("Registered 10 total spots (3 MC, 4 C, 3 L).\n");
    }

    static void main() {
        // 1. Initialize core system components
        MonitoringSystem parkingSystem = new MonitoringSystem();
        initializeParkingSpots(parkingSystem);

        // 2. Initialize Terminals
        EntryTerminal entryTerminal = new EntryTerminal("E-1", parkingSystem);
        ExitTerminal exitTerminal = new ExitTerminal("X-1", parkingSystem);

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        System.out.println("==========================================");
        System.out.println("   Welcome to the Smart Parking System!   ");
        System.out.println("==========================================");

        while (choice != 4) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1: Process Vehicle Entry");
            System.out.println("2: Process Vehicle Departure");
            System.out.println("3: View Parking Spot Statistics");
            System.out.println("4: Exit Application");
            System.out.print("Enter your choice: ");

            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("\n--- Invalid input. Please enter a number (1-4). ---");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- Option 1: Vehicle Entry ---");
                    entryTerminal.processVehicleArrival(scanner);
                    break;

                case 2:
                    System.out.println("\n--- Option 2: Vehicle Departure ---");
                    System.out.print("Enter the occupied Parking Spot ID (e.g., C-204, L-302): ");
                    String spotId = scanner.nextLine().toUpperCase().trim();
                    exitTerminal.processVehicleDeparture(spotId);
                    break;

                case 3:
                    System.out.println("\n--- Option 3: Parking Statistics ---");
                    parkingSystem.displayStats();
                    break;

                case 4:
                    System.out.println("\nThank you for using the Smart Parking System. Goodbye!");
                    break;

                default:
                    System.out.println("\n--- Invalid choice. Please select 1, 2, 3, or 4. ---");
            }
        }

        scanner.close();
    }
}