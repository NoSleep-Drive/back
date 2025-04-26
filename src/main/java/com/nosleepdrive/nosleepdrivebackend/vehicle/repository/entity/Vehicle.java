package com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity;

import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicle", updatable = false)
    private Long idVehicle;

    @Column(name = "car_number", updatable = false, unique = true, length = 35)
    private String carNumber;

    @Column(name = "error_state")
    private int errorState;

    @Column(name="created_date")
    private Date createdDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_company", referencedColumnName = "id_company")
    Company company;
}
