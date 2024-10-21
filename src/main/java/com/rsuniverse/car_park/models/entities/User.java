package com.rsuniverse.car_park.models.entities;

import com.rsuniverse.car_park.models.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String createdBy;
    private String createdAt;
    private Set<UserRole> roles;
}
