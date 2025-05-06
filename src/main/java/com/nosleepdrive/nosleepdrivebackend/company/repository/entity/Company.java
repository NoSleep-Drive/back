package com.nosleepdrive.nosleepdrivebackend.company.repository.entity;

import com.nosleepdrive.nosleepdrivebackend.vehicle.repository.entity.Vehicle;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_company", updatable = false)
    private Long idCompany;

    @NonNull
    @Column(name = "id", updatable = false, unique = true, length = 50, nullable = false)
    private String id;

    @NonNull
    @Column(name = "password", length = 64, nullable = false)
    private String password;

    @NonNull
    @Column(name = "company_name", length = 50, nullable = false)
    private String companyName;


    @Column(name = "business_number", length = 50, unique = true)
    private String businessNumber;

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();
}

