package com.smarteventplatform.service;

import com.smarteventplatform.model.User;
import com.smarteventplatform.model.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceTest {
    @Test
    void registerUserShouldSaveUserAndAllowLogin() {
        UserService userService = new UserService();

        User registeredUser = userService.registerUser("Usama", "usama@test.com", "pass123", UserRole.ATTENDEE);
        User loggedInUser = userService.login("usama@test.com", "pass123");

        assertEquals("Usama", registeredUser.getName());
        assertEquals(registeredUser.getUserId(), loggedInUser.getUserId());
    }

    @Test
    void registerUserShouldRejectDuplicateEmail() {
        UserService userService = new UserService();
        userService.registerUser("Usama", "usama@test.com", "pass123", UserRole.ATTENDEE);

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser("Ali", "usama@test.com", "pass123", UserRole.ATTENDEE));
    }

    @Test
    void loginShouldRejectWrongPassword() {
        UserService userService = new UserService();
        userService.registerUser("Usama", "usama@test.com", "pass123", UserRole.ATTENDEE);

        assertThrows(IllegalArgumentException.class,
                () -> userService.login("usama@test.com", "pass455"));
    }
}
