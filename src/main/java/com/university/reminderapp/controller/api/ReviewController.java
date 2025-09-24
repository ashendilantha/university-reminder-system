package com.university.reminderapp.controller.api;

import com.university.reminderapp.dto.request.ReviewRequest;
import com.university.reminderapp.dto.response.ApiResponse;
import com.university.reminderapp.model.Review;
import com.university.reminderapp.model.User;
import com.university.reminderapp.service.ReviewService;
import com.university.reminderapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Review>> getReviews(@RequestParam(required = false) Long eventId,
                                                   @RequestParam(required = false) Boolean me) {
        User currentUser = userService.getCurrentUser();

        if (eventId != null) {
            return ResponseEntity.ok(reviewService.getReviewsByEvent(eventId));
        } else if (Boolean.TRUE.equals(me) && currentUser.getRole().equals("STUDENT")) {
            return ResponseEntity.ok(reviewService.getReviewsByStudent(currentUser));
        } else {
            return ResponseEntity.ok(reviewService.getAllReviews());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @PostMapping
    public ResponseEntity<Review> createReview(@Valid @RequestBody ReviewRequest request) {
        User currentUser = userService.getCurrentUser();
        return new ResponseEntity<>(reviewService.createReview(request, currentUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(reviewService.updateReview(id, request, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        reviewService.deleteReview(id, currentUser);
        return ResponseEntity.ok(new ApiResponse(true, "Review deleted successfully"));
    }
}