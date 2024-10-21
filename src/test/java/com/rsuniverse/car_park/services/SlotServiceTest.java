package com.rsuniverse.car_park.services;

import com.rsuniverse.car_park.exception.customExceptions.ParkingLotException;
import com.rsuniverse.car_park.models.dtos.SlotDto;
import com.rsuniverse.car_park.models.dtos.VehicleDto;
import com.rsuniverse.car_park.models.entities.Slot;
import com.rsuniverse.car_park.models.entities.Vehicle;
import com.rsuniverse.car_park.models.enums.ErrorCode;
import com.rsuniverse.car_park.repo.SlotRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SlotServiceTest {

    @InjectMocks
    private SlotService slotService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Authentication authentication;

    private Map<Integer, Slot> slotRepoMap;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        slotRepoMap = SlotRepo.getInstance().getSlotMap();  // Set the repository map directly for testing

        // Initialize slots
        for (int i = 1; i <= 5; i++) {
            Slot newSlot = new Slot();
            newSlot.setId(i);
            newSlot.setVehicle(null);
            slotRepoMap.put(i, newSlot);
        }
    }

//    @Test
//    void testLockSlot_Success() {
//        // Setup
//        VehicleDto vehicleDto = VehicleDto.builder()
//                .numberPlate("ABC123")
//                .build();
//        Vehicle vehicle = Vehicle.builder()
//                .numberPlate("ABC123")
//                .build();
//        vehicle.setNumberPlate("ABC123");
//
//        when(modelMapper.map(any(VehicleDto.class), eq(Vehicle.class))).thenReturn(vehicle);
//        when(modelMapper.map(any(Slot.class), eq(SlotDto.class))).thenAnswer(invocation -> {
//            SlotDto dto = SlotDto.builder()
//                    .id(((Slot) invocation.getArgument(0))
//                            .getId())
//                    .isOccupied(true)
//                    .build();
//            return dto;
//        });
//
//        // Execute
//        SlotDto result = slotService.lockSlot(vehicleDto, authentication);
//
//        // Verify
//        assertEquals(1, result.getId());
//        assertTrue(result.isOccupied());
//        assertEquals("ABC123", slotRepoMap.get(1).getVehicle().getNumberPlate());
//    }

    @Test
    void testLockSlot_VehicleAlreadyParked() {
        // Setup
        Slot occupiedSlot = Slot.builder().
                id(1)
                .vehicle(
                        Vehicle.builder()
                                .numberPlate("ABC123")
                                .ownerName("Rishi")
                                .ownerContactNumber("2435635678")
                                .build()
                )
                .build();
        slotRepoMap.put(1, occupiedSlot);

        VehicleDto vehicleDto = VehicleDto.builder()
                .numberPlate("ABC123")
                .build();

        // Execute & Verify
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> slotService.lockSlot(vehicleDto, authentication));
        assertEquals(ErrorCode.VEHICLE_ALREADY_PARKED, exception.getErrorCode());
    }

//    @Test
//    void testLockSlot_ParkingLotFull() {
//        // Setup
//        for (Slot slot : slotRepoMap.values()) {
//            slot.setVehicle(
//                    Vehicle.builder()
//                            .numberPlate("ABC123")
//                            .ownerName("Rishi")
//                            .ownerContactNumber("2435635678")
//                            .build()
//            );
//        }
//        VehicleDto vehicleDto = VehicleDto.builder()
//                .numberPlate("ABC123")
//                .build();
//
//        // Execute & Verify
//        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> slotService.lockSlot(vehicleDto, authentication));
//        assertEquals(ErrorCode.PARKING_LOT_FULL, exception.getErrorCode());
//    }

    @Test
    void testGetSlotInfo_Success() {
        // Setup
        Slot slot = Slot.builder().
                id(1)
                .vehicle(null)
                .build();
        slotRepoMap.put(1, slot);
        when(modelMapper.map(any(Slot.class), eq(SlotDto.class)))
                .thenReturn(
                        SlotDto.builder()
                                .id(1)
                                .isOccupied(true)
                                .build()
                );

        // Execute
        SlotDto result = slotService.getSlotInfo(1);

        // Verify
        assertEquals(1, result.getId());
        assertFalse(result.isOccupied());
    }

//    @Test
//    void testGetSlotInfo_NotFound() {
//        // Execute & Verify
//        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> slotService.getSlotInfo(1));
//        assertEquals(ErrorCode.INVALID_SLOT, exception.getErrorCode());
//    }

    @Test
    void testReleaseSlot_Success() {
        // Setup

        Vehicle vehicle = Vehicle.builder()
                .numberPlate("ABC123")
                .ownerName("Rishi")
                .ownerContactNumber("2435635678")
                .build();

        Slot slot = Slot.builder()
                .id(1)
                .vehicle(vehicle)
                .build();

        slotRepoMap.put(1, slot);

        VehicleDto vehicleDto = VehicleDto.builder()
                .numberPlate("ABC123")
                .build();

        when(modelMapper.map(any(Slot.class), eq(SlotDto.class)))
                .thenReturn(
                        SlotDto.builder()
                                .id(1)
                                .isOccupied(false)
                                .build()
                );

        // Execute
        SlotDto result = slotService.releaseSlot(vehicleDto);

        // Verify
        assertEquals(1, result.getId());
        assertFalse(result.isOccupied());
        assertNull(slotRepoMap.get(1).getVehicle());
    }

    @Test
    void testReleaseSlot_VehicleNotFound() {
        // Setup
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setNumberPlate("NON_EXISTENT");

        // Execute & Verify
        ParkingLotException exception = assertThrows(ParkingLotException.class, () -> slotService.releaseSlot(vehicleDto));
        assertEquals(ErrorCode.VEHICLE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testGetAllSlots() {
        // Execute
        var result = slotService.getAllSlots();

        // Verify
        assertEquals(5, result.size());
    }

    @Test
    void testGetFreeSlots() {
        // Setup
        slotRepoMap.get(1).setVehicle(new Vehicle("", "", "")); // Mark slot 1 as occupied

        // Execute
        var result = slotService.getFreeSlots();

        // Verify
        assertEquals(4, result.size());
        assertFalse(result.contains(slotRepoMap.get(1))); // Slot 1 should not be in free slots
    }
}
