package com.smarteventplatform.service;

import com.smarteventplatform.model.Event;
import com.smarteventplatform.model.Session;
import com.smarteventplatform.model.User;
import com.smarteventplatform.model.UserRole;
import com.smarteventplatform.model.Venue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionServiceTest {
    @Test
    void createSessionShouldGenerateSessionId() {
        SessionService sessionService = new SessionService();
        Venue venue = new Venue("V01", "Main Hall", 100);

        Session session = sessionService.createSession("Java Basics", "Sara", "10:00 AM", venue, 50);

        assertTrue(session.getSessionId().startsWith("S"));
    }

    @Test
    void eventShouldRejectSameVenueAndSameTimeSlotConflict() {
        UserService userService = new UserService();
        EventService eventService = new EventService();
        SessionService sessionService = new SessionService();
        User organizer = userService.registerUser("Organizer", "org@test.com", "pass123", UserRole.ORGANIZER);
        Event event = eventService.createEvent("Java Day", "2026-07-01", organizer);
        Venue venue = new Venue("V01", "Main Hall", 100);

        Session firstSession = sessionService.createSession("Java Basics", "Sara", "10:00 AM", venue, 50);
        Session secondSession = sessionService.createSession("Java Advanced", "Bilal", "10:00 AM", venue, 40);
        event.addSession(firstSession);

        assertThrows(IllegalStateException.class, () -> event.addSession(secondSession));
        assertEquals(1, event.getSessions().size());
    }

    @Test
    void sessionCapacityCannotExceedVenueCapacity() {
        SessionService sessionService = new SessionService();
        Venue venue = new Venue("V01", "Small Hall", 50);

        assertThrows(IllegalArgumentException.class,
                () -> sessionService.createSession("Big Talk", "Sara", "11:00 AM", venue, 100));
    }
}
