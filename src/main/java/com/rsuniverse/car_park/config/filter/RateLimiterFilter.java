package com.rsuniverse.car_park.config.filter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RateLimiterFilter extends OncePerRequestFilter {

    @Value("${rate.limiter.max-requests}")
    private int maxRequests; 

    @Value("${rate.limiter.time-period}")
    private long timePeriod; 

    private final Map<String, RequestData> requestCounts = new HashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, java.io.IOException {

        String ipAddress = request.getRemoteAddr();
        long currentTime = Instant.now().toEpochMilli();

        RequestData requestData = requestCounts.getOrDefault(ipAddress, new RequestData(0, currentTime));

        if (currentTime - requestData.getLastRequestTime() > timePeriod) {
            requestData.setCount(0);
            requestData.setLastRequestTime(currentTime);
        }

        requestData.incrementCount();

        if (requestData.getCount() > maxRequests) {
            log.warn("Too many requests from IP: {}", ipAddress);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return;
        }

        requestCounts.put(ipAddress, requestData);
        filterChain.doFilter(request, response);
    }

    @Data
    private static class RequestData {
        private int count;
        private long lastRequestTime;

        public RequestData(int count, long lastRequestTime) {
            this.count = count;
            this.lastRequestTime = lastRequestTime;
        }

        public void incrementCount() {
            this.count++;
        }
    }
}
