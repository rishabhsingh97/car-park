package com.rsuniverse.car_park.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rsuniverse.car_park.exception.customExceptions.ParkingLotException;
import com.rsuniverse.car_park.models.dtos.SlotDto;
import com.rsuniverse.car_park.models.dtos.VehicleDto;
import com.rsuniverse.car_park.models.entities.Slot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;

import com.rsuniverse.car_park.models.entities.Vehicle;
import com.rsuniverse.car_park.models.enums.ErrorCode;
import com.rsuniverse.car_park.repo.SlotRepo;
import com.rsuniverse.car_park.utils.CommonUtils;

import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlotService {

    private final ModelMapper modelMapper;

    @Value("${slot.number:5}")
    private int numberOfSlots;

    @PostConstruct
    public void initSlots() {
        for (int i = 0; i < numberOfSlots; i++) {
            Slot newSlot = Slot.builder().id(i + 1).build();
            SlotRepo.getInstance().addSlot(newSlot); // Add slot to the repository
        }
    }

    public SlotDto lockSlot(VehicleDto vehicleDto, Authentication authentication) {
        log.info("Attempting to park car with license plate: {}", vehicleDto.getNumberPlate());
        Map<Integer, Slot> slotRepo = SlotRepo.getInstance().getSlotMap();

        // Check for duplicate number plate
        for (Slot slot : slotRepo.values()) {
            if (slot.isOccupied() && slot.getVehicle().getNumberPlate().equals(vehicleDto.getNumberPlate())) {
                log.warn("Failed to park car: Vehicle with license plate {} is already parked in slot: {}", vehicleDto.getNumberPlate(), slot.getId());
                throw new ParkingLotException(ErrorCode.VEHICLE_ALREADY_PARKED);
            }
        }

        Vehicle vehicle = modelMapper.map(vehicleDto, Vehicle.class);
        for (Slot slot : slotRepo.values()) {
            if (!slot.isOccupied()) {
                slot.setVehicle(vehicle);
                slot.setEntryTimestamp(CommonUtils.getFormattedDateTime());
                CommonUtils.assignAuthDetails(slot, authentication);
                log.info("Car parked successfully in slot: {}", slot.getId());
                SlotDto slotDto = modelMapper.map(slot, SlotDto.class);
                slotDto.setOccupied(true); // Mark the slot as occupied
                return slotDto;
            }
        }

        log.warn("Failed to park car with license plate: {}. Parking lot may be full.", vehicleDto.getNumberPlate());
        throw new ParkingLotException(ErrorCode.PARKING_LOT_FULL);
    }

    public SlotDto getSlotInfo(int slotId) {
        log.info("Fetching slot info for slot ID: {}", slotId);
        Slot slot = SlotRepo.getInstance().getSlotById(slotId);

        if (slot != null) {
            SlotDto slotDto = modelMapper.map(slot, SlotDto.class);
            slotDto.setOccupied(slot.isOccupied());
            return slotDto;
        } else {
            log.warn("No slot found for ID: {}", slotId);
            throw new ParkingLotException(ErrorCode.INVALID_SLOT);
        }
    }

    public SlotDto releaseSlot(VehicleDto vehicleDto) {
        log.info("Attempting to unpark car with license plate: {}", vehicleDto.getNumberPlate());

        Map<Integer, Slot> slotRepo = SlotRepo.getInstance().getSlotMap();
        for (Slot slot : slotRepo.values()) {
            if (slot.isOccupied() && slot.getVehicle().getNumberPlate().equals(vehicleDto.getNumberPlate())) {
                slot.setVehicle(null);
                log.info("Car with license plate: {} successfully removed from slot: {}", vehicleDto.getNumberPlate(), slot.getId());
                SlotDto slotDto = modelMapper.map(slot, SlotDto.class);
                slotDto.setOccupied(false); // Mark the slot as unoccupied
                slotDto.setExitTimestamp(CommonUtils.getFormattedDateTime());
                return slotDto;
            }
        }

        log.warn("No car found with license plate: {} to unpark", vehicleDto.getNumberPlate());
        throw new ParkingLotException(ErrorCode.VEHICLE_NOT_FOUND);
    }

    public List<Slot> getAllSlots() {
        return new ArrayList<>(SlotRepo.getInstance().getSlotMap().values()); 
    }

    public List<Slot> getFreeSlots() {
        return SlotRepo.getInstance().getSlotMap().values().stream()
                .filter(slot -> !slot.isOccupied())
                .collect(Collectors.toList()); 
    }
}
