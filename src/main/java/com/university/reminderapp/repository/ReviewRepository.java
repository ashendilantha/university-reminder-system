package com.university.reminderapp.repository;

import com.university.reminderapp.model.Event;
import com.university.reminderapp.model.Review;
import com.university.reminderapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEvent(Event event);
    List<Review> findByStudent(User student);
    Optional<Review> findByEventAndStudent(Event event, User student);
    List<Review> findByEventId(Long eventId);
    void deleteByStudent(User student);
}
