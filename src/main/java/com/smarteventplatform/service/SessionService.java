package com.smarteventplatform.service;

import com.smarteventplatform.model.Event;
import com.smarteventplatform.model.Session;
import com.smarteventplatform.model.Venue;

import java.util.List;

public class SessionService {
    private int nextId = 1;

    public Session createSession(String title, String speaker, String timeSlot, Venue venue, int capacity) {
        String sessionId = String.format("S%03d", nextId++);
        return new Session(sessionId, title, speaker, timeSlot, venue, capacity);
    }

    public List<Session> searchBySpeaker(List<Event> events, String speakerKeyword) {
        if (events == null || speakerKeyword == null || speakerKeyword.isBlank()) {
            return List.of();
        }

        String normalizedKeyword = speakerKeyword.toLowerCase();
        return events.stream()
                .flatMap(event -> event.getSessions().stream())
                .filter(session -> session.getSpeaker().toLowerCase().contains(normalizedKeyword))
                .toList();
    }

    public List<Session> searchByTimeSlot(List<Event> events, String timeSlot) {
        if (events == null || timeSlot == null || timeSlot.isBlank()) {
            return List.of();
        }

        return events.stream()
                .flatMap(event -> event.getSessions().stream())
                .filter(session -> session.getTimeSlot().equalsIgnoreCase(timeSlot))
                .toList();
    }
}
