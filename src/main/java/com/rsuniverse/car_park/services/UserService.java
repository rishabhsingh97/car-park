package com.rsuniverse.car_park.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rsuniverse.car_park.exception.customExceptions.UserException;
import com.rsuniverse.car_park.models.dtos.UserDto;
import com.rsuniverse.car_park.models.entities.User;
import com.rsuniverse.car_park.models.enums.ErrorCode;
import com.rsuniverse.car_park.models.enums.UserRole;
import com.rsuniverse.car_park.models.responses.PaginatedRes;
import com.rsuniverse.car_park.repo.UserRepo;
import com.rsuniverse.car_park.utils.CommonUtils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String ADMIN_EMAIL;

    @Value("${admin.password}")
    private String ADMIN_PASSWORD;

    @PostConstruct
    private void initDefaultAdmin() {
        Map<Integer, User> userRepo = UserRepo.getInstance().getUserMap();
        if (userRepo.isEmpty()) {
            User adminUser = User.builder()
                    .firstName("Admin")
                    .lastName("")
                    .email(ADMIN_EMAIL)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .roles(Set.of(UserRole.ADMIN))
                    .createdAt(CommonUtils.getFormattedDateTime())
                    .createdBy("system")
                    .build();

            UserRepo.getInstance().addUser(adminUser);
        }
    }

    public PaginatedRes<UserDto> getAllUsers(int page, int size) {
        log.info("Fetching all users - page: {}, size: {}", page, size);

        Map<Integer, User> userRepo = UserRepo.getInstance().getAllUsers();
        List<UserDto> users = userRepo.values().stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .peek(user-> user.setPassword(""))
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());

        int totalCount = userRepo.size();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        return PaginatedRes.<UserDto>builder()
                .items(users)
                .pageNum(page)
                .pageSize(size)
                .totalPages(totalPages)
                .totalCount(totalCount)
                .build();
    }

    public UserDto getUserById(int id) {
        log.info("Fetching user by id: {}", id);

        User user = UserRepo.getInstance().getUserById(id);
        
        if (user != null) {
            user.setPassword("");
            return modelMapper.map(user, UserDto.class);
        } else {
            log.warn("User with id {} not found", id);
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public UserDto createUser(UserDto userReq, Authentication authentication) {
        log.info("Creating user: {}", userReq);

        Map<Integer, User> userRepo = UserRepo.getInstance().getUserMap();
        if (userRepo.values().stream().anyMatch(user -> user.getEmail().equals(userReq.getEmail()))) {
            throw new UserException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = modelMapper.map(userReq, User.class);
        user.setPassword(passwordEncoder.encode(userReq.getPassword()));
        user.setCreatedAt(CommonUtils.getFormattedDateTime());
        user.setCreatedBy(CommonUtils.getCreatedBy(authentication));
        User savedUser = UserRepo.getInstance().addUser(user);
        savedUser.setPassword("");
        return modelMapper.map(savedUser, UserDto.class);
    }

    public UserDto updateUser(int id, UserDto userDto) {
        log.info("Updating user with id: {}", id);

        User existingUser = UserRepo.getInstance().getUserById(id);
        if (existingUser != null) {
            User user = modelMapper.map(userDto, User.class);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setId(existingUser.getId());
            user.setCreatedAt(existingUser.getCreatedAt());
            user.setCreatedBy(existingUser.getCreatedBy());
            User updatedUser = UserRepo.getInstance().updateUser(user); 
            updatedUser.setPassword("");
            log.info("User updated successfully: {}", updatedUser);
            return modelMapper.map(updatedUser, UserDto.class);
        } else {
            log.warn("User with id {} not found", id);
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void deleteUser(int id) {
        log.info("Deleting user with id: {}", id);

        if (UserRepo.getInstance().removeUser(id) != null) {
            log.info("User with id {} deleted successfully", id);
        } else {
            log.warn("User with id {} not found", id);
            throw new UserException(ErrorCode.USER_NOT_FOUND);
        }
    }
}
