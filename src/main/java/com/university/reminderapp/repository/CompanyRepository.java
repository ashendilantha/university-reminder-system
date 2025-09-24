package com.university.reminderapp.repository;

import com.university.reminderapp.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByStatus(String status);
    List<Company> findByStatusNot(String status);
}
