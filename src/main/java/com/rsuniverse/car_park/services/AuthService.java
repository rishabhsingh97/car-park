package com.rsuniverse.car_park.services;

import org.modelmapper.ModelMapper;

import com.rsuniverse.car_park.models.entities.User;
import com.rsuniverse.car_park.models.pojos.AuthUser;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.rsuniverse.car_park.exception.customExceptions.AuthValidationException;
import com.rsuniverse.car_park.models.dtos.LoginDto;
import com.rsuniverse.car_park.models.dtos.RefreshDto;
import com.rsuniverse.car_park.models.enums.ErrorCode;
import com.rsuniverse.car_park.models.responses.AuthRes;
import com.rsuniverse.car_park.repo.UserRepo;
import com.rsuniverse.car_park.utils.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    @Override
    public AuthUser loadUserByUsername(String username) {
        User user = UserRepo.getInstance().getUserByEmail(username);
        if (user == null) {
            throw new AuthValidationException(username.concat(" - user not found with this email"), ErrorCode.USER_NOT_FOUND);
        }
        return modelMapper.map(user, AuthUser.class);
    }

    public AuthRes login(AuthenticationManager authenticationManager, LoginDto loginReq) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword())
            );
        } catch (BadCredentialsException e) {
            log.warn("Invalid credentials for user: {}", loginReq.getEmail());
        } catch (AuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
            throw new AuthValidationException(e.getMessage(), ErrorCode.INVALID_AUTH);
        }

        if (authentication !=null && authentication.isAuthenticated()) {
            AuthUser user = (AuthUser) authentication.getPrincipal();
            String accessToken = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);

            return AuthRes.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        throw new AuthValidationException("User password is wrong", ErrorCode.INVALID_AUTH);
    }

    public AuthRes refreshToken(RefreshDto refreshReq) {
        String refreshToken = refreshReq.getRefreshToken();
        String subject = jwtUtils.extractSubject(refreshToken);
        AuthUser user = jwtUtils.extractUser(refreshToken);

        if (user != null && jwtUtils.validateToken(refreshToken, subject)) {
            String newAccessToken = jwtUtils.generateToken(user);
            return AuthRes.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            log.warn("Invalid refresh token: {}", refreshToken);
            return null;
        }
    }
}
