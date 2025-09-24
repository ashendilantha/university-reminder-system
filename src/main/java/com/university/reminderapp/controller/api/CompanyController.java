package com.university.reminderapp.controller.api;

import com.university.reminderapp.dto.request.CompanyRequest;
import com.university.reminderapp.dto.response.ApiResponse;
import com.university.reminderapp.model.Company;
import com.university.reminderapp.model.User;
import com.university.reminderapp.service.CompanyService;
import com.university.reminderapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Company>> getActiveCompanies() {
        return ResponseEntity.ok(companyService.getActiveCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@Valid @RequestBody CompanyRequest request) {
        User currentUser = userService.getCurrentUser();
        return new ResponseEntity<>(companyService.createCompany(request, currentUser), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @Valid @RequestBody CompanyRequest request) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(companyService.updateCompany(id, request, currentUser));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Company> deactivateCompany(@PathVariable Long id) {
        User currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(companyService.deactivateCompany(id, currentUser));
    }
}