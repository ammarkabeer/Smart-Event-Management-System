package com.smarteventplatform.service;

import com.smarteventplatform.model.Event;
import com.smarteventplatform.model.PaymentStatus;
import com.smarteventplatform.model.Registration;
import com.smarteventplatform.model.TicketType;
import com.smarteventplatform.model.User;
import com.smarteventplatform.model.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegistrationServiceTest {
    @Test
    void successfulPaymentShouldMarkRegistrationPaidAndGenerateQrCode() {
        TestData testData = createTestData();

        Registration registration = testData.registrationService.register(testData.attendee, testData.event, TicketType.VIP);
        testData.registrationService.processPayment(registration, true);

        assertEquals(PaymentStatus.PAID, registration.getPaymentStatus());
        assertNotNull(registration.getQrCode());
    }

    @Test
    void failedPaymentShouldMarkRegistrationFailedAndNoQrCode() {
        TestData testData = createTestData();

        Registration registration = testData.registrationService.register(testData.attendee, testData.event, TicketType.STANDARD);
        testData.registrationService.processPayment(registration, false);

        assertEquals(PaymentStatus.FAILED, registration.getPaymentStatus());
        assertNull(registration.getQrCode());
    }

    @Test
    void attendeeCannotRegisterTwiceForSameEvent() {
        TestData testData = createTestData();
        testData.registrationService.register(testData.attendee, testData.event, TicketType.VIP);

        assertThrows(IllegalStateException.class,
                () -> testData.registrationService.register(testData.attendee, testData.event, TicketType.STANDARD));
    }

    private TestData createTestData() {
        UserService userService = new UserService();
        EventService eventService = new EventService();
        RegistrationService registrationService = new RegistrationService();

        User organizer = userService.registerUser("Organizer", "org@test.com", "pass123", UserRole.ORGANIZER);
        User attendee = userService.registerUser("Attendee", "att@test.com", "pass123", UserRole.ATTENDEE);
        Event event = eventService.createEvent("Cloud Day", "2026-08-01", organizer);

        return new TestData(registrationService, attendee, event);
    }

    private record TestData(RegistrationService registrationService, User attendee, Event event) {
    }
}
