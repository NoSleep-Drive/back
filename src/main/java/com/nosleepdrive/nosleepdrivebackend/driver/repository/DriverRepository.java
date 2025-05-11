package com.nosleepdrive.nosleepdrivebackend.driver.repository;

import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
}
