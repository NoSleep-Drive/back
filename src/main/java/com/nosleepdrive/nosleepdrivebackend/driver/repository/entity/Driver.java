package com.nosleepdrive.nosleepdrivebackend.driver.repository.entity;

import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.Date;

@Entity
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_driver", updatable = false)
    private Long idDriver;

    @NonNull
    @Column(name = "start_time", nullable = false, updatable = false)
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_vehicle", referencedColumnName = "id_vehicle")
    Vehicle vehicle;
}
