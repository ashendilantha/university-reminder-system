package com.university.reminderapp.service;

import com.university.reminderapp.model.*;
import com.university.reminderapp.repository.DeliveryLogRepository;
import com.university.reminderapp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DeliveryLogRepository deliveryLogRepository;

    @Autowired
    private EmailService emailService;

    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Notification> getUnreadNotificationsForUser(User user) {
        return notificationRepository.findByUserAndReadAtIsNullOrderByCreatedAtDesc(user);
    }

    public int getUnreadNotificationCount(User user) {
        return notificationRepository.countByUserAndReadAtIsNull(user);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    @Transactional
    public void deleteNotification(Long id, User currentUser) {
        Notification notification = getNotificationById(id);

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to delete this notification");
        }

        deliveryLogRepository.deleteByNotification(notification);
        notificationRepository.delete(notification);
    }

    @Transactional
    public Notification markAsRead(Long id, User currentUser) {
        Notification notification = getNotificationById(id);

        if (!notification.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to read this notification");
        }

        notification.setReadAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public List<DeliveryLog> getDeliveryLogsForNotification(Long notificationId) {
        Notification notification = getNotificationById(notificationId);
        return deliveryLogRepository.findByNotification(notification);
    }

    public void sendEventNotification(User user, Event event, String actionMessage) {
        // Create in-app notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setUniversity(event.getUniversity());
        notification.setType("EVENT");
        notification.setTitle(actionMessage);
        notification.setBody("Event: " + event.getTitle() + " at " + event.getVenue() +
                " starting on " + event.getStartsAt());

        Notification savedNotification = notificationRepository.save(notification);

        // Log in-app delivery
        DeliveryLog inAppLog = new DeliveryLog();
        inAppLog.setNotification(savedNotification);
        inAppLog.setChannel("IN_APP");
        inAppLog.setStatus("SENT");
        inAppLog.setSentAt(LocalDateTime.now());
        deliveryLogRepository.save(inAppLog);

        // Send email and log result
        boolean emailSent = emailService.sendEventNotification(user, event, actionMessage);

        DeliveryLog emailLog = new DeliveryLog();
        emailLog.setNotification(savedNotification);
        emailLog.setChannel("EMAIL");

        if (emailSent) {
            emailLog.setStatus("SENT");
            emailLog.setSentAt(LocalDateTime.now());
        } else {
            emailLog.setStatus("FAILED");
            emailLog.setError("Failed to send email");
        }

        deliveryLogRepository.save(emailLog);
    }

    public void sendBillNotification(User user, Bill bill, String actionMessage) {
        // Create in-app notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setUniversity(bill.getUniversity());
        notification.setType("BILL");
        notification.setTitle(actionMessage);
        notification.setBody("Bill amount: " + bill.getAmount() + " due on " + bill.getDueDate() +
                " - " + bill.getDescription());

        Notification savedNotification = notificationRepository.save(notification);

        // Log in-app delivery
        DeliveryLog inAppLog = new DeliveryLog();
        inAppLog.setNotification(savedNotification);
        inAppLog.setChannel("IN_APP");
        inAppLog.setStatus("SENT");
        inAppLog.setSentAt(LocalDateTime.now());
        deliveryLogRepository.save(inAppLog);

        // Send email and log result
        boolean emailSent = emailService.sendBillNotification(user, bill, actionMessage);

        DeliveryLog emailLog = new DeliveryLog();
        emailLog.setNotification(savedNotification);
        emailLog.setChannel("EMAIL");

        if (emailSent) {
            emailLog.setStatus("SENT");
            emailLog.setSentAt(LocalDateTime.now());
        } else {
            emailLog.setStatus("FAILED");
            emailLog.setError("Failed to send email");
        }

        deliveryLogRepository.save(emailLog);
    }

    public void sendBillNoticeNotification(User user, BillNotice notice) {
        // Create in-app notification
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setUniversity(notice.getUniversity());
        notification.setType("BILL");
        notification.setTitle("Important Bill Notice: " + notice.getTitle());
        notification.setBody(notice.getMessage() + " (Valid until: " + notice.getValidTo() + ")");

        Notification savedNotification = notificationRepository.save(notification);

        // Log in-app delivery
        DeliveryLog inAppLog = new DeliveryLog();
        inAppLog.setNotification(savedNotification);
        inAppLog.setChannel("IN_APP");
        inAppLog.setStatus("SENT");
        inAppLog.setSentAt(LocalDateTime.now());
        deliveryLogRepository.save(inAppLog);

        // Send email and log result
        boolean emailSent = emailService.sendBillNoticeNotification(user, notice);

        DeliveryLog emailLog = new DeliveryLog();
        emailLog.setNotification(savedNotification);
        emailLog.setChannel("EMAIL");

        if (emailSent) {
            emailLog.setStatus("SENT");
            emailLog.setSentAt(LocalDateTime.now());
        } else {
            emailLog.setStatus("FAILED");
            emailLog.setError("Failed to send email");
        }

        deliveryLogRepository.save(emailLog);
    }

    public void sendSystemNotification(User user, String title, String message) {
        // Create in-app notification
        Notification notification = new Notification();
        notification.setUser(user);
        if (user.getUniversity() != null) {
            notification.setUniversity(user.getUniversity());
        }
        notification.setType("SYSTEM");
        notification.setTitle(title);
        notification.setBody(message);

        notificationRepository.save(notification);
    }
}
