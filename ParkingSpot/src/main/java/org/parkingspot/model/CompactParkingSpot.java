package org.parkingspot.model;

import org.parkingspot.constants.SpotType;
import org.parkingspot.interfaces.ParkingSpot;

public class CompactParkingSpot extends ParkingSpot {
    private final static double COMPACT_PARKING_RATE = 2.0;
    private final double actualDistanceToTerminal;

    public CompactParkingSpot(String parkingSpotId, double distance) {
        super(parkingSpotId, SpotType.COMPACT);
        this.actualDistanceToTerminal = distance;
    }

    @Override
    public double calculateDistanceToTerminal() {
        return actualDistanceToTerminal;
    }

    @Override
    public double getPricePerHour() {
        return COMPACT_PARKING_RATE;
    }

}
