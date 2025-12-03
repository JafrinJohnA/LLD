package org.parkingspot.interfaces;

import lombok.Getter;
import lombok.Setter;
import org.parkingspot.constants.SpotStatus;
import org.parkingspot.constants.SpotType;

@Getter
@Setter
public abstract class ParkingSpot implements Comparable<ParkingSpot>{
    private String parkingSpotId;
    private SpotStatus spotStatus;
    private final SpotType spotType;

    public ParkingSpot(String parkingSpotId, SpotType spotType) {
        // Correctly assign parameters and default status to the instance fields
        this.parkingSpotId = parkingSpotId;
        this.spotType = spotType;
        this.spotStatus = SpotStatus.ACTIVE; // Set the default state
    }
    public abstract double calculateDistanceToTerminal();

    // Abstract method for getting the specific rate
    public abstract double getPricePerHour();

    @Override
    public int compareTo(ParkingSpot other) {
        double thisDistance = this.calculateDistanceToTerminal();
        double otherDistance = other.calculateDistanceToTerminal();

        // This ensures the PriorityQueue acts as a Min-Heap based on distance.
        return Double.compare(thisDistance, otherDistance);
    }
}
