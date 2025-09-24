package com.university.reminderapp.controller.api;

import com.university.reminderapp.dto.request.UserRequest;
import com.university.reminderapp.dto.response.ApiResponse;
import com.university.reminderapp.model.User;
import com.university.reminderapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) Long universityId,
                                               @RequestParam(required = false) String role) {
        if (universityId != null && role != null) {
            return ResponseEntity.ok(userService.getUsersByUniversityAndRole(universityId, role));
        } else {
            return ResponseEntity.ok(userService.getAllUsers());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request) {
        User currentUser = userService.getCurrentUser();
        return new ResponseEntity<>(userService.createUser(request, currentUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(userService.updateUser(id, request, currentUser));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(userService.deactivateUser(id, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        userService.deleteUser(id, currentUser);
        return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully"));
    }
}