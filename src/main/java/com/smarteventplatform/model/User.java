package com.smarteventplatform.model;

import java.util.Objects;

public class User {
    private final int userId;
    private final String name;
    private final String email;
    private final String password;
    private final UserRole role;

    public User(int userId, String name, String email, String password, UserRole role) {
        validate(name, email, password, role);
        this.userId = userId;
        this.name = name.trim();
        this.email = email.trim().toLowerCase();
        this.password = password;
        this.role = role;
    }

    private static void validate(String name, String email, String password, UserRole role) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (isBlank(email)) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        if (role == null) {
            throw new IllegalArgumentException("User role is required.");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof User user)) {
            return false;
        }
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return String.format("[User #%d] %s | %s | Role: %s", userId, name, email, role);
    }
}
