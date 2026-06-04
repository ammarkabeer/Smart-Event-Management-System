package com.smarteventplatform.model;

public class Registration {
    private final String registrationId;
    private final User attendee;
    private final Event event;
    private final TicketType ticketType;
    private PaymentStatus paymentStatus;
    private String qrCode;

    public Registration(String registrationId, User attendee, Event event, TicketType ticketType) {
        validate(registrationId, attendee, event, ticketType);
        this.registrationId = registrationId.trim();
        this.attendee = attendee;
        this.event = event;
        this.ticketType = ticketType;
        this.paymentStatus = PaymentStatus.PENDING;
    }
    private static void validate(String registrationId, User attendee, Event event, TicketType ticketType) {
        if (isBlank(registrationId)) {
            throw new IllegalArgumentException("Registration ID cannot be empty.");
        }
        if (attendee == null) {
            throw new IllegalArgumentException("Attendee is required.");
        }
        if (event == null) {
            throw new IllegalArgumentException("Event is required.");
        }
        if (ticketType == null) {
            throw new IllegalArgumentException("Ticket type is required.");
        }
        if (attendee.getRole() != UserRole.ATTENDEE) {
            throw new IllegalArgumentException("Only Attendees can register for events.");
        }
    }

    public void confirmPayment() {
        paymentStatus = PaymentStatus.PAID;
        qrCode = generateQrCode();
    }

    public void failPayment() {
        paymentStatus = PaymentStatus.FAILED;
        qrCode = null;
    }

    private String generateQrCode() {
        return "QR-" + registrationId + "-" + event.getEventId();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public User getAttendee() {
        return attendee;
    }

    public Event getEvent() {
        return event;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getQrCode() {
        return qrCode;
    }

    @Override
    public String toString() {
        String displayQrCode = qrCode == null ? "N/A" : qrCode;
        return String.format(
                "[Reg %s] %s -> \"%s\" | Ticket: %s | Status: %s | QR: %s",
                registrationId,
                attendee.getName(),
                event.getTitle(),
                ticketType,
                paymentStatus,
                displayQrCode
        );
    }
}
