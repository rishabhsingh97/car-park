package com.rsuniverse.car_park.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rsuniverse.car_park.models.dtos.SlotDto;
import com.rsuniverse.car_park.models.dtos.VehicleDto;
import com.rsuniverse.car_park.models.entities.Slot;
import com.rsuniverse.car_park.models.entities.Vehicle;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BeansConfig {

    // Auth beans
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        try {
            return authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            return null;
        }
    }

    //Mapper config beans
    @Bean
    public ModelMapper modelMapper() {
         ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        modelMapper.typeMap(Slot.class, SlotDto.class).addMappings(mapper -> {
            mapper.map(src -> src.isOccupied(), SlotDto::setOccupied);
            mapper.map(Slot::getVehicle, SlotDto::setVehicle);
        });

        return modelMapper;
    }

}
