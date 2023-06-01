package com.parkit.parkingsystem.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@EqualsAndHashCode
public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehiclePlate;
    private double price;
    private Date inTime;
    private Date outTime;
    private boolean discounted;
}
