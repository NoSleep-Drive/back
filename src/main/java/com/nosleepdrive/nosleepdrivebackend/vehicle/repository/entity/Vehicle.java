package com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity;

import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehicle", updatable = false)
    private Long idVehicle;

    @NonNull
    @Column(name = "id_Hardware", updatable = false, unique = true, length = 50, nullable = false)
    private String idHardware;

    @NonNull
    @Column(name = "car_number", unique = true, length = 45, nullable = false)
    private String carNumber;

    @NonNull
    @Column(name = "error_state", nullable = false)
    private int errorState;

    @NonNull
    @Column(name="created_date", updatable = false, nullable = false)
    private Date createdDate;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_company", referencedColumnName = "id_company", nullable = false)
    private Company company;
}
