package com.rsuniverse.car_park.models.responses;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rsuniverse.car_park.config.filter.LogFilter;
import com.rsuniverse.car_park.models.enums.ErrorCode;

@Slf4j
@Data
@Builder
public class BaseRes<T> {

    private T data;
    private boolean success;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int errorCode;

    private String requestId;

    public static <T> ResponseEntity<BaseRes<T>> success(T data, String message) {
        BaseRes<T> response = BaseRes.<T>builder()
            .data(data)
            .success(true)
            .message(message)
            .requestId(LogFilter.getRequestId())
            .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<BaseRes<T>> error(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        log.info(">>> ", message);
        BaseRes<T> response = BaseRes.<T>builder()
            .data(null)
            .success(false)
            .message(message)
            .errorCode(errorCode.getCode())
            .requestId(LogFilter.getRequestId())
            .build();
        return ResponseEntity.status(httpStatus).body(response);
    }
}
