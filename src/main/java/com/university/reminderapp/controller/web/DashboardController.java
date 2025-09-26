////package com.university.reminderapp.controller.web;
////
////import com.university.reminderapp.model.User;
////import com.university.reminderapp.service.UserService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.access.prepost.PreAuthorize;
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.servlet.view.RedirectView;
////
////@Controller
////public class DashboardController {
////
////    @Autowired
////    private UserService userService;
////
////    /**
////     * Redirects authenticated users to their specific dashboard based on their role.
////     */
////    @GetMapping("/dashboard")
////    @PreAuthorize("isAuthenticated()")
////    public String dashboardRedirect() {
////        User currentUser = userService.getCurrentUser();
////        String role = currentUser.getRole();
////
////        switch (role) {
////            case "COMPANY_ADMIN":
////                return "redirect:/admin/company-dashboard";
////            case "UNIVERSITY_ADMIN":
////                return "redirect:/admin/university-dashboard";
////            case "EVENT_MANAGER":
////                return "redirect:/event-manager-dashboard";
////            case "STUDENT":
////                return "redirect:/student-dashboard";
////            case "PARENT":
////                return "redirect:/parent-dashboard";
////            default:
////                return "redirect:/"; // Redirect to home for unknown roles
////        }
////    }
////
////    @GetMapping("/admin/company-dashboard")
////    @PreAuthorize("hasAuthority('COMPANY_ADMIN')")
////    public String companyAdminDashboard(Model model) {
////        // Add necessary model attributes for the company-admin.html template
////        model.addAttribute("totalUniversities", 0); // Replace with actual data
////        model.addAttribute("activeUniversities", 0);
////        model.addAttribute("activeUniversitiesPercentage", 0);
////        model.addAttribute("totalAdmins", 0);
////        model.addAttribute("recentUniversities", java.util.Collections.emptyList());
////        return "company-admin";
////    }
////
////    @GetMapping("/admin/university-dashboard")
////    @PreAuthorize("hasAuthority('UNIVERSITY_ADMIN')")
////    public String universityAdminDashboard(Model model) {
////        User currentUser = userService.getCurrentUser();
////        model.addAttribute("university", currentUser.getUniversity());
////        // Add other necessary model attributes
////        model.addAttribute("studentCount", 0);
////        model.addAttribute("eventManagerCount", 0);
////        model.addAttribute("parentCount", 0);
////        model.addAttribute("eventCount", 0);
////        model.addAttribute("pendingBills", java.util.Collections.emptyList());
////        model.addAttribute("upcomingEvents", java.util.Collections.emptyList());
////        model.addAttribute("activeBillNotices", java.util.Collections.emptyList());
////        return "university-admin";
////    }
////
////    @GetMapping("/event-manager-dashboard")
////    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
////    public String eventManagerDashboard(Model model) {
////        // Add necessary model attributes for event-manager.html
////        model.addAttribute("totalEvents", 0);
////        model.addAttribute("upcomingEventsCount", 0);
////        model.addAttribute("totalReviews", 0);
////        model.addAttribute("upcomingEvents", java.util.Collections.emptyList());
////        model.addAttribute("recentReviews", java.util.Collections.emptyList());
////        return "event-manager";
////    }
////
////    @GetMapping("/student-dashboard")
////    @PreAuthorize("hasAuthority('STUDENT')")
////    public String studentDashboard(Model model) {
////        model.addAttribute("student", userService.getCurrentUser());
////        // Add other necessary model attributes
////        model.addAttribute("billCount", 0);
////        model.addAttribute("pendingBillCount", 0);
////        model.addAttribute("upcomingEventsCount", 0);
////        model.addAttribute("reviewCount", 0);
////        model.addAttribute("activeBillNotices", java.util.Collections.emptyList());
////        model.addAttribute("pendingBills", java.util.Collections.emptyList());
////        model.addAttribute("upcomingEvents", java.util.Collections.emptyList());
////        return "student";
////    }
////
////    @GetMapping("/parent-dashboard")
////    @PreAuthorize("hasAuthority('PARENT')")
////    public String parentDashboard(Model model) {
////        model.addAttribute("parent", userService.getCurrentUser());
////        // Add other necessary model attributes
////        model.addAttribute("studentCount", 0);
////        model.addAttribute("pendingBillCount", 0);
////        model.addAttribute("upcomingEventsCount", 0);
////        model.addAttribute("students", java.util.Collections.emptyList());
////        model.addAttribute("studentPendingBills", new java.util.HashMap<>());
////        model.addAttribute("recentBills", java.util.Collections.emptyList());
////        model.addAttribute("activeBillNotices", java.util.Collections.emptyList());
////        model.addAttribute("upcomingEvents", java.util.Collections.emptyList());
////        return "parent";
////    }
////}
//
////
////
////package com.university.reminderapp.controller.web;
////
////import com.university.reminderapp.model.User;
////import com.university.reminderapp.service.UserService;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.security.access.prepost.PreAuthorize;
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.web.bind.annotation.GetMapping;
////
////@Controller
////public class DashboardController {
////
////    @Autowired
////    private UserService userService;
////
////    /**
////     * Redirects authenticated users to their specific dashboard based on their role.
////     */
////    @GetMapping("/dashboard")
////    @PreAuthorize("isAuthenticated()")
////    public String dashboardRedirect() {
////        User currentUser = userService.getCurrentUser();
////        String role = currentUser.getRole();
////
////        switch (role) {
////            case "COMPANY_ADMIN":
////                return "redirect:/admin/company-dashboard";
////            case "UNIVERSITY_ADMIN":
////                return "redirect:/admin/university-dashboard";
////            case "EVENT_MANAGER":
////                return "redirect:/event-manager-dashboard";
////            case "STUDENT":
////                return "redirect:/student-dashboard";
////            case "PARENT":
////                return "redirect:/parent-dashboard";
////            default:
////                return "redirect:/"; // Redirect to home for unknown roles
////        }
////    }
////
////    @GetMapping("/admin/company-dashboard")
////    @PreAuthorize("hasRole('COMPANY_ADMIN')")
////    public String companyAdminDashboard(Model model) {
////        // Add necessary model attributes for the company-admin.html template
////        model.addAttribute("totalUniversities", 0); // Replace with actual data
////        model.addAttribute("activeUniversities", 0);
////        model.addAttribute("activeUniversitiesPercentage", 0);
////        model.addAttribute("totalAdmins", 0);
////        model.addAttribute("recentUniversities", java.util.Collections.emptyList());
////        return "company-admin";
////    }
////
////    @GetMapping("/admin/university-dashboard")
////    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
////    public String universityAdminDashboard(Model model) {
////        User currentUser = userService.getCurrentUser();
////        model.addAttribute("university", currentUser.getUniversity());
////        // Add other necessary model attributes
////        model.addAttribute("studentCount", 0);
////        model.addAttribute("eventManagerCount", 0);
////        model.addAttribute("parentCount", 0);
////        model.addAttribute("eventCount", 0);
////        model.addAttribute("pendingBills", java.util.Collections.emptyList());
////        model.addAttribute("upcomingEvents", java.util.Collections.emptyList());
////        model.addAttribute("activeBillNotices", java.util.Collections.emptyList());
////        return "university-admin";
////    }
////
////    @GetMapping("/event-manager-dashboard")
////    @PreAuthorize("hasRole('EVENT_MANAGER')")
////    public String eventManagerDashboard(Model model) {
////        // Add necessary model attributes for event-manager.html
////        model.addAttribute("totalEvents", 0);
////        model.addAttribute("upcomingEventsCount", 0);
////        model.addAttribute("totalReviews", 0);
////        model.addAttribute("upcomingEvents", java.util.Collections.emptyList());
////        model.addAttribute("recentReviews", java.util.Collections.emptyList());
////        return "event-manager";
////    }
////
////    @GetMapping("/student-dashboard")
////    @PreAuthorize("hasRole('STUDENT')")
////    public String studentDashboard(Model model) {
////        User currentUser = userService.getCurrentUser();
////        model.addAttribute("student", currentUser);
////        // Add other necessary model attributes
////        model.addAttribute("billCount", 0);
////        model.addAttribute("pendingBillCount", 0);
////        model.addAttribute("upcomingEventsCount", 0);
////        model.addAttribute("reviewCount", 0);
////        model.addAttribute("activeBillNotices", java.util.Collections.emptyList());
////        model.addAttribute("pendingBills", java.util.Collections.emptyList());
////        model.addAttribute("upcomingEvents", java.util.Collections.emptyList());
////        return "student";
////    }
////
////    @GetMapping("/parent-dashboard")
////    @PreAuthorize("hasRole('PARENT')")
////    public String parentDashboard(Model model) {
////        User currentUser = userService.getCurrentUser();
////        model.addAttribute("parent", currentUser);
////        // Add other necessary model attributes
////        model.addAttribute("studentCount", 0);
////        model.addAttribute("pendingBillCount", 0);
////        model.addAttribute("upcomingEventsCount", 0);
////        model.addAttribute("students", java.util.Collections.emptyList());
////        model.addAttribute("studentPendingBills", new java.util.HashMap<>());
////        model.addAttribute("recentBills", java.util.Collections.emptyList());
////        model.addAttribute("activeBillNotices", java.util.Collections.emptyList());
////        model.addAttribute("upcomingEvents", java.util.Collections.emptyList());
////        return "parent";
////    }
////}
//
//
//
//package com.university.reminderapp.controller.web;
//
//import com.university.reminderapp.model.User;
//import com.university.reminderapp.service.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.time.LocalDateTime;
//import java.util.stream.Collectors;
//
//@Controller
//public class DashboardController {
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private UniversityService universityService;
//    @Autowired
//    private EventService eventService;
//    @Autowired
//    private BillService billService;
//    @Autowired
//    private BillNoticeService billNoticeService;
//    @Autowired
//    private ReviewService reviewService;
//
//    @GetMapping("/dashboard")
//    @PreAuthorize("isAuthenticated()")
//    public String dashboardRedirect() {
//        User currentUser = userService.getCurrentUser();
//        if (currentUser == null) {
//            return "redirect:/login?error";
//        }
//        String role = currentUser.getRole();
//
//        switch (role) {
//            case "COMPANY_ADMIN":
//                return "redirect:/admin/company-dashboard";
//            case "UNIVERSITY_ADMIN":
//                return "redirect:/admin/university-dashboard";
//            case "EVENT_MANAGER":
//                return "redirect:/event-manager-dashboard";
//            case "STUDENT":
//                return "redirect:/student-dashboard";
//            case "PARENT":
//                return "redirect:/parent-dashboard";
//            default:
//                return "redirect:/";
//        }
//    }
//
//    @GetMapping("/admin/company-dashboard")
//    @PreAuthorize("hasRole('COMPANY_ADMIN')")
//    public String companyAdminDashboard(Model model) {
//        long totalUniversities = universityService.getAllUniversities().size();
//        long activeUniversities = universityService.getActiveUniversities().size();
//        model.addAttribute("totalUniversities", totalUniversities);
//        model.addAttribute("activeUniversities", activeUniversities);
//        model.addAttribute("activeUniversitiesPercentage", totalUniversities > 0 ? (activeUniversities * 100 / totalUniversities) : 0);
//        model.addAttribute("totalAdmins", userService.getAllUsers().stream().filter(u -> "UNIVERSITY_ADMIN".equals(u.getRole())).count());
//        model.addAttribute("recentUniversities", universityService.getAllUniversities().stream().limit(5).collect(Collectors.toList()));
//        return "company-admin";
//    }
//
//    @GetMapping("/admin/university-dashboard")
//    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
//    public String universityAdminDashboard(Model model) {
//        User currentUser = userService.getCurrentUser();
//        Long universityId = currentUser.getUniversity().getId();
//        model.addAttribute("university", currentUser.getUniversity());
//        model.addAttribute("studentCount", userService.getUsersByUniversityAndRole(universityId, "STUDENT").size());
//        model.addAttribute("eventManagerCount", userService.getUsersByUniversityAndRole(universityId, "EVENT_MANAGER").size());
//        model.addAttribute("parentCount", userService.getUsersByUniversityAndRole(universityId, "PARENT").size());
//        model.addAttribute("eventCount", eventService.getUpcomingEvents(universityId).size());
//        model.addAttribute("pendingBills", billService.getBillsByUniversity(universityId).stream()
//                .filter(b -> "PENDING".equals(b.getStatus())).limit(5).collect(Collectors.toList()));
//        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents(universityId).stream().limit(5).collect(Collectors.toList()));
//        model.addAttribute("activeBillNotices", billNoticeService.getActiveBillNotices(universityId));
//        return "university-admin";
//    }
//
//    @GetMapping("/event-manager-dashboard")
//    @PreAuthorize("hasRole('EVENT_MANAGER')")
//    public String eventManagerDashboard(Model model) {
//        User currentUser = userService.getCurrentUser();
//        Long universityId = currentUser.getUniversity().getId();
//        model.addAttribute("totalEvents", eventService.getEventsByUniversity(universityId).size());
//        model.addAttribute("upcomingEventsCount", eventService.getUpcomingEvents(universityId).size());
//        model.addAttribute("totalReviews", reviewService.getAllReviews().stream()
//                .filter(r -> r.getUniversity().getId().equals(universityId)).count());
//        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents(universityId).stream().limit(5).collect(Collectors.toList()));
//        model.addAttribute("recentReviews", reviewService.getAllReviews().stream()
//                .filter(r -> r.getUniversity().getId().equals(universityId)).limit(5).collect(Collectors.toList()));
//        return "event-manager";
//    }
//
//    @GetMapping("/student-dashboard")
//    @PreAuthorize("hasRole('STUDENT')")
//    public String studentDashboard(Model model) {
//        User currentUser = userService.getCurrentUser();
//        Long universityId = currentUser.getUniversity().getId();
//        model.addAttribute("student", currentUser);
//        model.addAttribute("billCount", billService.getBillsByStudent(currentUser).size());
//        model.addAttribute("pendingBillCount", billService.getBillsByStudent(currentUser).stream().filter(b -> "PENDING".equals(b.getStatus())).count());
//        model.addAttribute("upcomingEventsCount", eventService.getUpcomingEvents(universityId).size());
//        model.addAttribute("reviewCount", reviewService.getReviewsByStudent(currentUser).size());
//        model.addAttribute("activeBillNotices", billNoticeService.getActiveBillNotices(universityId));
//        model.addAttribute("pendingBills", billService.getBillsByStudent(currentUser).stream()
//                .filter(b -> "PENDING".equals(b.getStatus())).limit(5).collect(Collectors.toList()));
//        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents(universityId).stream().limit(5).collect(Collectors.toList()));
//        return "student";
//    }
//
//    @GetMapping("/parent-dashboard")
//    @PreAuthorize("hasRole('PARENT')")
//    public String parentDashboard(Model model) {
//        User currentUser = userService.getCurrentUser();
//        Long universityId = currentUser.getUniversity().getId();
//        model.addAttribute("parent", currentUser);
//        model.addAttribute("studentCount", userService.getStudentsByParent(currentUser.getId()).size());
//        model.addAttribute("pendingBillCount", billService.getBillsByParent(currentUser).stream().filter(b -> "PENDING".equals(b.getStatus())).count());
//        model.addAttribute("upcomingEventsCount", eventService.getUpcomingEvents(universityId).size());
//        model.addAttribute("students", userService.getStudentsByParent(currentUser.getId()));
//        model.addAttribute("studentPendingBills", billService.getBillsByParent(currentUser).stream()
//                .collect(Collectors.groupingBy(b -> b.getStudent().getId(), Collectors.counting())));
//        model.addAttribute("recentBills", billService.getBillsByParent(currentUser).stream().limit(5).collect(Collectors.toList()));
//        model.addAttribute("activeBillNotices", billNoticeService.getActiveBillNotices(universityId));
//        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents(universityId).stream().limit(5).collect(Collectors.toList()));
//        return "parent";
//    }
//}




