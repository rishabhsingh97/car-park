package com.rsuniverse.car_park.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    PARKING_LOT_FULL(101, "Parking lot full"),
    INVALID_SLOT(102, "Slot number is invalid"),
    VEHICLE_NOT_FOUND(103, "Vehicle not found"),
    VEHICLE_ALREADY_PARKED(104, "Vehicle already parked"),

    UNKNOWN_ERROR_OCCURED(201, "Unknown error occurred"),
    INVALID_FEILDS(202, "Invalid field(s) present in request"),

    USER_NOT_FOUND(301, "User not found"),
    USER_ALREADY_EXISTS(302, "User already exists"),

    INVALID_AUTH(401, "Invalid authentication credentials"),
    ACCESS_DENIED(402, "Invalid/Corrupt Token");

    private final int code;
    private final String message;

}
