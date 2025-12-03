package org.parkingspot.model;

import org.parkingspot.constants.SpotType;
import org.parkingspot.interfaces.ParkingSpot;

public class MotorCycleParkingSpot extends ParkingSpot {
    private static final double MOTORCYCLE_RATE = 1.0;
    private final double actualDistanceToTerminal;

    public MotorCycleParkingSpot(String spotId, double distance) {
        // Call the parent constructor with the specific type
        super(spotId, SpotType.MOTORCYCLE);
        this.actualDistanceToTerminal = distance;
    }

    @Override
    public double calculateDistanceToTerminal() {
        return actualDistanceToTerminal;
    }

    @Override
    public double getPricePerHour() {
        return MOTORCYCLE_RATE;
    }
}