package com.university.reminderapp.controller.web;

import com.university.reminderapp.model.User;
import com.university.reminderapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
public class DashboardController {

    @Autowired
    private UserService userService;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private EventService eventService;
    @Autowired
    private BillService billService;
    @Autowired
    private BillNoticeService billNoticeService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()") // Ensures user is logged in before redirecting
    public String dashboardRedirect() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            // This should not happen if @PreAuthorize is working, but it's a good safeguard
            return "redirect:/login?error=true";
        }
        String role = currentUser.getRole();

        switch (role) {
            case "COMPANY_ADMIN":
                return "redirect:/admin/company-dashboard";
            case "UNIVERSITY_ADMIN":
                return "redirect:/admin/university-dashboard";
            case "EVENT_MANAGER":
                return "redirect:/event-manager-dashboard";
            case "STUDENT":
                return "redirect:/student-dashboard";
            case "PARENT":
                return "redirect:/parent-dashboard";
            default:
                // Redirect to home for unknown roles or if something goes wrong
                return "redirect:/";
        }
    }

    @GetMapping("/admin/company-dashboard")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    public String companyAdminDashboard(Model model) {
        long totalUniversities = universityService.getAllUniversities().size();
        long activeUniversities = universityService.getActiveUniversities().size();
        model.addAttribute("totalUniversities", totalUniversities);
        model.addAttribute("activeUniversities", activeUniversities);
        model.addAttribute("activeUniversitiesPercentage", totalUniversities > 0 ? (activeUniversities * 100 / totalUniversities) : 0);
        model.addAttribute("totalAdmins", userService.getAllUsers().stream().filter(u -> "UNIVERSITY_ADMIN".equals(u.getRole())).count());
        model.addAttribute("recentUniversities", universityService.getAllUniversities().stream().limit(5).collect(Collectors.toList()));
        return "dashboards/company-admin";
    }

    @GetMapping("/admin/university-dashboard")
    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
    public String universityAdminDashboard(Model model) {
        User currentUser = userService.getCurrentUser();
        Long universityId = currentUser.getUniversity().getId();
        model.addAttribute("university", currentUser.getUniversity());
        model.addAttribute("studentCount", userService.getUsersByUniversityAndRole(universityId, "STUDENT").size());
        model.addAttribute("eventManagerCount", userService.getUsersByUniversityAndRole(universityId, "EVENT_MANAGER").size());
        model.addAttribute("parentCount", userService.getUsersByUniversityAndRole(universityId, "PARENT").size());
        model.addAttribute("eventCount", eventService.getUpcomingEvents(universityId).size());
        model.addAttribute("pendingBills", billService.getBillsByUniversity(universityId).stream()
                .filter(b -> "PENDING".equals(b.getStatus())).limit(5).collect(Collectors.toList()));
        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents(universityId).stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("activeBillNotices", billNoticeService.getActiveBillNotices(universityId));
        return "dashboards/university-admin";
    }

    @GetMapping("/event-manager-dashboard")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public String eventManagerDashboard(Model model) {
        User currentUser = userService.getCurrentUser();
        Long universityId = currentUser.getUniversity().getId();
        model.addAttribute("totalEvents", eventService.getEventsByUniversity(universityId).size());
        model.addAttribute("upcomingEventsCount", eventService.getUpcomingEvents(universityId).size());
        model.addAttribute("totalReviews", reviewService.getAllReviews().stream()
                .filter(r -> r.getUniversity().getId().equals(universityId)).count());
        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents(universityId).stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("recentReviews", reviewService.getAllReviews().stream()
                .filter(r -> r.getUniversity().getId().equals(universityId)).limit(5).collect(Collectors.toList()));
        return "dashboards/event-manager";
    }

    @GetMapping("/student-dashboard")
    @PreAuthorize("hasRole('STUDENT')")
    public String studentDashboard(Model model) {
        User currentUser = userService.getCurrentUser();
        Long universityId = currentUser.getUniversity().getId();
        model.addAttribute("student", currentUser);
        model.addAttribute("billCount", billService.getBillsByStudent(currentUser).size());
        model.addAttribute("pendingBillCount", billService.getBillsByStudent(currentUser).stream().filter(b -> "PENDING".equals(b.getStatus())).count());
        model.addAttribute("upcomingEventsCount", eventService.getUpcomingEvents(universityId).size());
        model.addAttribute("reviewCount", reviewService.getReviewsByStudent(currentUser).size());
        model.addAttribute("activeBillNotices", billNoticeService.getActiveBillNotices(universityId));
        model.addAttribute("pendingBills", billService.getBillsByStudent(currentUser).stream()
                .filter(b -> "PENDING".equals(b.getStatus())).limit(5).collect(Collectors.toList()));
        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents(universityId).stream().limit(5).collect(Collectors.toList()));
        return "dashboards/student";
    }

    @GetMapping("/parent-dashboard")
    @PreAuthorize("hasRole('PARENT')")
    public String parentDashboard(Model model) {
        User currentUser = userService.getCurrentUser();
        Long universityId = currentUser.getUniversity().getId();
        model.addAttribute("parent", currentUser);
        model.addAttribute("studentCount", userService.getStudentsByParent(currentUser.getId()).size());
        model.addAttribute("pendingBillCount", billService.getBillsByParent(currentUser).stream().filter(b -> "PENDING".equals(b.getStatus())).count());
        model.addAttribute("upcomingEventsCount", eventService.getUpcomingEvents(universityId).size());
        model.addAttribute("students", userService.getStudentsByParent(currentUser.getId()));
        model.addAttribute("studentPendingBills", billService.getBillsByParent(currentUser).stream()
                .collect(Collectors.groupingBy(b -> b.getStudent().getId(), Collectors.counting())));
        model.addAttribute("recentBills", billService.getBillsByParent(currentUser).stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("activeBillNotices", billNoticeService.getActiveBillNotices(universityId));
        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents(universityId).stream().limit(5).collect(Collectors.toList()));
        return "dashboards/parent";
    }

    // Convenience redirect so legacy /admin/bills links work
    @GetMapping("/admin/bills")
    @PreAuthorize("hasRole('UNIVERSITY_ADMIN')")
    public String adminBillsRedirect() {
        return "redirect:/bills";
    }
}