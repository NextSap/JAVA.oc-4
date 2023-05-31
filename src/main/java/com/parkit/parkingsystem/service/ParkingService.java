package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.model.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

public class ParkingService {

    private static final Logger logger = LogManager.getLogger("ParkingService");

    private static final FareCalculatorService fareCalculatorService = new FareCalculatorService();

    private final InputReaderUtil inputReaderUtil;
    private final ParkingSpotDAO parkingSpotDAO;
    private final TicketDAO ticketDAO;

    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO) {
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    public void processIncomingVehicle() {
        try {
            ParkingSpot parkingSpot = getParkingSpotAvailable();
            if (parkingSpot == null)
                return;

            parkingSpot.setAvailable(false);

            String vehiclePlate = askForVehiclePlate();
            boolean discounted = ticketDAO.getAmountTicket(vehiclePlate) > 0;

            parkingSpotDAO.updateParking(parkingSpot);

            Ticket ticket = Ticket.builder().withParkingSpot(parkingSpot).withVehiclePlate(vehiclePlate)
                    .withInTime(new Date()).withDiscounted(discounted).build();
            ticketDAO.saveTicket(ticket);

            if (discounted)
                System.out.println("Happy to see you again ! As a regular user of our parking, you will get 5% off on your ticket");

            System.out.println("Generated Ticket and saved in DB");
            System.out.println("Please park your vehicle in spot number:" + parkingSpot.getId());
            System.out.println("Recorded in-time for vehicle number:" + ticket.getVehiclePlate() + " is:" + ticket.getInTime());
        } catch (Exception e) {
            logger.error("Unable to process incoming vehicle", e);
        }
    }

    public String askForVehiclePlate() {
        System.out.println("Please type the vehicle plate and press enter key");
        return inputReaderUtil.readVehiclePlate();
    }

    public ParkingSpot getParkingSpotAvailable() {
        ParkingType parkingType = askForVehicleType();
        int parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);

        return parkingNumber > 0 ? new ParkingSpot(parkingNumber, parkingType, true) : null;
    }

    public ParkingType askForVehicleType() {
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1:
                return ParkingType.CAR;
            case 2:
                return ParkingType.BIKE;
            default:
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
        }
    }

    public void processExitingVehicle() {
        try {
            String vehiclePlate = askForVehiclePlate();

            Ticket ticket = ticketDAO.getTicket(vehiclePlate);
            ticket.setOutTime(new Date());

            fareCalculatorService.calculateFare(ticket);

            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);

                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehiclePlate() + " is:" + ticket.getOutTime());
            } else {
                System.out.println("Unable to update ticket information. Error occurred");
            }
        } catch (Exception e) {
            logger.error("Unable to process exiting vehicle", e);
        }
    }
}
