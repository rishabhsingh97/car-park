package com.rsuniverse.car_park.models.dtos;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Tag(name = "DTO", description = "Login DTO")
public class LoginDto {

    @Email(message="Invalid email format")
    @NotBlank(message="Email can not be blank")
    String email;
    
    @NotBlank(message="Password can not be blank")
    String password;
}
