package com.nosleepdrive.nosleepdrivebackend.sleep.repository;

import com.nosleepdrive.nosleepdrivebackend.sleep.repository.entity.Sleep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SleepRepository extends JpaRepository<Sleep, Long> {
    @Query("SELECT count(*) FROM Sleep s " +
            "JOIN s.driver d " +
            "JOIN d.vehicle v " +
            "JOIN v.company c " +
            "WHERE c.idCompany = :companyId AND FUNCTION('DATE', s.sleepTime) = FUNCTION('DATE', :date)")
    int getCountSleepsByCompanyIdAndDate(@Param("companyId") Long companyId, @Param("date") Date date);

    @Query("SELECT s FROM Sleep s " +
            "JOIN s.driver d " +
            "JOIN d.vehicle v " +
            "JOIN v.company c " +
            "WHERE c.idCompany = :companyId " +
            "ORDER BY s.sleepTime DESC ")
    List<Sleep> getRecentSleep(@Param("companyId") Long companyId);
}
