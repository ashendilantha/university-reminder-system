package com.university.reminderapp.controller.web;

import com.university.reminderapp.exception.AccessDeniedException;
import com.university.reminderapp.model.User;
import com.university.reminderapp.service.BillService;
import com.university.reminderapp.service.EventService;
import com.university.reminderapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/parent")
@PreAuthorize("hasRole('PARENT')")
public class ParentWebController {

    @Autowired
    private UserService userService;

    @Autowired
    private BillService billService;

    @Autowired
    private EventService eventService;

    @GetMapping("/students")
    public String listStudents(Model model) {
        User currentUser = userService.getCurrentUser();
        List<User> students = userService.getStudentsByParent(currentUser.getId());

        Map<Long, Long> pendingCounts = billService.getBillsByParent(currentUser).stream()
                .filter(bill -> "PENDING".equalsIgnoreCase(bill.getStatus()))
                .collect(Collectors.groupingBy(bill -> bill.getStudent().getId(), Collectors.counting()));

        model.addAttribute("students", students);
        model.addAttribute("pendingCounts", pendingCounts);
        return "parent/parent-student-list";
    }

    @GetMapping("/students/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        User currentUser = userService.getCurrentUser();
        User student = userService.getUserById(id);

        if (student.getParent() == null || !student.getParent().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only view your linked students");
        }

        model.addAttribute("student", student);
        model.addAttribute("bills", billService.getBillsByParent(currentUser).stream()
                .filter(bill -> bill.getStudent().getId().equals(id))
                .collect(Collectors.toList()));
        model.addAttribute("upcomingEvents", eventService.getEventsByUniversity(student.getUniversity().getId()));
        return "parent/parent-student-detail";
    }
}
