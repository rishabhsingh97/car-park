package com.rsuniverse.car_park.config.filter;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.rsuniverse.car_park.models.pojos.AuthUser;
import com.rsuniverse.car_park.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        AuthUser user = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                user = jwtUtils.extractUser(token);
            } catch (Exception e) {
                handleErrorResponse(response, "Corrupt token", HttpServletResponse.SC_UNAUTHORIZED);
                return; 
            }
        }

        if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtils.validateToken(token, "jobify:user_access_token")) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                handleErrorResponse(response, "Invalid/expired token", HttpServletResponse.SC_UNAUTHORIZED);
                return; 
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleErrorResponse(HttpServletResponse response, String errorMessage, int statusCode) throws IOException {
        log.error("Error occurred: {}", errorMessage);
        response.setContentType("application/json");
        response.setStatus(statusCode);
        String requestId = LogFilter.getRequestId();
        String json = String.format("{\"error\": \"%s\", \"requestId\":\"%s\"}", errorMessage, requestId);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
