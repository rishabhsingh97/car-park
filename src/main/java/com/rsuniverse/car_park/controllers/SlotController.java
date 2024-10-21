package com.rsuniverse.car_park.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rsuniverse.car_park.models.dtos.SlotDto;
import com.rsuniverse.car_park.models.dtos.VehicleDto;
import com.rsuniverse.car_park.models.enums.ErrorCode;
import com.rsuniverse.car_park.models.responses.BaseRes;
import com.rsuniverse.car_park.services.SlotService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; 
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/slot")
@RequiredArgsConstructor
@Tag(name = "Slot Service", description = "Operations related to slot management")
public class SlotController {

    private final SlotService slotService;

    @PostMapping("/park")
    public ResponseEntity<BaseRes<SlotDto>> parkVehicle(@RequestBody @Valid VehicleDto vehicleDto, Authentication authentication) {
        log.info("Incoming request to park car with license plate: {}", vehicleDto.getNumberPlate());
        SlotDto slotDto = slotService.lockSlot(vehicleDto, authentication);
        return BaseRes.success(slotDto, vehicleDto.getNumberPlate().concat(" - parked successfully"));
    }

    @GetMapping("/{slotId}")
    public ResponseEntity<BaseRes<SlotDto>> getSlotInfo(@PathVariable int slotId) {
        if(slotId <= 0){
            return BaseRes.error("Slot number is invalid", ErrorCode.INVALID_SLOT, HttpStatus.CONFLICT);
        }
        log.info("Incoming request to get slot info for slotId: {}", slotId);
        SlotDto slotDto = slotService.getSlotInfo(slotId);
        return BaseRes.success(slotDto, String.valueOf(slotId).concat(" - fetched succesfully"));
    }

    @PostMapping("/unpark")
    public ResponseEntity<BaseRes<SlotDto>> unparkVehicle(@RequestBody @Valid VehicleDto vehicleDto) {
        log.info("Incoming request to unpark car with license plate: {}", vehicleDto.getNumberPlate());
        SlotDto slotDto = slotService.releaseSlot(vehicleDto);
        return BaseRes.success(slotDto, vehicleDto.getNumberPlate().concat(" - released successfully"));
    }
    
}
