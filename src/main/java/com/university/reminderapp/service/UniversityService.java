package com.university.reminderapp.service;

import com.university.reminderapp.dto.request.UniversityRequest;
import com.university.reminderapp.exception.ResourceNotFoundException;
import com.university.reminderapp.model.Company;
import com.university.reminderapp.model.University;
import com.university.reminderapp.model.User;
import com.university.reminderapp.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityService {
    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private CompanyService companyService;

    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }

    public List<University> getUniversitiesByCompany(Long companyId) {
        return universityRepository.findByCompanyIdAndStatusNot(companyId, "DELETED");
    }

    public List<University> getActiveUniversities() {
        return universityRepository.findByStatus("ACTIVE");
    }

    public University getUniversityById(Long id) {
        return universityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("University not found with id: " + id));
    }

    public University createUniversity(UniversityRequest request, User currentUser) {
        Company company = companyService.getCompanyById(request.getCompanyId());

        University university = new University();
        university.setCompany(company);
        university.setName(request.getName());
        university.setDescription(request.getDescription());
        university.setAddress(request.getAddress());
        university.setContactEmail(request.getContactEmail());
        university.setContactPhone(request.getContactPhone());
        university.setStatus("ACTIVE");
        university.setCreatedBy(currentUser);
        university.setUpdatedBy(currentUser);

        return universityRepository.save(university);
    }

    public University updateUniversity(Long id, UniversityRequest request, User currentUser) {
        University university = getUniversityById(id);

        if (request.getCompanyId() != null) {
            Company company = companyService.getCompanyById(request.getCompanyId());
            university.setCompany(company);
        }

        university.setName(request.getName());
        university.setDescription(request.getDescription());
        university.setAddress(request.getAddress());
        university.setContactEmail(request.getContactEmail());
        university.setContactPhone(request.getContactPhone());

        if (request.getStatus() != null) {
            university.setStatus(request.getStatus());
        }

        university.setUpdatedBy(currentUser);

        return universityRepository.save(university);
    }

    public University deactivateUniversity(Long id, User currentUser) {
        University university = getUniversityById(id);
        university.setStatus("INACTIVE");
        university.setUpdatedBy(currentUser);

        return universityRepository.save(university);
    }

    public void deleteUniversity(Long id) {
        University university = getUniversityById(id);
        university.setStatus("DELETED");
        universityRepository.save(university);
    }
}
