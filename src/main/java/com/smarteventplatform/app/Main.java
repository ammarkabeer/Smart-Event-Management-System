package com.smarteventplatform.app;

import com.smarteventplatform.model.Event;
import com.smarteventplatform.model.PaymentStatus;
import com.smarteventplatform.model.Registration;
import com.smarteventplatform.model.Session;
import com.smarteventplatform.model.TicketType;
import com.smarteventplatform.model.User;
import com.smarteventplatform.model.UserRole;
import com.smarteventplatform.model.Venue;
import com.smarteventplatform.service.EventService;
import com.smarteventplatform.service.RegistrationService;
import com.smarteventplatform.service.SessionService;
import com.smarteventplatform.service.UserService;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        EventService eventService = new EventService();
        SessionService sessionService = new SessionService();
        RegistrationService registrationService = new RegistrationService();

        System.out.println("Welcome to our  \n");
        System.out.println("Smart Event Management & Networking Platform\n\n");

        try {
            User admin = userService.registerUser("Admin khan", "admin@events.com", "admin123", UserRole.ADMIN);
            User organizer = userService.registerUser("Ammar Kabeer", "ammar@events.com", "pass1234", UserRole.ORGANIZER);
            User attendeeOne = userService.registerUser("Usama", "usama@gmail.com", "pass1234", UserRole.ATTENDEE);
            User attendeeTwo = userService.registerUser("Ammar", "ammar@gmail.com", "pass1234", UserRole.ATTENDEE);

            printSection("Registered Users");
            userService.getAllUsers().forEach(System.out::println);

            User loggedInUser = userService.login("ammar@events.com", "pass1234");
            System.out.println("\nLogged in as: " + loggedInUser.getName());

            Venue venue = new Venue("V01", "Ghazkhan Hall", 100);
            Event event = eventService.createEvent("Tech Conference 2026", "2026-05-10", organizer);

            Session firstSession = sessionService.createSession("AI Workshop", "Dr. Sara", "10:00 AM", venue, 50);
            Session secondSession = sessionService.createSession("Cloud Computing", "Mr. Bilal", "02:00 PM", venue, 40);

            event.addSession(firstSession);
            event.addSession(secondSession);

            printSection("Event Details");
            System.out.println(event);
            event.getSessions().forEach(System.out::println);

            Registration firstRegistration = registrationService.register(attendeeOne, event, TicketType.VIP);
            Registration secondRegistration = registrationService.register(attendeeTwo, event, TicketType.STANDARD);

            registrationService.processPayment(firstRegistration, true);
            registrationService.processPayment(secondRegistration, false);

            printSection("Registrations");
            registrationService.getAllRegistrations().forEach(System.out::println);

            eventService.updateEvent(event.getEventId(), "Tech Conference 2026 Updated", null, admin);

            printSection("Updated Event");
            System.out.println(eventService.findById(event.getEventId()));

            printSection("Payment Summary");
            System.out.println("Paid registrations: " + countPaidRegistrations(registrationService));

        } catch (RuntimeException exception) {
            System.err.println("Error: " + exception.getMessage());

        } finally {
            printSection("Application Status");
            System.out.println("Application execution completed.");
            System.out.println("Thank you for using Smart Event Management & Networking Platform.");
        }
    }

    private static long countPaidRegistrations(RegistrationService registrationService) {
        return registrationService.getAllRegistrations().stream()
                .filter(registration -> registration.getPaymentStatus() == PaymentStatus.PAID)
                .count();
    }

    private static void printSection(String title) {
        System.out.println("\n--- " + title + " ---");
    }
}