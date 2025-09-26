package com.university.reminderapp.service;

import com.university.reminderapp.dto.request.EmailHistoryRequest;
import com.university.reminderapp.exception.AccessDeniedException;
import com.university.reminderapp.exception.ResourceNotFoundException;
import com.university.reminderapp.model.EmailHistory;
import com.university.reminderapp.model.University;
import com.university.reminderapp.model.User;
import com.university.reminderapp.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailHistoryService {

    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UniversityService universityService;

    @Autowired
    private EmailService emailService;

    public List<EmailHistory> getEmailHistoryForUniversity(Long universityId) {
        return emailHistoryRepository.findByUniversityIdOrderBySentAtDesc(universityId);
    }

    public EmailHistory getEmailHistoryById(Long id) {
        return emailHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Email history not found with id: " + id));
    }

    @Transactional
    public EmailHistory createEmail(EmailHistoryRequest request, User sender) {
        User recipient = userService.getUserById(request.getRecipientId());
        University university = sender.getUniversity();

        if (recipient.getUniversity() == null || !recipient.getUniversity().getId().equals(university.getId())) {
            throw new AccessDeniedException("Recipient must belong to your university");
        }

        EmailHistory emailHistory = new EmailHistory();
        emailHistory.setUniversity(university);
        emailHistory.setSender(sender);
        emailHistory.setRecipient(recipient);
        emailHistory.setSubject(request.getSubject());
        emailHistory.setBody(request.getBody());
        emailHistory.setSentAt(LocalDateTime.now());

        boolean sent = emailService.sendCustomEmail(recipient, sender, request.getSubject(), request.getBody());
        if (!sent) {
            throw new RuntimeException("Failed to send email to recipient");
        }

        return emailHistoryRepository.save(emailHistory);
    }

    @Transactional
    public EmailHistory updateEmail(Long id, EmailHistoryRequest request, User sender) {
        EmailHistory emailHistory = getEmailHistoryById(id);

        if (!emailHistory.getUniversity().getId().equals(sender.getUniversity().getId())) {
            throw new ResourceNotFoundException("Email history not found");
        }

        User recipient = userService.getUserById(request.getRecipientId());

        if (recipient.getUniversity() == null || !recipient.getUniversity().getId().equals(emailHistory.getUniversity().getId())) {
            throw new AccessDeniedException("Recipient must belong to your university");
        }

        emailHistory.setRecipient(recipient);
        emailHistory.setSubject(request.getSubject());
        emailHistory.setBody(request.getBody());
        emailHistory.setSentAt(LocalDateTime.now());
        emailHistory.setUpdatedAt(LocalDateTime.now());

        boolean sent = emailService.sendCustomEmail(recipient, sender, request.getSubject(), request.getBody());
        if (!sent) {
            throw new RuntimeException("Failed to resend email");
        }

        return emailHistoryRepository.save(emailHistory);
    }

    @Transactional
    public void deleteEmail(Long id, User sender) {
        EmailHistory emailHistory = getEmailHistoryById(id);

        if (!emailHistory.getUniversity().getId().equals(sender.getUniversity().getId())) {
            throw new ResourceNotFoundException("Email history not found");
        }

        emailHistoryRepository.delete(emailHistory);
    }
}
