package com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity;

import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.Date;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicle", updatable = false)
    private Long idVehicle;

    @NonNull
    @Column(name = "id_Hardware", updatable = false, unique = true, length = 50, nullable = false)
    private Long idHardware;

    @NonNull
    @Column(name = "car_number", updatable = false, unique = true, length = 45, nullable = false)
    private String carNumber;

    @NonNull
    @Column(name = "error_state", nullable = false)
    private int errorState;

    @NonNull
    @Column(name="created_date", updatable = false, nullable = false)
    private Date createdDate;

    @NonNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_company", referencedColumnName = "id_company", nullable = false)
    private Company company;
}
