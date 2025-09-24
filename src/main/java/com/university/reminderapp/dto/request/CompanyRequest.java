package com.university.reminderapp.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CompanyRequest {
    @NotBlank(message = "Company name is required")
    private String name;

    private String description;

    private String status;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}