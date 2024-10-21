package com.rsuniverse.car_park.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.rsuniverse.car_park.exception.customExceptions.AuthValidationException;
import com.rsuniverse.car_park.exception.customExceptions.VehicleException;
import com.rsuniverse.car_park.exception.customExceptions.ParkingLotException;
import com.rsuniverse.car_park.exception.customExceptions.UserException;
import com.rsuniverse.car_park.models.enums.ErrorCode;
import com.rsuniverse.car_park.models.responses.BaseRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseRes<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return BaseRes.error(message, ErrorCode.INVALID_FEILDS, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseRes<String>> handleException(Exception ex, WebRequest request) {
        log.error("error : {}", ex.getMessage());
        return BaseRes.error(ex.getMessage(), ErrorCode.UNKNOWN_ERROR_OCCURED, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Custom Excxeption
    @ExceptionHandler(AuthValidationException.class)
    public ResponseEntity<BaseRes<String>> handleException(AuthValidationException ex) {
        log.error("error : {}", ex.getMessage());
        return BaseRes.error(ex.getMessage(), ex.getErrorCode(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<BaseRes<String>> handleException(UserException ex) {
        log.error("error : {}", ex.getMessage());
        return BaseRes.error(ex.getMessage(), ex.getErrorCode(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ParkingLotException.class)
    public ResponseEntity<BaseRes<String>> handleException(ParkingLotException ex) {
        log.error("error : {}", ex.getMessage());
        return BaseRes.error(ex.getMessage(), ex.getErrorCode(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(VehicleException.class)
    public ResponseEntity<BaseRes<String>> handleException(VehicleException ex) {
        log.error("error : {}", ex.getMessage());
        return BaseRes.error(ex.getMessage(), ex.getErrorCode(), HttpStatus.CONFLICT);
    }
}
