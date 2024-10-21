package com.rsuniverse.car_park.exception.customExceptions;

import com.rsuniverse.car_park.models.enums.ErrorCode;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {
    
    private final ErrorCode errorCode;
    private final String message;

    public UserException(ErrorCode errorCode) {
        super(errorCode.getMessage()); 
        this.errorCode = errorCode;
        this.message = errorCode.getMessage(); 
    }

    public UserException(String message, ErrorCode errorCode) {
        super(message); 
        this.message=message;
        this.errorCode = errorCode;
    }
}
