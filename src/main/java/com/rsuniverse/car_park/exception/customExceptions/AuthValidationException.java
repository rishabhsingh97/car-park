package com.rsuniverse.car_park.exception.customExceptions;

import com.rsuniverse.car_park.models.enums.ErrorCode;

import lombok.Getter;

@Getter
public class AuthValidationException extends RuntimeException {
    
    private final ErrorCode errorCode;
    private String message;

    public AuthValidationException(ErrorCode errorCode) {
        super(errorCode.getMessage()); 
        this.errorCode = errorCode;
        this.message = errorCode.getMessage(); 
    }

    public AuthValidationException(String message, ErrorCode errorCode) {
        super(message); 
        this.message = message;
        this.errorCode = errorCode;
    }
}
