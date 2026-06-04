package com.smarteventplatform.service;

import com.smarteventplatform.model.User;
import com.smarteventplatform.model.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final List<User> users = new ArrayList<>();
    private int nextId = 1;

    public User registerUser(String name, String email, String password, UserRole role) {
        if (isEmailRegistered(email)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        User user = new User(nextId++, name, email, password, role);
        users.add(user);
        return user;
    }

    public User login(String email, String password) {
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User findById(int userId) {
        return users.stream()
                .filter(user -> user.getUserId() == userId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    private boolean isEmailRegistered(String email) {
        if (email == null) {
            return false;
        }
        Optional<User> existingUser = users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
        return existingUser.isPresent();
    }
}
