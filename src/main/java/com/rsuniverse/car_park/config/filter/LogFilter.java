package com.rsuniverse.car_park.config.filter;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {

    private static final ThreadLocal<String> requestHolder = new ThreadLocal<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();
        requestHolder.set(requestId);

        log.info("Request Id: {}", requestId);
        log.info("Request URI: {}", request.getRequestURI());


        try {
            filterChain.doFilter(request, response);
        } finally {
            requestHolder.remove();
        }
    }

    public static String getRequestId() {
        return requestHolder.get();
    }

}
