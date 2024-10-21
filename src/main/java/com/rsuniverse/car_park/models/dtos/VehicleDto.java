package com.rsuniverse.car_park.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDto {

    @NotBlank(message = "numberPlate cannot be blank")
    private String numberPlate;
    @NotBlank(message = "ownerName cannot be blank")
    private String ownerName;
    @NotBlank(message = "ownerContactNumber cannot be blank")
    private String ownerContactNumber;
}
