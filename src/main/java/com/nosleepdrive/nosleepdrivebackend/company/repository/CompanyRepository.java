package com.nosleepdrive.nosleepdrivebackend.company.repository;

import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository  extends JpaRepository<Company, Long> {
    boolean existsById(String id);
    Optional<Company> findById(String id);
}
