package com.parkit.parkingsystem;

import com.parkit.parkingsystem.loader.PropertiesLoader;
import com.parkit.parkingsystem.model.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket;
    private ParkingSpot parkingSpotCar;
    private ParkingSpot parkingSpotBike;

    private static PropertiesLoader configLoader;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
        configLoader = new PropertiesLoader();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = Ticket.builder().withDiscounted(false).build();
        parkingSpotCar = ParkingSpot.builder().withId(1).withParkingType(ParkingType.CAR).withIsAvailable(false).build();
        parkingSpotBike = ParkingSpot.builder().withId(1).withParkingType(ParkingType.BIKE).withIsAvailable(false).build();
    }

    @Test
    public void calculateFareCar() {
        Date inTime = new Date(System.currentTimeMillis() - (60 * 60 * 1000));
        Date outTime = new Date();

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpotCar);
        fareCalculatorService.calculateFare(ticket);
        assertEquals(ticket.getPrice(), Double.parseDouble(getConfig("CAR_RATE_PER_HOUR")));
    }

    @Test
    public void calculateFareBike() {
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(parkingSpotBike);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(ticket.getPrice(), Double.parseDouble(getConfig("BIKE_RATE_PER_HOUR")));
    }

    @Test
    public void calculateFareUnknownType() {
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(new ParkingSpot(1, null, false));

        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithFutureInTime() {
        ticket.setInTime(new Date(System.currentTimeMillis() + (60 * 60 * 1000)));
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(parkingSpotBike);

        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime() {
        ticket.setInTime(new Date(System.currentTimeMillis() - (45 * 60 * 1000))); //45 minutes parking time should give 3/4th parking fare
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(parkingSpotBike);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.75 * Double.parseDouble(getConfig("BIKE_RATE_PER_HOUR"))), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime() {
        ticket.setInTime(new Date(System.currentTimeMillis() - (45 * 60 * 1000))); //45 minutes parking time should give 3/4th parking fare
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(parkingSpotCar);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.75 * Double.parseDouble(getConfig("CAR_RATE_PER_HOUR"))), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime() {
        ticket.setInTime(new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000))); //24 hours parking time should give 24 * parking fare per hour
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(parkingSpotCar);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((24 * Double.parseDouble(getConfig("CAR_RATE_PER_HOUR"))), ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThan30minutesParkingTime() {
        ticket.setInTime(new Date(System.currentTimeMillis() - (29 * 60 * 1000))); //Less than 30 min parking time should give 0
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(parkingSpotCar);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithLessThan30minutesParkingTime() {
        ticket.setInTime(new Date(System.currentTimeMillis() - (29 * 60 * 1000))); //Less than 30 min parking time should give 0
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(parkingSpotBike);

        fareCalculatorService.calculateFare(ticket);

        assertEquals(0, ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithDiscount() {
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(parkingSpotCar);
        ticket.setDiscounted(true);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.95 * (1 * Double.parseDouble(getConfig("CAR_RATE_PER_HOUR")))), ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithDiscount() {
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setOutTime(new Date());
        ticket.setParkingSpot(parkingSpotBike);
        ticket.setDiscounted(true);

        fareCalculatorService.calculateFare(ticket);

        assertEquals((0.95 * (1 * Double.parseDouble(getConfig("BIKE_RATE_PER_HOUR")))), ticket.getPrice());
    }

    private String getConfig(String key) {
        return configLoader.getProperties().getProperty(key);
    }
}
