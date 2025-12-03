package org.parkingspot.model;

import org.parkingspot.constants.PaymentStatus;
import org.parkingspot.constants.SpotStatus;
import org.parkingspot.constants.SpotType;
import org.parkingspot.constants.TicketStatus;
import org.parkingspot.interfaces.ParkingSpot;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.stream.Collectors;

public class MonitoringSystem {

    Map<SpotType, PriorityQueue<ParkingSpot>> availableSpotHeaps;
    Map<String, ParkingTicket> activeTickets; // Map: ticketId -> ticket
    Map<String, String> spotToTicketMap; // Map: spotId -> ticketId
    private final Map<String, ParkingSpot> allParkingSpots;

    public MonitoringSystem() {
        this.allParkingSpots = new HashMap<>();
        this.activeTickets = new HashMap<>();
        this.availableSpotHeaps = new HashMap<>();
        this.spotToTicketMap = new HashMap<>();
    }

    public ParkingTicket getTicketById(String ticketId) {
        return activeTickets.get(ticketId);
    }

    /**
     * Retrieves the active parking ticket associated with a reserved parking spot.
     */
    public ParkingTicket getTicketBySpotId(String parkingSpotId) {
        String ticketId = spotToTicketMap.get(parkingSpotId);
        return activeTickets.get(ticketId);
    }


    public ParkingSpot getParkingSpotById(String parkingSpotId) {
        return allParkingSpots.get(parkingSpotId);
    }

    public ParkingTicket issueTicket(String vehicleNumber, ParkingSpot spot) {
        String ticketId = UUID.randomUUID().toString();

        // Create the ticket using the builder pattern
        ParkingTicket ticket = ParkingTicket.builder()
                .parkingTicketId(ticketId)
                .parkingSpotId(spot.getParkingSpotId())
                .issueTime(LocalDateTime.now())
                .vehicleNumber(vehicleNumber)
                .ticketStatus(TicketStatus.ACTIVE)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        // Store the ticket and map the spot to the ticket for easy lookup
        activeTickets.put(ticketId, ticket);
        spotToTicketMap.put(spot.getParkingSpotId(), ticketId);

        return ticket;
    }

    public ParkingSpot allocateSpot(SpotType requiredType) {
        PriorityQueue<ParkingSpot> heap = availableSpotHeaps.get(requiredType);
        if (heap == null || heap.isEmpty()) {
            System.out.println("--- Monitoring System: Allocation failed. No spots available for " + requiredType + ".");
            return null;
        }
        ParkingSpot allocatedSpot = heap.poll();

        if (allocatedSpot != null) {
            allocatedSpot.setSpotStatus(SpotStatus.RESERVED);
            System.out.println("--- Monitoring System: Allocated Spot ID: " + allocatedSpot.getParkingSpotId()
                    + " (Distance: " + allocatedSpot.calculateDistanceToTerminal() + "m)");
        }
        return allocatedSpot;
    }

    public void registerSpot(ParkingSpot spot) {
        this.allParkingSpots.put(spot.getParkingSpotId(), spot);
        availableSpotHeaps.computeIfAbsent(spot.getSpotType(), k -> new PriorityQueue<>()).add(spot);
    }

    public void releaseSpot(ParkingSpot spot) {
        if (spot != null) {
            spot.setSpotStatus(SpotStatus.ACTIVE);
            availableSpotHeaps.computeIfAbsent(spot.getSpotType(), k -> new PriorityQueue<>()).add(spot);
            // Remove the spot-to-ticket mapping as the spot is now free
            spotToTicketMap.remove(spot.getParkingSpotId());
            System.out.println("--- Monitoring System: Spot " + spot.getParkingSpotId() + " released and available.");
        }
    }

    public void completeTicket(String ticketId) {
        ParkingTicket ticket = activeTickets.remove(ticketId);
        if (ticket != null) {
            System.out.println("--- Monitoring System: Ticket " + ticketId + " completed and removed.");
        }
    }

    /**
     * Calculates and displays the current parking spot statistics.
     */
    public void displayStats() {
        // Group all spots by type and status to get counts
        Map<String, Long> stats = allParkingSpots.values().stream()
                .collect(Collectors.groupingBy(
                        spot -> spot.getSpotType().toString() + "_" + spot.getSpotStatus().toString(),
                        Collectors.counting()
                ));

        System.out.println("\n--- Parking Lot Statistics ---");

        long totalSpots = allParkingSpots.size();
        long totalOccupied = stats.entrySet().stream()
                .filter(e -> e.getKey().endsWith("_RESERVED"))
                .mapToLong(Map.Entry::getValue)
                .sum();
        long totalFree = stats.entrySet().stream()
                .filter(e -> e.getKey().endsWith("_ACTIVE"))
                .mapToLong(Map.Entry::getValue)
                .sum();

        System.out.printf("Total Spots: %d | Occupied: %d | Free: %d\n",
                totalSpots, totalOccupied, totalFree);

        System.out.println("\n--- Breakdown by Type ---");
        for (SpotType type : SpotType.values()) {
            long active = stats.getOrDefault(type.toString() + "_ACTIVE", 0L);
            long reserved = stats.getOrDefault(type.toString() + "_RESERVED", 0L);
            long total = active + reserved;
            System.out.printf("%-12s | Total: %2d | Free: %2d | Occupied: %2d\n",
                    type.toString(), total, active, reserved);
        }
        System.out.println("------------------------------------");
    }
}