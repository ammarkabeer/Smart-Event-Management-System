package com.smarteventplatform.service;

import com.smarteventplatform.model.Event;
import com.smarteventplatform.model.User;
import com.smarteventplatform.model.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EventServiceTest {
    @Test
    void organizerShouldCreateAndUpdateEvent() {
        UserService userService = new UserService();
        EventService eventService = new EventService();
        User organizer = userService.registerUser("Organizer", "org@test.com", "pass123", UserRole.ORGANIZER);

        Event event = eventService.createEvent("Tech Expo", "2026-06-01", organizer);
        eventService.updateEvent(event.getEventId(), "Tech Expo Updated", null, organizer);

        assertEquals("Tech Expo Updated", event.getTitle());
    }

    @Test
    void attendeeShouldNotCreateEvent() {
        UserService userService = new UserService();
        EventService eventService = new EventService();
        User attendee = userService.registerUser("Attendee", "att@test.com", "pass123", UserRole.ATTENDEE);

        assertThrows(IllegalArgumentException.class,
                () -> eventService.createEvent("Wrong Event", "2026-06-02", attendee));
    }

    @Test
    void nonOwnerShouldNotUpdateEvent() {
        UserService userService = new UserService();
        EventService eventService = new EventService();
        User owner = userService.registerUser("Owner", "owner@test.com", "pass123", UserRole.ORGANIZER);
        User otherOrganizer = userService.registerUser("Other", "other@test.com", "pass123", UserRole.ORGANIZER);
        Event event = eventService.createEvent("Private Event", "2026-06-01", owner);

        assertThrows(SecurityException.class,
                () -> eventService.updateEvent(event.getEventId(), "Hacked Title", null, otherOrganizer));
    }
}
