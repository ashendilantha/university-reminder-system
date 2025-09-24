package com.university.reminderapp.repository;

import com.university.reminderapp.model.Company;
import com.university.reminderapp.model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    List<University> findByCompanyAndStatus(Company company, String status);
    List<University> findByCompanyId(Long companyId);
    List<University> findByCompanyIdAndStatusNot(Long companyId, String status);
    List<University> findByStatus(String status);
}
