package com.university.reminderapp.controller.web;

import com.university.reminderapp.dto.request.UniversityAdminProfileRequest;
import com.university.reminderapp.dto.request.UserRequest;
import com.university.reminderapp.dto.request.EmailHistoryRequest;
import com.university.reminderapp.model.University;
import com.university.reminderapp.model.User;
import com.university.reminderapp.model.EmailHistory;
import com.university.reminderapp.service.NotificationService;
import com.university.reminderapp.service.UserService;
import com.university.reminderapp.service.EmailHistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/university")
@PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
public class UniversityAdminWebController {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailHistoryService emailHistoryService;

    @GetMapping("/users")
    public String listUsers(@RequestParam(required = false) String role,
                            @RequestParam(required = false) String status,
                            Model model) {
        User currentUser = userService.getCurrentUser();
        University university = currentUser.getUniversity();
        List<User> users = userService.getUsersByUniversity(university.getId());

        // Filtering logic
        if (role != null && !role.isEmpty()) {
            users = users.stream().filter(u -> role.equals(u.getRole())).collect(Collectors.toList());
        }
        if (status != null && !status.isEmpty()) {
            users = users.stream().filter(u -> status.equals(u.getStatus())).collect(Collectors.toList());
        }

        model.addAttribute("users", users);
        model.addAttribute("university", university);
        return "admin/university/university-user-list";
    }

    @GetMapping("/users/create")
    public String showCreateUserForm(Model model) {
        User currentUser = userService.getCurrentUser();
        University university = currentUser.getUniversity();

        UserRequest userRequest = new UserRequest();
        userRequest.setUniversityId(university.getId());

        model.addAttribute("user", userRequest);
        model.addAttribute("university", university);
        model.addAttribute("userId", null);
        // Provide a list of parents for the dropdown
        model.addAttribute("parents", userService.getUsersByUniversityAndRole(university.getId(), "PARENT"));

        // FIX: Return the correct template path based on your file structure
        return "admin/university/university-user-form";
    }

    @PostMapping("/users/create")
    public String createUser(@Valid @ModelAttribute("user") UserRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        User currentUser = userService.getCurrentUser();
        University university = currentUser.getUniversity();

        if (result.hasErrors()) {
            model.addAttribute("university", university);
            model.addAttribute("userId", null);
            model.addAttribute("parents", userService.getUsersByUniversityAndRole(university.getId(), "PARENT"));
            // FIX: Return the correct template path on error
            return "admin/university/university-user-form";
        }

        userService.createUser(request, currentUser);
        redirectAttributes.addFlashAttribute("success", "User created successfully!");
        return "redirect:/admin/university/users";
    }

    @GetMapping("/users/{id}/edit")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User currentUser = userService.getCurrentUser();
        University university = currentUser.getUniversity();
        User userToEdit = userService.getUserById(id);

        UserRequest userRequest = new UserRequest();
        userRequest.setUniversityId(userToEdit.getUniversity().getId());
        userRequest.setEmail(userToEdit.getEmail());
        userRequest.setFirstName(userToEdit.getFirstName());
        userRequest.setLastName(userToEdit.getLastName());
        userRequest.setRole(userToEdit.getRole());
        userRequest.setParentId(userToEdit.getParent() != null ? userToEdit.getParent().getId() : null);
        userRequest.setStatus(userToEdit.getStatus());

        model.addAttribute("user", userRequest);
        model.addAttribute("university", university);
        model.addAttribute("userId", id);
        model.addAttribute("parents", userService.getUsersByUniversityAndRole(university.getId(), "PARENT"));

