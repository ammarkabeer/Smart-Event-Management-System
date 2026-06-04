package com.smarteventplatform.service;

import com.smarteventplatform.model.Event;
import com.smarteventplatform.model.User;
import com.smarteventplatform.model.UserRole;

import java.util.ArrayList;
import java.util.List;

public class EventService {
    private final List<Event> events = new ArrayList<>();
    private int nextId = 1;

    public Event createEvent(String title, String date, User organizer) {
        String eventId = String.format("E%03d", nextId++);
        Event event = new Event(eventId, title, date, organizer);
        events.add(event);
        return event;
    }

    public void updateEvent(String eventId, String newTitle, String newDate, User requester) {
        Event event = findById(eventId);
        checkEventPermission(event, requester);
        event.setTitle(newTitle);
        event.setDate(newDate);
    }

    public void deleteEvent(String eventId, User requester) {
        Event event = findById(eventId);
        checkEventPermission(event, requester);
        events.remove(event);
    }

    public List<Event> getAllEvents() {
        return new ArrayList<>(events);
    }

    public Event findById(String eventId) {
        return events.stream()
                .filter(event -> event.getEventId().equalsIgnoreCase(eventId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
    }

    public List<Event> searchByTitle(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        String normalizedKeyword = keyword.toLowerCase();
        return events.stream()
                .filter(event -> event.getTitle().toLowerCase().contains(normalizedKeyword))
                .toList();
    }

    private void checkEventPermission(Event event, User requester) {
        if (requester == null) {
            throw new SecurityException("Permission denied.");
        }

        boolean isOwner = event.getOrganizer().equals(requester);
        boolean isAdmin = requester.getRole() == UserRole.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new SecurityException("Permission denied: not the event organizer.");
        }
    }
}
