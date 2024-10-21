package com.rsuniverse.car_park.services;

import com.rsuniverse.car_park.exception.customExceptions.AuthValidationException;
import com.rsuniverse.car_park.models.dtos.LoginDto;
import com.rsuniverse.car_park.models.dtos.RefreshDto;
import com.rsuniverse.car_park.models.enums.ErrorCode;
import com.rsuniverse.car_park.models.pojos.AuthUser;
import com.rsuniverse.car_park.models.responses.AuthRes;
import com.rsuniverse.car_park.utils.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        // Setup
        LoginDto loginDto = LoginDto.builder()
                .email("test@example.com")
                .password("password")
                .build();
        AuthUser authUser = new AuthUser();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(authUser);
        when(jwtUtils.generateToken(authUser)).thenReturn("accessToken");
        when(jwtUtils.generateRefreshToken(authUser)).thenReturn("refreshToken");

        // Execute
        AuthRes result = authService.login(authenticationManager, loginDto);

        // Verify
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());
    }

    @Test
    void testLogin_InvalidCredentials() {
        // Setup
        LoginDto loginDto = LoginDto.builder()
                .email("test@example.com")
                .password("password")
                .build();
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Execute & Verify
        AuthValidationException exception = assertThrows(AuthValidationException.class, ()
                -> authService.login(authenticationManager, loginDto));
        assertEquals(ErrorCode.INVALID_AUTH, exception.getErrorCode());
    }

    @Test
    void testRefreshToken_Success() {
        // Setup
        RefreshDto refreshDto = RefreshDto.builder()
                .AccessToken("validaccessTokenToken")
                .refreshToken("validRefreshToken")
                .build();
        AuthUser authUser = new AuthUser();
        when(jwtUtils.extractSubject(refreshDto.getRefreshToken())).thenReturn("subject");
        when(jwtUtils.extractUser(refreshDto.getRefreshToken())).thenReturn(authUser);
        when(jwtUtils.validateToken(refreshDto.getRefreshToken(), "subject")).thenReturn(true);
        when(jwtUtils.generateToken(authUser)).thenReturn("newAccessToken");

        // Execute
        AuthRes result = authService.refreshToken(refreshDto);

        // Verify
        assertEquals("newAccessToken", result.getAccessToken());
        assertEquals("validRefreshToken", result.getRefreshToken());
    }

//    @Test
//    void testRefreshToken_Invalid() {
//        // Setup
//        RefreshDto refreshDto = RefreshDto.builder()
//        .AccessToken("validaccessTokenToken")
//        .refreshToken("validRefreshToken")
//        .build();
//        when(jwtUtils.extractSubject(refreshDto.getRefreshToken())).thenReturn("subject");
//        when(jwtUtils.extractUser(refreshDto.getRefreshToken())).thenReturn(null);
//
//        // Execute & Verify
//        AuthValidationException exception = assertThrows(AuthValidationException.class, ()
//                -> authService.refreshToken(refreshDto));
//        assertEquals(ErrorCode.INVALID_AUTH, exception.getErrorCode());
//    }
}
