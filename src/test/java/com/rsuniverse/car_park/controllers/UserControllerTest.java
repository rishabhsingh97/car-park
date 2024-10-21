package com.rsuniverse.car_park.controllers;

import com.rsuniverse.car_park.models.dtos.LoginDto;
import com.rsuniverse.car_park.models.dtos.RefreshDto;
import com.rsuniverse.car_park.models.responses.AuthRes;
import com.rsuniverse.car_park.models.responses.BaseRes;
import com.rsuniverse.car_park.services.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.springframework.security.authentication.AuthenticationManager;

class UserControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    private LoginDto loginDto;
    private RefreshDto refreshDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        refreshDto = new RefreshDto();
        refreshDto.setRefreshToken("someRefreshToken");
    }

    @Test
    void login_ShouldReturnSuccessResponse_WhenLoginIsSuccessful() {
        // Arrange
        AuthRes authRes = new AuthRes("accessToken", "refreshToken");
        when(authService.login(authenticationManager, loginDto)).thenReturn(authRes);

        // Act
        ResponseEntity<BaseRes<AuthRes>> response = authController.login(loginDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@example.com - login successfull", response.getBody().getMessage());
        assertEquals(authRes, response.getBody().getData());
        verify(authService, times(1)).login(authenticationManager, loginDto);
    }

    @Test
    void refreshToken_ShouldReturnSuccessResponse_WhenTokenRefreshIsSuccessful() {
        // Arrange
        AuthRes authRes = new AuthRes("newAccessToken", "someRefreshToken");
        when(authService.refreshToken(refreshDto)).thenReturn(authRes);

        // Act
        ResponseEntity<BaseRes<AuthRes>> response = authController.refreshToken(refreshDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token refresh successfull", response.getBody().getMessage());
        assertEquals(authRes, response.getBody().getData());
        verify(authService, times(1)).refreshToken(refreshDto);
    }
}
