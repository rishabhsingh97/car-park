package com.rsuniverse.car_park.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.security.core.Authentication;

import com.rsuniverse.car_park.models.entities.Slot;
import com.rsuniverse.car_park.models.pojos.AuthUser;

public class CommonUtils {

    public static Slot assignAuthDetails(Slot slot, Authentication authentication) {
        slot.setCreatedAt(getFormattedDateTime());
        slot.setCreatedBy(getCreatedBy(authentication));
        return slot;
    }

    public static String getCreatedBy(Authentication authentication) {
        AuthUser user = (AuthUser) authentication.getPrincipal();
        return user.getEmail();
    }

    public static String getFormattedDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
        return now.format(formatter);
    }
}
