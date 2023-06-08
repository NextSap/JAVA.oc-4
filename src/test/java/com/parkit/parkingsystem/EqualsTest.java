package com.parkit.parkingsystem;

import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class EqualsTest {

    @Test
    public void testTicketEquals() {
        EqualsVerifier.simple().forClass(Ticket.class).verify();
    }

    @Test
    public void testParkingSportEquals() {
        EqualsVerifier.simple().forClass(ParkingSpot.class).verify();
    }
}
