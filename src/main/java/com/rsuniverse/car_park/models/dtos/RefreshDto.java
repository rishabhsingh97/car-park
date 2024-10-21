package com.rsuniverse.car_park.models.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshDto {
    
    @NotBlank(message="Access Token can not be blank")
    String AccessToken;
    
    @NotBlank(message="Refresh Token can not be blank")
    String refreshToken;
}
