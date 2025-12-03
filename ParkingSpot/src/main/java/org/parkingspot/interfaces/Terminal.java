package org.parkingspot.interfaces;

import lombok.Getter;
import lombok.Setter;
import org.parkingspot.constants.TerminalType;
import org.parkingspot.model.MonitoringSystem;
import org.parkingspot.model.ParkingTicket;
import org.parkingspot.model.PaymentTransaction;

@Getter
@Setter
public abstract class Terminal {

    private final String terminalId;
    private final TerminalType type;
    private final MonitoringSystem monitoringSystem;

    public Terminal(String terminalId, TerminalType type, MonitoringSystem monitoringSystem) {
        this.terminalId = terminalId;
        this.type = type;
        this.monitoringSystem = monitoringSystem;
    }

    public void displayMessage(String message) {
        System.out.println("[" + terminalId + "]: " + message);
    }

    public ParkingTicket scanTicket(String ticketId) {
        displayMessage("Scanning ticket: " + ticketId);
        return monitoringSystem.getTicketById(ticketId);
    }

}
