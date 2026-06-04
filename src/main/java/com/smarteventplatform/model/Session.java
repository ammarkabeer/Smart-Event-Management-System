package com.smarteventplatform.model;

public class Session {
    private final String sessionId;
    private final String title;
    private final String speaker;
    private final String timeSlot;
    private final Venue venue;
    private final int capacity;

    public Session(String sessionId, String title, String speaker, String timeSlot, Venue venue, int capacity) {
        validate(sessionId, title, speaker, timeSlot, venue, capacity);
        this.sessionId = sessionId.trim();
        this.title = title.trim();
        this.speaker = speaker.trim();
        this.timeSlot = timeSlot.trim();
        this.venue = venue;
        this.capacity = capacity;
    }

    private static void validate(String sessionId, String title, String speaker, String timeSlot, Venue venue, int capacity) {
        if (isBlank(sessionId)) {
            throw new IllegalArgumentException("Session ID cannot be empty.");
        }
        if (isBlank(title)) {
            throw new IllegalArgumentException("Session title cannot be empty.");
        }
        if (isBlank(speaker)) {
            throw new IllegalArgumentException("Speaker name cannot be empty.");
        }
        if (isBlank(timeSlot)) {
            throw new IllegalArgumentException("Time slot cannot be empty.");
        }
        if (venue == null) {
            throw new IllegalArgumentException("Venue is required.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Session capacity must be positive.");
        }
        if (capacity > venue.getCapacity()) {
            throw new IllegalArgumentException("Session capacity cannot exceed venue capacity.");
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getTitle() {
        return title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public Venue getVenue() {
        return venue;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return String.format(
                "[Session %s] \"%s\" by %s @ %s | Venue: %s | Capacity: %d",
                sessionId,
                title,
                speaker,
                timeSlot,
                venue.getName(),
                capacity
        );
    }
}
