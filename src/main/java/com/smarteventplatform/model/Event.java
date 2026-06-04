package com.smarteventplatform.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Event {
    private final String eventId;
    private String title;
    private String date;
    private final User organizer;
    private final List<Session> sessions = new ArrayList<>();

    public Event(String eventId, String title, String date, User organizer) {
        validate(eventId, title, date, organizer);
        this.eventId = eventId.trim();
        this.title = title.trim();
        this.date = date.trim();
        this.organizer = organizer;
    }

    private static void validate(String eventId, String title, String date, User organizer) {
        if (isBlank(eventId)) {
            throw new IllegalArgumentException("Event ID cannot be empty.");
        }
        if (isBlank(title)) {
            throw new IllegalArgumentException("Event title cannot be empty.");
        }
        if (isBlank(date)) {
            throw new IllegalArgumentException("Event date cannot be empty.");
        }
        if (organizer == null) {
            throw new IllegalArgumentException("Organizer is required.");
        }
        if (organizer.getRole() != UserRole.ORGANIZER && organizer.getRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Only an Organizer or Admin can create events.");
        }
    }

    public void addSession(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session is required.");
        }
        if (hasScheduleConflict(session)) {
            throw new IllegalStateException(
                    "Scheduling conflict: Venue \"" + session.getVenue().getName()
                            + "\" is already booked at " + session.getTimeSlot()
            );
        }
        sessions.add(session);
    }

    private boolean hasScheduleConflict(Session newSession) {
        return sessions.stream().anyMatch(existingSession ->
                existingSession.getVenue().getVenueId().equalsIgnoreCase(newSession.getVenue().getVenueId())
                        && existingSession.getTimeSlot().equalsIgnoreCase(newSession.getTimeSlot())
        );
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public User getOrganizer() {
        return organizer;
    }

    public List<Session> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

    public void setTitle(String title) {
        if (!isBlank(title)) {
            this.title = title.trim();
        }
    }

    public void setDate(String date) {
        if (!isBlank(date)) {
            this.date = date.trim();
        }
    }

    @Override
    public String toString() {
        return String.format(
                "[Event %s] \"%s\" on %s | Organizer: %s | Sessions: %d",
                eventId,
                title,
                date,
                organizer.getName(),
                sessions.size()
        );
    }
}
