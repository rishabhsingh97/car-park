package com.rsuniverse.car_park.services;

import com.rsuniverse.car_park.exception.customExceptions.UserException;
import com.rsuniverse.car_park.models.dtos.UserDto;
import com.rsuniverse.car_park.models.entities.User;
import com.rsuniverse.car_park.models.enums.ErrorCode;
import com.rsuniverse.car_park.models.responses.PaginatedRes;
import com.rsuniverse.car_park.repo.UserRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.modelmapper.ModelMapper;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    private Map<Integer, User> userRepoMap;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        userRepoMap = UserRepo.getInstance().getUserMap();
//    }

//    @Test
//    void testGetAllUsers() {
//        // Setup
//        User user = User.builder().id(1).email("test@example.com").password("password").build();
//        userRepoMap.put(1, user);
//        when(modelMapper.map(any(User.class), eq(UserDto.class)))
//                .thenReturn(
//                        UserDto.builder()
//                                .email("test@example.com")
//                                .password("password")
//                                .build()
//                );
//
//        // Execute
//        PaginatedRes<UserDto> result = userService.getAllUsers(0, 10);
//
//        // Verify
//        assertEquals(1, result.getTotalCount());
//        assertEquals(1, result.getItems().size());
//        assertEquals("test@example.com", result.getItems().get(0).getEmail());
//    }
//
//    @Test
//    void testGetUserById_Success() {
//        // Setup
//        User user =User.builder()
//                .email("test@example.com")
//                .password("password")
//                .build();
//        userRepoMap.put(1, user);
//        when(modelMapper.map(any(User.class), eq(UserDto.class)))
//                .thenReturn(
//                        UserDto.builder()
//                                .email("test@example.com")
//                                .password("")
//                                .build()
//                );
//
//        // Execute
//        UserDto result = userService.getUserById(1);
//
//        // Verify
//        assertEquals("test@example.com", result.getEmail());
//    }

//    @Test
//    void testGetUserById_NotFound() {
//        // Execute & Verify
//        UserException exception = assertThrows(UserException.class, () -> userService.getUserById(1));
//        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
//    }

//    @Test
//    void testCreateUser_Success() {
//        // Setup
//        UserDto userDto = UserDto.builder()
//                .email("test@example.com")
//                .password("password")
//                .build();
//
//        User user = User.builder()
//                .id(1)
//                .email("newuser@example.com")
//                .password("encodedPassword")
//                .build();
//
//        when(modelMapper.map(any(UserDto.class), eq(User.class))).thenReturn(user);
//        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
//        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);
//        when(UserRepo.getInstance().addUser(any(User.class))).thenReturn(user);
//
//        // Execute
//        UserDto result = userService.createUser(userDto, authentication);
//
//        // Verify
//        assertEquals("newuser@example.com", result.getEmail());
//        verify(UserRepo.getInstance()).addUser(any(User.class));
//    }

//    @Test
//    void testCreateUser_AlreadyExists() {
//        // Setup
//        User existingUser = User.builder()
//                .id(1)
//                .email("existing@example.com")
//                .password("password")
//                .build();
//        userRepoMap.put(1, existingUser);
//
//        UserDto newUserDto = UserDto.builder()
//                .email("existing@example.com")
//                .password("password")
//                .build();
//
//        // Execute & Verify
//        UserException exception = assertThrows(UserException.class, () -> userService.createUser(newUserDto, authentication));
//        assertEquals(ErrorCode.USER_ALREADY_EXISTS, exception.getErrorCode());
//    }

//    @Test
//    void testUpdateUser_Success() {
//        // Setup
//        User existingUser = User.builder()
//                .id(1)
//                .email("existing@example.com")
//                .password("password").build();
//        userRepoMap.put(1, existingUser);
//
//        UserDto userDto = UserDto.builder()
//                .email("existing@example.com")
//                .password("password")
//                .build();
//
//        when(modelMapper.map(any(UserDto.class), eq(User.class))).thenReturn(existingUser);
//        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedNewPassword");
//        when(UserRepo.getInstance().updateUser(any(User.class))).thenReturn(existingUser);
//        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(userDto);
//
//        // Execute
//        UserDto result = userService.updateUser(1, userDto);
//
//        // Verify
//        assertEquals("updated@example.com", result.getEmail());
//    }
//
//    @Test
//    void testUpdateUser_NotFound() {
//        // Setup
//
//        UserDto userDto = UserDto.builder()
//                .email("existing@example.com")
//                .password("password")
//                .build();
//
//        // Execute & Verify
//        UserException exception = assertThrows(UserException.class, () -> userService.updateUser(1, userDto));
//        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
//    }
//
//    @Test
//    void testDeleteUser_Success() {
//        // Setup
//        User existingUser = User.builder()
//                .id(1)
//                .email("test@example.com")
//                .password("password")
//                .build();
//        userRepoMap.put(1, existingUser);
//
//        // Execute
//        userService.deleteUser(1);
//
//        // Verify
//        assertNull(userRepoMap.get(1));
//    }

//    @Test
//    void testDeleteUser_NotFound() {
//        // Execute & Verify
//        UserException exception = assertThrows(UserException.class, () -> userService.deleteUser(1));
//        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
//    }
}
