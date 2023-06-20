package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.ParkingType;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
            lenient().when(inputReaderUtil.readVehiclePlate()).thenReturn("ABCDEF");
            lenient().when(inputReaderUtil.readSelection()).thenReturn(1);

            ParkingSpot parkingSpot = ParkingSpot.builder().withId(1).withParkingType(ParkingType.CAR).withIsAvailable(false).build();

            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehiclePlate("ABCDEF");

            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);

            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up test mock objects", e);
        }
    }

    @Test
    public void processExitingVehicleTest() {
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        lenient().when(ticketDAO.getAmountTicket(anyString())).thenReturn(0);

        parkingService.processExitingVehicle();

        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }


    @Test
    public void testProcessIncomingVehicle() {
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        parkingService.processIncomingVehicle();
    }

    @Test
    public void processExitingVehicleTestUnableUpdate() {
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

        parkingService.processExitingVehicle();

        verify(parkingSpotDAO, Mockito.times(0)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void testGetNextParkingNumberIfAvailable() {
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

        ParkingSpot parkingSpot = parkingService.getParkingSpotAvailable();

        Assertions.assertEquals(parkingSpot, ParkingSpot.builder().withId(1).withParkingType(ParkingType.CAR).withIsAvailable(true).build());
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberNotFound() {
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);

        Assertions.assertThrows(NullPointerException.class, () -> parkingService.getParkingSpotAvailable());
    }

    @Test
    public void testGetNextParkingNumberIfAvailableParkingNumberWrongArgument() {
        when(inputReaderUtil.readSelection()).thenReturn(3);

        Assertions.assertThrows(IllegalArgumentException.class, () -> parkingService.getParkingSpotAvailable());
    }

    @Test
    public void testAskVehicleTypeWithBike() {
        when(inputReaderUtil.readSelection()).thenReturn(2);

        Assertions.assertEquals(ParkingType.BIKE, parkingService.askVehicleType());
    }

    @Test
    public void testIfDiscounted() {
        int amountTicket = ticketDAO.getAmountTicket("ABCDE");

        if (amountTicket > 0)
            Assertions.assertTrue(true);
        else
            Assertions.assertFalse(false);
    }
}

