package com.rsuniverse.car_park.repo;

import com.rsuniverse.car_park.models.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserRepoTest {

    private UserRepo userRepo;

//    @BeforeEach
//    void setUp() {
//        // Use getInstance() to ensure we're testing the singleton pattern.
//        userRepo = UserRepo.getInstance();
//
//        // Clear the userMap before each test to ensure no state persists between tests
//        userRepo.getUserMap().clear();
//    }
//
//    @Test
//    void testAddUser_Success() {
//        // Setup
//        User user = User.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .build();
//
//        // Execute
//        User addedUser = userRepo.addUser(user);
//
//        // Verify
//        assertNotNull(addedUser);
//        assertEquals(1, addedUser.getId()); // First user should have ID 1
//        assertEquals("John", addedUser.getFirstName());
//        assertEquals("Doe", addedUser.getLastName());
//        assertEquals("john.doe@example.com", addedUser.getEmail());
//
//        // Ensure user is in the repository
//        User repoUser = userRepo.getUserById(1);
//        assertNotNull(repoUser);
//        assertEquals("john.doe@example.com", repoUser.getEmail());
//    }

//    @Test
//    void testUpdateUser_Success() {
//        // Setup
//        User user = User.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .build();
//
//        User addedUser = userRepo.addUser(user);
//
//        // Modify user information
//        addedUser.setFirstName("Jane");
//        addedUser.setEmail("jane.doe@example.com");
//
//        // Execute
//        User updatedUser = userRepo.updateUser(addedUser);
//
//        // Verify
//        assertEquals("Jane", updatedUser.getFirstName());
//        assertEquals("jane.doe@example.com", updatedUser.getEmail());
//
//        // Ensure changes are reflected in the repository
//        User repoUser = userRepo.getUserById(updatedUser.getId());
//        assertNotNull(repoUser);
//        assertEquals("Jane", repoUser.getFirstName());
//        assertEquals("jane.doe@example.com", repoUser.getEmail());
//    }
//
//    @Test
//    void testGetUserById_Success() {
//        // Setup
//        User user = User.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .build();
//
//        userRepo.addUser(user);
//
//        // Execute
//        User foundUser = userRepo.getUserById(1);
//
//        // Verify
//        assertNotNull(foundUser);
//        assertEquals(1, foundUser.getId());
//        assertEquals("john.doe@example.com", foundUser.getEmail());
//    }
//
//    @Test
//    void testGetUserById_NotFound() {
//        // Execute
//        User foundUser = userRepo.getUserById(999); // Non-existent ID
//
//        // Verify
//        assertNull(foundUser);
//    }
//
//    @Test
//    void testGetUserByEmail_Success() {
//        // Setup
//        User user = User.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .build();
//
//        userRepo.addUser(user);
//
//        // Execute
//        User foundUser = userRepo.getUserByEmail("john.doe@example.com");
//
//        // Verify
//        assertNotNull(foundUser);
//        assertEquals("John", foundUser.getFirstName());
//    }
//
//    @Test
//    void testGetUserByEmail_NotFound() {
//        // Execute
//        User foundUser = userRepo.getUserByEmail("non.existent@example.com");
//
//        // Verify
//        assertNull(foundUser);
//    }
//
//    @Test
//    void testRemoveUser_Success() {
//        // Setup
//        User user = User.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .build();
//
//        userRepo.addUser(user);
//
//        // Execute
//        User removedUser = userRepo.removeUser(1);
//
//        // Verify
//        assertNotNull(removedUser);
//        assertEquals(1, removedUser.getId());
//
//        // Ensure the user no longer exists in the repository
//        assertNull(userRepo.getUserById(1));
//    }
//
//    @Test
//    void testRemoveUser_NotFound() {
//        // Execute
//        User removedUser = userRepo.removeUser(999); // Non-existent ID
//
//        // Verify
//        assertNull(removedUser);
//    }
//
//    @Test
//    void testGetAllUsers() {
//        // Setup
//        User user1 = User.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("john.doe@example.com")
//                .build();
//
//        User user2 = User.builder()
//                .firstName("Jane")
//                .lastName("Smith")
//                .email("jane.smith@example.com")
//                .build();
//
//        userRepo.addUser(user1);
//        userRepo.addUser(user2);
//
//        // Execute
//        Map<Integer, User> allUsers = userRepo.getAllUsers();
//
//        // Verify
//        assertEquals(2, allUsers.size());
//        assertEquals("john.doe@example.com", allUsers.get(1).getEmail());
//        assertEquals("jane.smith@example.com", allUsers.get(2).getEmail());
//    }
}