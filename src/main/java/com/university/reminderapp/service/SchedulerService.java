package com.university.reminderapp.service;

import com.university.reminderapp.model.Bill;
import com.university.reminderapp.model.Event;
import com.university.reminderapp.model.User;
import com.university.reminderapp.repository.BillRepository;
import com.university.reminderapp.repository.EventRepository;
import com.university.reminderapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SchedulerService {
    @Autowired
    private BillRepository billRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    // Run daily at 8:00 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendDailyReminders() {
        sendBillReminders();
        sendEventReminders();
    }

    // Run hourly for imminent events (starting in the next hour)
    @Scheduled(cron = "0 0 * * * ?")
    public void sendImminentEventReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);

        // Find all events starting in the next hour
        List<Event> imminentEvents = eventRepository.findAll().stream()
                .filter(event -> event.getStartsAt().isAfter(now) &&
                        event.getStartsAt().isBefore(oneHourLater) &&
                        event.getStatus().equals("SCHEDULED"))
                .toList();

        for (Event event : imminentEvents) {
            // Find all students in this university
            List<User> students = userRepository.findByUniversityAndRole(event.getUniversity(), "STUDENT");

            for (User student : students) {
                String subject = "Reminder: Event starting soon";
                String message = "The event '" + event.getTitle() + "' at " + event.getVenue() +
                        " is starting in less than an hour at " + event.getStartsAt();

                emailService.sendReminderEmail(student, subject, message);
                notificationService.sendSystemNotification(student, subject, message);
            }
        }
    }

    private void sendBillReminders() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate nextWeek = today.plusWeeks(1);

        // Find all bills due tomorrow or in the next week
        List<Bill> upcomingBills = billRepository.findAll().stream()
                .filter(bill -> (bill.getDueDate().equals(tomorrow) || bill.getDueDate().equals(nextWeek)) &&
                        bill.getStatus().equals("PENDING"))
                .toList();

        for (Bill bill : upcomingBills) {
            User student = bill.getStudent();

            String subject;
            String message;

            if (bill.getDueDate().equals(tomorrow)) {
                subject = "Urgent: Bill due tomorrow";
                message = "Your bill of amount " + bill.getAmount() + " for " + bill.getDescription() +
                        " is due tomorrow.";
            } else {
                subject = "Reminder: Bill due in one week";
                message = "Your bill of amount " + bill.getAmount() + " for " + bill.getDescription() +
                        " is due in one week on " + bill.getDueDate();
            }

            emailService.sendReminderEmail(student, subject, message);
            notificationService.sendSystemNotification(student, subject, message);

            // Also notify parent if exists
            if (bill.getParent() != null) {
                emailService.sendReminderEmail(bill.getParent(), subject,
                        "Student " + student.getFullName() + ": " + message);
                notificationService.sendSystemNotification(bill.getParent(), subject,
                        "Student " + student.getFullName() + ": " + message);
            }
        }
    }

    private void sendEventReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);

        // Find all events happening tomorrow
        List<Event> tomorrowEvents = eventRepository.findAll().stream()
                .filter(event -> event.getStartsAt().isAfter(now) &&
                        event.getStartsAt().isBefore(tomorrow) &&
                        event.getStatus().equals("SCHEDULED"))
                .toList();

        for (Event event : tomorrowEvents) {
            // Find all students in this university
            List<User> students = userRepository.findByUniversityAndRole(event.getUniversity(), "STUDENT");

            for (User student : students) {
                String subject = "Reminder: Event tomorrow";
                String message = "The event '" + event.getTitle() + "' at " + event.getVenue() +
                        " is scheduled for tomorrow at " + event.getStartsAt();

                emailService.sendReminderEmail(student, subject, message);
                notificationService.sendSystemNotification(student, subject, message);
            }
        }
    }
}