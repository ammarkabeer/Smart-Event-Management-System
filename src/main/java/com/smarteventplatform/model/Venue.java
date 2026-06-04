package com.smarteventplatform.model;

public class Venue {
    private final String venueId;
    private final String name;
    private final int capacity;

    public Venue(String venueId, String name, int capacity) {
        if (isBlank(venueId)) {
            throw new IllegalArgumentException("Venue ID cannot be empty.");
        }
        if (isBlank(name)) {
            throw new IllegalArgumentException("Venue name cannot be empty.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Venue capacity must be positive.");
        }

        this.venueId = venueId.trim();
        this.name = name.trim();
        this.capacity = capacity;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public String getVenueId() {
        return venueId;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return String.format("[Venue %s] %s | Capacity: %d", venueId, name, capacity);
    }
}
