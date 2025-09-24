package com.university.reminderapp.repository;

import com.university.reminderapp.model.University;
import com.university.reminderapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByUniversityAndRole(University university, String role);
    List<User> findByUniversityId(Long universityId);
    List<User> findByUniversityIdAndStatusNot(Long universityId, String status);
    List<User> findByUniversityIdAndRole(Long universityId, String role);
    List<User> findByParentId(Long parentId);
}
