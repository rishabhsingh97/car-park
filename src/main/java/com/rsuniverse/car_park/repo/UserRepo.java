package com.rsuniverse.car_park.repo;

import com.rsuniverse.car_park.models.entities.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepo {
    private static final UserRepo instance = new UserRepo();
    private final Map<Integer, User> userMap = new HashMap<>();
    
    private int userId = 1;

    private UserRepo() {}

    public static UserRepo getInstance() {
        return instance;
    }

    public Map<Integer, User> getUserMap() {
        return userMap;
    }

    public User addUser(User user) {
        user.setId(this.userId); 
        userMap.put(userId, user);
        userId++; 
        return user;
    }

    public User updateUser(User user) {
        userMap.put(user.getId(), user); 
        return user;
    }

    public User getUserById(int id) {
        return userMap.get(id);
    }

    public User getUserByEmail(String email) {
        return userMap.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null); 
    }

    public User removeUser(int id) {
        return userMap.remove(id);
    }

    public Map<Integer, User> getAllUsers() {
        return new HashMap<>(userMap);
    }
}
