package com.nosleepdrive.nosleepdrivebackend.vehicle.repository;

import com.nosleepdrive.nosleepdrivebackend.user.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<User, Long> {

}