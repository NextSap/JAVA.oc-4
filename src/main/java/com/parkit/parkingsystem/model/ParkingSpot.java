package com.parkit.parkingsystem.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
@EqualsAndHashCode
public class ParkingSpot {
    private int id;
    private ParkingType parkingType;
    private boolean isAvailable;
}
