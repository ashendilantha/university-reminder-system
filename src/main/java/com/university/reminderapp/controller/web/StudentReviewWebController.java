package com.university.reminderapp.controller.web;

import com.university.reminderapp.dto.request.ReviewRequest;
import com.university.reminderapp.exception.AccessDeniedException;
import com.university.reminderapp.model.Event;
import com.university.reminderapp.model.Review;
import com.university.reminderapp.model.User;
import com.university.reminderapp.service.EventService;
import com.university.reminderapp.service.ReviewService;
import com.university.reminderapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/student/reviews")
@PreAuthorize("hasRole('STUDENT')")
public class StudentReviewWebController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listMyReviews(Model model) {
        User currentUser = userService.getCurrentUser();
        List<Review> reviews = reviewService.getReviewsByStudent(currentUser);
        model.addAttribute("reviews", reviews);
        return "review/student-review-list";
    }

    @GetMapping("/create")
    public String showCreateForm(@RequestParam("eventId") Long eventId, Model model) {
        User currentUser = userService.getCurrentUser();
        Event event = eventService.getEventById(eventId);
        ensureSameUniversity(event, currentUser);

        ReviewRequest request = new ReviewRequest();
        request.setEventId(eventId);

        model.addAttribute("review", request);
        model.addAttribute("reviewId", null);
        model.addAttribute("event", event);
        return "review/review-form";
    }

    @PostMapping("/create")
    public String createReview(@Valid @ModelAttribute("review") ReviewRequest request,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        User currentUser = userService.getCurrentUser();
        Event event = eventService.getEventById(request.getEventId());
        ensureSameUniversity(event, currentUser);

        if (result.hasErrors()) {
            model.addAttribute("reviewId", null);
            model.addAttribute("event", event);
            return "review/review-form";
        }

        reviewService.createReview(request, currentUser);
        redirectAttributes.addFlashAttribute("success", "Review added successfully!");
        return "redirect:/events/" + request.getEventId();
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        User currentUser = userService.getCurrentUser();
        Review review = reviewService.getReviewById(id);
        ensureOwner(review, currentUser);

        ReviewRequest request = new ReviewRequest();
        request.setEventId(review.getEvent().getId());
        request.setRating(review.getRating());
        request.setComment(review.getComment());

        model.addAttribute("review", request);
        model.addAttribute("reviewId", id);
        model.addAttribute("event", review.getEvent());
        return "review/review-form";
    }

    @PostMapping("/{id}/edit")
    public String updateReview(@PathVariable Long id,
                               @Valid @ModelAttribute("review") ReviewRequest request,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        User currentUser = userService.getCurrentUser();
        Review review = reviewService.getReviewById(id);
        ensureOwner(review, currentUser);

        if (result.hasErrors()) {
            model.addAttribute("reviewId", id);
            model.addAttribute("event", review.getEvent());
            return "review/review-form";
        }

        reviewService.updateReview(id, request, currentUser);
        redirectAttributes.addFlashAttribute("success", "Review updated successfully!");
        return "redirect:/student/reviews";
    }

    @PostMapping("/{id}/delete")
    public String deleteReview(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        User currentUser = userService.getCurrentUser();
        Review review = reviewService.getReviewById(id);
        ensureOwner(review, currentUser);

        reviewService.deleteReview(id, currentUser);
        redirectAttributes.addFlashAttribute("success", "Review deleted successfully!");
        return "redirect:/student/reviews";
    }

    private void ensureSameUniversity(Event event, User user) {
        if (event.getUniversity() == null || user.getUniversity() == null ||
                !event.getUniversity().getId().equals(user.getUniversity().getId())) {
            throw new AccessDeniedException("You cannot review events from another university");
        }
    }

    private void ensureOwner(Review review, User user) {
        if (!review.getStudent().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only manage your own reviews");
        }
    }
}
