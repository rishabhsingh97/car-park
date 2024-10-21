package com.rsuniverse.car_park.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rsuniverse.car_park.models.dtos.LoginDto;
import com.rsuniverse.car_park.models.dtos.RefreshDto;
import com.rsuniverse.car_park.models.responses.AuthRes;
import com.rsuniverse.car_park.models.responses.BaseRes;
import com.rsuniverse.car_park.services.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Service", description = "Operations related to staff authentication")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<BaseRes<AuthRes>> login(@RequestBody @Valid LoginDto loginDto) {
        AuthRes authRes = authService.login(authenticationManager, loginDto);
        return BaseRes.success(authRes, loginDto.getEmail().concat(" - login successfull"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<BaseRes<AuthRes>> refreshToken(@RequestBody @Valid RefreshDto refreshDto) {
        AuthRes authRes = authService.refreshToken(refreshDto);
        return BaseRes.success(authRes, "token refresh successfull");
    }

}
