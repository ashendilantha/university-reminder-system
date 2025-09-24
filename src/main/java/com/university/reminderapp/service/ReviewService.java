package com.university.reminderapp.service;

import com.university.reminderapp.dto.request.ReviewRequest;
import com.university.reminderapp.exception.AccessDeniedException;
import com.university.reminderapp.exception.BadRequestException;
import com.university.reminderapp.exception.ResourceNotFoundException;
import com.university.reminderapp.model.Event;
import com.university.reminderapp.model.Review;
import com.university.reminderapp.model.User;
import com.university.reminderapp.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private EventService eventService;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByEvent(Long eventId) {
        return reviewRepository.findByEventId(eventId);
    }

    public List<Review> getReviewsByStudent(User student) {
        return reviewRepository.findByStudent(student);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    public Review createReview(ReviewRequest request, User currentUser) {
        if (!currentUser.getRole().equals("STUDENT")) {
            throw new AccessDeniedException("Only students can create reviews");
        }

        Event event = eventService.getEventById(request.getEventId());

        // Check if student has already reviewed this event
        Optional<Review> existingReview = reviewRepository.findByEventAndStudent(event, currentUser);
        if (existingReview.isPresent()) {
            throw new BadRequestException("You have already reviewed this event");
        }

        Review review = new Review();
        review.setUniversity(currentUser.getUniversity());
        review.setEvent(event);
        review.setStudent(currentUser);
        review.setRating(request.getRating());

        // Sanitize comment to prevent XSS attacks
        if (request.getComment() != null) {
            // Basic sanitization (remove script tags)
            String sanitizedComment = request.getComment().replaceAll("<script.*?>.*?</script>", "");
            review.setComment(sanitizedComment);
        }

        review.setCreatedBy(currentUser);
        review.setUpdatedBy(currentUser);

        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, ReviewRequest request, User currentUser) {
        Review review = getReviewById(id);

        // Check if current user is the review owner
        if (!review.getStudent().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only update your own reviews");
        }

        review.setRating(request.getRating());

        // Sanitize comment to prevent XSS attacks
        if (request.getComment() != null) {
            // Basic sanitization (remove script tags)
            String sanitizedComment = request.getComment().replaceAll("<script.*?>.*?</script>", "");
            review.setComment(sanitizedComment);
        }

        review.setUpdatedBy(currentUser);

        return reviewRepository.save(review);
    }

    public void deleteReview(Long id, User currentUser) {
        Review review = getReviewById(id);

        // Event Manager can delete any review, but students can only delete their own
        if (currentUser.getRole().equals("STUDENT") && !review.getStudent().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can only delete your own reviews");
        }

        // University Admin cannot delete reviews
        if (currentUser.getRole().equals("UNIVERSITY_ADMIN")) {
            throw new AccessDeniedException("University Admins cannot delete reviews");
        }

        reviewRepository.delete(review);
    }
}