package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.App;
import com.parkit.parkingsystem.model.Ticket;

import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().getTime() < ticket.getInTime().getTime()) ){
            throw new IllegalArgumentException("Out time provided is incorrect: " + ticket.getOutTime().toString());
        }

        long inTime = TimeUnit.MILLISECONDS.toMinutes(ticket.getInTime().getTime());
        long outTime = TimeUnit.MILLISECONDS.toMinutes(ticket.getOutTime().getTime());

        double carRatePerMinute = Double.parseDouble(App.getConfig("CAR_RATE_PER_HOUR")) / 60;
        double bikeRatePerMinute = Double.parseDouble(App.getConfig("BIKE_RATE_PER_HOUR")) / 60;

        long duration = outTime - inTime;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR:
                ticket.setPrice(duration * carRatePerMinute);
                break;
            case BIKE:
                ticket.setPrice(duration * bikeRatePerMinute);
                break;
            default:
                throw new IllegalArgumentException("Unkown Parking Type");
        }

        if (duration < 30)
            ticket.setPrice(0);

        if (ticket.isDiscounted())
            ticket.setPrice(ticket.getPrice() * 0.95);
    }
}