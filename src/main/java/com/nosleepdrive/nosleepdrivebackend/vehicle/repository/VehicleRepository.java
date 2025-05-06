package com.nosleepdrive.nosleepdrivebackend.vehicle.repository;

import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByCarNumber(String carNumber);
    boolean existsByIdHardware(String idHardware);
}