package com.rsuniverse.car_park.utils;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rsuniverse.car_park.models.enums.UserRole;
import com.rsuniverse.car_park.models.pojos.AuthUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Value("${jwt.accessToken.expiry}")
    private long userTokenExpiryInMs;

    @Value("${jwt.refreshToken.expiry}")
    private long refreshTokenExpiryInMs;


    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(AuthUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getFirstName().concat(" ").concat(user.getLastName()));
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());
        return createToken(claims, "jobify:user_access_token", userTokenExpiryInMs);
    }

    public String generateRefreshToken(AuthUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("name", user.getFirstName().concat(" ").concat(user.getLastName()));
        claims.put("email", user.getEmail());
        claims.put("roles", user.getRoles());
        return createToken(claims, "jobify:user_refresh_token", refreshTokenExpiryInMs);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expiration)))
                .and()
                .signWith(getKey())
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractSubject(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(Date.from(Instant.now()));
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Set<UserRole> extractRoles(String token) {
        List<String> rolesList = extractClaim(token, claims -> (List<String>) claims.get("roles"));
        
        return rolesList.stream()
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public AuthUser extractUser(String token) {
        int id = extractClaim(token, claims -> (int) claims.get("id"));
        String name = extractClaim(token, claims -> (String) claims.get("name"));
        String email = extractClaim(token, claims -> (String) claims.get("email"));
        Set<UserRole> roles = extractRoles(token);

        AuthUser user = new AuthUser();
        
        String[] nameParts = name.split(" ");
        user.setId(id);
        user.setFirstName(nameParts[0]); 
        user.setLastName(nameParts.length > 1 ? nameParts[1] : ""); 
        user.setEmail(email);
        user.setRoles(roles);
        return user;
    }
}