        // FIX: Return the correct template path
        return "admin/university/university-user-form";
    }

    @PostMapping("/users/{id}/edit")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("user") UserRequest request,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        User currentUser = userService.getCurrentUser();
        University university = currentUser.getUniversity();

        if (result.hasErrors()) {
            model.addAttribute("university", university);
            model.addAttribute("userId", id);
            model.addAttribute("parents", userService.getUsersByUniversityAndRole(university.getId(), "PARENT"));
            // FIX: Return the correct template path on error
            return "admin/university/university-user-form";
        }

        userService.updateUser(id, request, currentUser);
        redirectAttributes.addFlashAttribute("success", "User updated successfully!");
        return "redirect:/admin/university/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        userService.deleteUser(id, currentUser);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        return "redirect:/admin/university/users";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        User currentUser = userService.getCurrentUser();

        UniversityAdminProfileRequest request = new UniversityAdminProfileRequest();
        request.setEmail(currentUser.getEmail());
        request.setFirstName(currentUser.getFirstName());
        request.setLastName(currentUser.getLastName());

        model.addAttribute("admin", request);
        model.addAttribute("university", currentUser.getUniversity());
        model.addAttribute("activeTab", "university-profile");
        return "admin/university/admin-profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("admin") UniversityAdminProfileRequest request,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        User currentUser = userService.getCurrentUser();

        if (result.hasErrors()) {
            model.addAttribute("university", currentUser.getUniversity());
            return "admin/university/admin-profile";
        }

        userService.updateUniversityAdminProfile(currentUser, request);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/admin/university/profile";
    }

    @GetMapping("/notifications")
    public String viewNotifications(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("notifications", notificationService.getNotificationsForUser(currentUser));
        model.addAttribute("university", currentUser.getUniversity());
        model.addAttribute("activeTab", "university-notifications");
        return "admin/university/university-notifications";
    }

    @PostMapping("/notifications/{id}/delete")
    public String deleteNotification(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        notificationService.deleteNotification(id, currentUser);
        redirectAttributes.addFlashAttribute("success", "Notification deleted successfully!");
        return "redirect:/admin/university/notifications";
    }

    @GetMapping("/emails/create")
    public String showCreateEmailForm(Model model) {
        User currentUser = userService.getCurrentUser();
        University university = currentUser.getUniversity();

        model.addAttribute("email", new EmailHistoryRequest());
        model.addAttribute("recipients", userService.getUsersByUniversity(university.getId()));
        model.addAttribute("university", university);
        model.addAttribute("isEdit", false);
        model.addAttribute("activeTab", "university-emails");
        return "admin/university/email-form";
    }

    @PostMapping("/emails/create")
    public String createEmail(@Valid @ModelAttribute("email") EmailHistoryRequest request,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        User currentUser = userService.getCurrentUser();
        University university = currentUser.getUniversity();

        if (result.hasErrors()) {
            model.addAttribute("recipients", userService.getUsersByUniversity(university.getId()));
            model.addAttribute("university", university);
            model.addAttribute("isEdit", false);
            model.addAttribute("activeTab", "university-emails");
            return "admin/university/email-form";
        }

        emailHistoryService.createEmail(request, currentUser);
        redirectAttributes.addFlashAttribute("success", "Email sent successfully!");
        return "redirect:/admin/university/emails";
    }

    @GetMapping("/emails")
    public String listSentEmails(Model model) {
        User currentUser = userService.getCurrentUser();
        University university = currentUser.getUniversity();

        List<EmailHistory> emails = emailHistoryService.getEmailHistoryForUniversity(university.getId());

        model.addAttribute("emails", emails);
        model.addAttribute("university", university);
        model.addAttribute("activeTab", "university-emails");
        return "admin/university/email-list";
    }

    @GetMapping("/emails/{id}/edit")
    public String showEditEmailForm(@PathVariable Long id, Model model) {
        User currentUser = userService.getCurrentUser();
        University university = currentUser.getUniversity();

        EmailHistory emailHistory = emailHistoryService.getEmailHistoryById(id);
        if (!emailHistory.getUniversity().getId().equals(university.getId())) {
            throw new RuntimeException("Email not found");
        }

        EmailHistoryRequest request = new EmailHistoryRequest();
        request.setRecipientId(emailHistory.getRecipient().getId());
        request.setSubject(emailHistory.getSubject());
        request.setBody(emailHistory.getBody());

        model.addAttribute("email", request);
        model.addAttribute("emailId", id);
        model.addAttribute("recipients", userService.getUsersByUniversity(university.getId()));
        model.addAttribute("university", university);
        model.addAttribute("isEdit", true);
        model.addAttribute("activeTab", "university-emails");
        return "admin/university/email-form";
    }

    @PostMapping("/emails/{id}/edit")
    public String updateEmail(@PathVariable Long id,
                              @Valid @ModelAttribute("email") EmailHistoryRequest request,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        User currentUser = userService.getCurrentUser();
        University university = currentUser.getUniversity();

        if (result.hasErrors()) {
            model.addAttribute("emailId", id);
            model.addAttribute("recipients", userService.getUsersByUniversity(university.getId()));
            model.addAttribute("university", university);
            model.addAttribute("isEdit", true);
            model.addAttribute("activeTab", "university-emails");
            return "admin/university/email-form";
        }

        emailHistoryService.updateEmail(id, request, currentUser);
        redirectAttributes.addFlashAttribute("success", "Email updated and resent successfully!");
        return "redirect:/admin/university/emails";
    }

    @PostMapping("/emails/{id}/delete")
    public String deleteEmail(@PathVariable Long id,
                              RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        emailHistoryService.deleteEmail(id, currentUser);
        redirectAttributes.addFlashAttribute("success", "Email deleted successfully!");
        return "redirect:/admin/university/emails";
    }
}
