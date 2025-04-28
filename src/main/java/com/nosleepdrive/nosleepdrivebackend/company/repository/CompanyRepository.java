package com.nosleepdrive.nosleepdrivebackend.company.repository;

import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository  extends JpaRepository<Company, Long> {
}
