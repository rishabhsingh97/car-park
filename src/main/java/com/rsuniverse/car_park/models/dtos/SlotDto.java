package com.rsuniverse.car_park.models.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rsuniverse.car_park.models.entities.Vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SlotDto {

    private int id;
    private Vehicle vehicle;
    private String entryTimestamp;
    private String exitTimestamp;
    private String createdBy;
    private String createdAt;
    private boolean isOccupied;

}
