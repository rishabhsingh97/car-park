package com.rsuniverse.car_park.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    private String numberPlate;
    private String ownerName;
    private String ownerContactNumber;
}
