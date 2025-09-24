package com.university.reminderapp.service;

import com.university.reminderapp.dto.request.CompanyRequest;
import com.university.reminderapp.exception.ResourceNotFoundException;
import com.university.reminderapp.model.Company;
import com.university.reminderapp.model.User;
import com.university.reminderapp.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findByStatusNot("DELETED");
    }

    public List<Company> getActiveCompanies() {
        return companyRepository.findByStatus("ACTIVE");
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }

    public Company createCompany(CompanyRequest request, User currentUser) {
        Company company = new Company();
        company.setName(request.getName());
        company.setDescription(request.getDescription());
        company.setStatus("ACTIVE");
        company.setCreatedBy(currentUser);
        company.setUpdatedBy(currentUser);

        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, CompanyRequest request, User currentUser) {
        Company company = getCompanyById(id);

        company.setName(request.getName());
        company.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            company.setStatus(request.getStatus());
        }
        company.setUpdatedBy(currentUser);

        return companyRepository.save(company);
    }

    public Company deactivateCompany(Long id, User currentUser) {
        Company company = getCompanyById(id);
        company.setStatus("INACTIVE");
        company.setUpdatedBy(currentUser);

        return companyRepository.save(company);
    }

    public void deleteCompany(Long id, User currentUser) {
        Company company = getCompanyById(id);
        company.setStatus("DELETED");
        company.setUpdatedBy(currentUser);
        companyRepository.save(company);
    }
}
