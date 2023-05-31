package com.parkit.parkingsystem.model;

import lombok.*;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehiclePlate;
    private double price;
    private Date inTime;
    private Date outTime;
    private boolean discounted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return id == ticket.id && Double.compare(ticket.price, price) == 0 && discounted == ticket.discounted && Objects.equals(parkingSpot, ticket.parkingSpot) && Objects.equals(vehiclePlate, ticket.vehiclePlate) && Objects.equals(inTime, ticket.inTime) && Objects.equals(outTime, ticket.outTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parkingSpot, vehiclePlate, price, inTime, outTime, discounted);
    }
}
