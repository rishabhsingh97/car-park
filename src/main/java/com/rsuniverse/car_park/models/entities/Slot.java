package com.rsuniverse.car_park.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slot {

    private int id;
    private Vehicle vehicle;
    private String entryTimestamp;
    private String createdBy;
    private String createdAt;

    public boolean isOccupied() {
        return vehicle != null;
    }

}
