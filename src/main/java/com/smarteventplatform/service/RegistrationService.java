package com.smarteventplatform.service;

import com.smarteventplatform.model.Event;
import com.smarteventplatform.model.Registration;
import com.smarteventplatform.model.TicketType;
import com.smarteventplatform.model.User;

import java.util.ArrayList;
import java.util.List;

public class RegistrationService {
    private final List<Registration> registrations = new ArrayList<>();
    private int nextId = 1;

    public Registration register(User attendee, Event event, TicketType ticketType) {
        if (attendee == null) {
            throw new IllegalArgumentException("Attendee is required.");
        }
        if (event == null) {
            throw new IllegalArgumentException("Event is required.");
        }
        if (isAlreadyRegistered(attendee, event)) {
            throw new IllegalStateException("Attendee already registered for this event.");
        }

        String registrationId = String.format("R%03d", nextId++);
        Registration registration = new Registration(registrationId, attendee, event, ticketType);
        registrations.add(registration);
        return registration;
    }

    public void processPayment(Registration registration, boolean paymentSuccess) {
        if (registration == null) {
            throw new IllegalArgumentException("Registration is required.");
        }

        if (paymentSuccess) {
            registration.confirmPayment();
            return;
        }
        registration.failPayment();
    }

    public List<Registration> getRegistrationsByEvent(String eventId) {
        return registrations.stream()
                .filter(registration -> registration.getEvent().getEventId().equalsIgnoreCase(eventId))
                .toList();
    }

    public List<Registration> getAllRegistrations() {
        return new ArrayList<>(registrations);
    }

    public Registration findById(String registrationId) {
        return registrations.stream()
                .filter(registration -> registration.getRegistrationId().equalsIgnoreCase(registrationId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Registration not found: " + registrationId));
    }

    private boolean isAlreadyRegistered(User attendee, Event event) {
        return registrations.stream().anyMatch(registration ->
                registration.getAttendee().equals(attendee)
                        && registration.getEvent().getEventId().equalsIgnoreCase(event.getEventId())
        );
    }
}
