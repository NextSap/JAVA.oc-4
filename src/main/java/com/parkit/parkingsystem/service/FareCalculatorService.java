package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.concurrent.TimeUnit;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket, boolean discount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inTime = TimeUnit.MILLISECONDS.toMinutes(ticket.getInTime().getTime());
        long outTime = TimeUnit.MILLISECONDS.toMinutes(ticket.getOutTime().getTime());

        long duration = outTime - inTime;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_MINUTE);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_MINUTE);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }

        if (duration < 30)
            ticket.setPrice(0);

        if (discount)
            ticket.setPrice(ticket.getPrice() * 0.95);

    }
}