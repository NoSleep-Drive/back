package com.nosleepdrive.nosleepdrivebackend.sleep.repository;

import com.nosleepdrive.nosleepdrivebackend.sleep.repository.entity.Sleep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepRepository extends JpaRepository<Sleep, Long> {
}
