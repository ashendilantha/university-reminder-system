package com.university.reminderapp.service;

import com.university.reminderapp.model.Bill;
import com.university.reminderapp.model.BillNotice;
import com.university.reminderapp.model.Event;
import com.university.reminderapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public boolean sendEmail(String to, String subject, String template, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                context.setVariable(entry.getKey(), entry.getValue());
            }

            String htmlContent = templateEngine.process(template, context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendEventNotification(User user, Event event, String actionType) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName());
        variables.put("eventTitle", event.getTitle());
        variables.put("eventDescription", event.getDescription());
        variables.put("eventVenue", event.getVenue());
        variables.put("eventStartsAt", event.getStartsAt());
        variables.put("eventEndsAt", event.getEndsAt());
        variables.put("actionType", actionType);

        String subject = actionType + ": " + event.getTitle();

        return sendEmail(user.getEmail(), subject, "emails/event-notification", variables);
    }

    public boolean sendBillNotification(User user, Bill bill, String actionType) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName());
        variables.put("studentName", bill.getStudent().getFullName());
        variables.put("billAmount", bill.getAmount());
        variables.put("billDescription", bill.getDescription());
        variables.put("billDueDate", bill.getDueDate());
        variables.put("billStatus", bill.getStatus());
        variables.put("actionType", actionType);

        String subject = actionType + ": Bill for " + bill.getStudent().getFullName();

        return sendEmail(user.getEmail(), subject, "emails/bill-notification", variables);
    }

    public boolean sendBillNoticeNotification(User user, BillNotice notice) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName());
        variables.put("noticeTitle", notice.getTitle());
        variables.put("noticeMessage", notice.getMessage());
        variables.put("validFrom", notice.getValidFrom());
        variables.put("validTo", notice.getValidTo());

        String subject = "Important: " + notice.getTitle();

        return sendEmail(user.getEmail(), subject, "emails/bill-notice-notification", variables);
    }

    public boolean sendReminderEmail(User user, String subject, String message) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName());
        variables.put("message", message);

        return sendEmail(user.getEmail(), subject, "emails/reminder", variables);
    }

    public boolean sendCustomEmail(User recipient, User sender, String subject, String message) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("recipientName", recipient.getFirstName());
        variables.put("senderName", sender.getFullName());
        variables.put("message", message);
        variables.put("subject", subject);

        return sendEmail(recipient.getEmail(), subject, "emails/custom-email", variables);
    }
}
