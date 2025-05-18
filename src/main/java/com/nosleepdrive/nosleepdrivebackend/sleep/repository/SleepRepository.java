package com.nosleepdrive.nosleepdrivebackend.sleep.repository;

import com.nosleepdrive.nosleepdrivebackend.sleep.repository.entity.Sleep;
import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Query("SELECT s FROM Sleep s " +
            "WHERE (:companyId = s.driver.vehicle.company.idCompany) " +
            "AND (:startDate IS NULL OR s.sleepTime >= :startDate) " +
            "AND (:endDate IS NULL OR s.sleepTime <= :endDate) " +
            "AND (:vehicleNumber IS NULL OR s.driver.vehicle.carNumber = :vehicleNumber) " +
            "AND (:driverHash IS NULL OR s.driver.driverHash = :driverHash)"+
            "ORDER BY s.sleepTime DESC ")
    List<Sleep> getFilteredSleepData(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("vehicleNumber") String vehicleNumber,
            @Param("driverHash") String driverHash);

    @Query("SELECT s FROM Sleep s WHERE s.idSleep IN (:idSleeps)")
    List<Sleep> getSleepsByIdSleepIsIn(@Param("idSleeps") Collection<Long> idSleeps);

    Sleep findByIdSleep(Long idSleep);
}
