package org.parkingspot.model;

import org.parkingspot.constants.SpotType;
import org.parkingspot.interfaces.ParkingSpot;

public class LargeParkingSpot extends ParkingSpot {
    private final static double LARGE_PARKING_RATE = 5.0;
    private final double actualDistanceToTerminal;

    public LargeParkingSpot(String parkingSpotId,double actualDistanceToTerminal) {
        super(parkingSpotId, SpotType.LARGE);
        this.actualDistanceToTerminal = actualDistanceToTerminal;
    }

    @Override
    public double calculateDistanceToTerminal() {
        return actualDistanceToTerminal;
    }

    @Override
    public double getPricePerHour() {
        return LARGE_PARKING_RATE;
    }
}
