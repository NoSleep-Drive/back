package com.nosleepdrive.nosleepdrivebackend.driver.repository;

import com.nosleepdrive.nosleepdrivebackend.driver.repository.entity.Driver;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query("SELECT d FROM Driver d WHERE d.vehicle = :vehicle AND d.startTime < :date AND (d.endTime > :date OR d.endTime IS NULL)")
    List<Driver> findActiveDrivers(
            @Param("vehicle") Vehicle vehicle,
            @Param("date") Date date);
}
