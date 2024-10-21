package com.rsuniverse.car_park.exception.customExceptions;

import com.rsuniverse.car_park.models.enums.ErrorCode;

import lombok.Getter;

@Getter
public class VehicleException extends RuntimeException {
    
    private final ErrorCode errorCode;
    private final String message;

    public VehicleException(ErrorCode errorCode) {
        super(errorCode.getMessage()); 
        this.errorCode = errorCode;
        this.message = errorCode.getMessage(); 
    }

    public VehicleException(String message, ErrorCode errorCode) {
        super(message); 
        this.errorCode = errorCode;
        this.message=message;
    }
}
