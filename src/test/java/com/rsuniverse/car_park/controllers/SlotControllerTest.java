package com.rsuniverse.car_park.controllers;

import com.rsuniverse.car_park.exception.customExceptions.ParkingLotException;
import com.rsuniverse.car_park.models.dtos.SlotDto;
import com.rsuniverse.car_park.models.dtos.VehicleDto;
import com.rsuniverse.car_park.models.enums.ErrorCode;
import com.rsuniverse.car_park.models.responses.BaseRes;
import com.rsuniverse.car_park.services.SlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SlotControllerTest {

    @InjectMocks
    private SlotController slotController;

    @Mock
    private SlotService slotService;

    @Mock
    private Authentication authentication;

    private VehicleDto vehicleDto;
    private SlotDto slotDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicleDto = new VehicleDto();
        vehicleDto.setNumberPlate("ABC123");

        slotDto = new SlotDto();
        slotDto.setOccupied(true);
    }

    @Test
    void parkVehicle_ShouldReturnSuccess_WhenVehicleIsParked() {
        // Arrange
        when(slotService.lockSlot(any(VehicleDto.class), any(Authentication.class))).thenReturn(slotDto);

        // Act
        ResponseEntity<BaseRes<SlotDto>> response = slotController.parkVehicle(vehicleDto, authentication);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("ABC123 - parked successfully", response.getBody().getMessage());
    }

    @Test
    void parkVehicle_ShouldThrowException_WhenParkingFails() {
        // Arrange
        when(slotService.lockSlot(any(VehicleDto.class), any(Authentication.class)))
                .thenThrow(new ParkingLotException(ErrorCode.PARKING_LOT_FULL));

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> {
            slotController.parkVehicle(vehicleDto, authentication);
        });

        assertEquals(ErrorCode.PARKING_LOT_FULL, exception.getErrorCode());
    }

    @Test
    void getSlotInfo_ShouldReturnSlotInfo_WhenSlotExists() {
        // Arrange
        when(slotService.getSlotInfo(1)).thenReturn(slotDto);

        // Act
        ResponseEntity<BaseRes<SlotDto>> response = slotController.getSlotInfo(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("1 - fetched succesfully", response.getBody().getMessage());
    }

    @Test
    void getSlotInfo_ShouldReturnError_WhenSlotIdIsInvalid() {
        // Act
        ResponseEntity<BaseRes<SlotDto>> response = slotController.getSlotInfo(-1);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Slot number is invalid", response.getBody().getMessage());
    }

    @Test
    void unparkVehicle_ShouldReturnSuccess_WhenVehicleIsUnparked() {
        // Arrange
        when(slotService.releaseSlot(any(VehicleDto.class))).thenReturn(slotDto);

        // Act
        ResponseEntity<BaseRes<SlotDto>> response = slotController.unparkVehicle(vehicleDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("ABC123 - released successfully", response.getBody().getMessage());
    }

    @Test
    void unparkVehicle_ShouldThrowException_WhenUnparkingFails() {
        // Arrange
        when(slotService.releaseSlot(any(VehicleDto.class)))
                .thenThrow(new ParkingLotException(ErrorCode.VEHICLE_NOT_FOUND));

        // Act & Assert
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> {
            slotController.unparkVehicle(vehicleDto);
        });

        assertEquals(ErrorCode.VEHICLE_NOT_FOUND, exception.getErrorCode());
    }
}
