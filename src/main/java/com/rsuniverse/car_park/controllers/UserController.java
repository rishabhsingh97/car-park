package com.rsuniverse.car_park.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rsuniverse.car_park.models.dtos.UserDto;
import com.rsuniverse.car_park.models.enums.ErrorCode;
import com.rsuniverse.car_park.models.pojos.AuthUser;
import com.rsuniverse.car_park.models.responses.BaseRes;
import com.rsuniverse.car_park.models.responses.PaginatedRes;
import com.rsuniverse.car_park.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Staff Service", description = "Operations related to staff management")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<BaseRes<PaginatedRes<UserDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Incoming request to get all users");
        PaginatedRes<UserDto> paginatedRes = userService.getAllUsers(page, size);
        return BaseRes.success(paginatedRes, "Users - fetched successfully.");
    }

    @GetMapping("/auth-user")
    public ResponseEntity<BaseRes<UserDto>> getAuthenticatedUser(HttpServletRequest request, Authentication authentication) {
        log.info("Incoming request to get authenticated user: {}", authentication.getPrincipal());

        if (authentication.getPrincipal() != null) {
            AuthUser user = (AuthUser) authentication.getPrincipal();
            UserDto userReq = userService.getUserById(user.getId());
            return BaseRes.success(userReq, "Current user - fetched successfully.");
        } else {
            return BaseRes.error("Error fetching users: ", ErrorCode.UNKNOWN_ERROR_OCCURED, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseRes<UserDto>> getUserById(@PathVariable int id) {
        log.info("Incoming request to get user with id: {}", id);
        UserDto userReq = userService.getUserById(id);
        return BaseRes.success(userReq, String.format("User with ID %s - fetched successfully.", id));
    }

    @PostMapping("")
    public ResponseEntity<BaseRes<UserDto>> createUser(@RequestBody @Valid UserDto userReq, Authentication authentication) {
        log.info("Incoming request to create user: {}", userReq);
        UserDto createdUser = userService.createUser(userReq, authentication);
        return BaseRes.success(createdUser, "User - created successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseRes<UserDto>> updateUser(@PathVariable int id, @RequestBody @Valid UserDto userReq) {
        log.info("Incoming request to update user with id: {}, user: {}", id, userReq);
        UserDto updatedUser = userService.updateUser(id, userReq);
        return BaseRes.success(updatedUser, String.format("User with ID %s - updated successfully.", id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseRes<Void>> deleteUser(@PathVariable int id) {
        log.info("Incoming request to delete user with id: {}", id);
        userService.deleteUser(id);
        return BaseRes.success(null, String.format("User with ID %s - deleted successfully.", id));
    }
}
