package com.university.reminderapp.controller.api;

import com.university.reminderapp.dto.request.UniversityAdminRequest;
import com.university.reminderapp.dto.request.UniversityRequest;
import com.university.reminderapp.dto.response.ApiResponse;
import com.university.reminderapp.model.University;
import com.university.reminderapp.model.User;
import com.university.reminderapp.service.UniversityService;
import com.university.reminderapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/universities")
public class UniversityController {
    @Autowired
    private UniversityService universityService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<University>> getAllUniversities() {
        return ResponseEntity.ok(universityService.getAllUniversities());
    }

    @GetMapping("/active")
    public ResponseEntity<List<University>> getActiveUniversities() {
        return ResponseEntity.ok(universityService.getActiveUniversities());
    }

    @GetMapping("/by-company/{companyId}")
    public ResponseEntity<List<University>> getUniversitiesByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(universityService.getUniversitiesByCompany(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<University> getUniversityById(@PathVariable Long id) {
        return ResponseEntity.ok(universityService.getUniversityById(id));
    }

    @PostMapping
    public ResponseEntity<University> createUniversity(@Valid @RequestBody UniversityRequest request) {
        User currentUser = userService.getCurrentUser();
        return new ResponseEntity<>(universityService.createUniversity(request, currentUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<University> updateUniversity(@PathVariable Long id, @Valid @RequestBody UniversityRequest request) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(universityService.updateUniversity(id, request, currentUser));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<University> deactivateUniversity(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(universityService.deactivateUniversity(id, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUniversity(@PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.ok(new ApiResponse(true, "University deleted successfully"));
    }

    @PostMapping("/{id}/admins")
    public ResponseEntity<User> createUniversityAdmin(@PathVariable Long id, @Valid @RequestBody UniversityAdminRequest request) {
        request.setUniversityId(id);
        User currentUser = userService.getCurrentUser();
        User admin = userService.createUniversityAdmin(request, currentUser);
        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }
}