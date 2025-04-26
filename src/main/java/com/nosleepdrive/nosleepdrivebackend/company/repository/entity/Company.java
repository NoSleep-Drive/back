package com.nosleepdrive.nosleepdrivebackend.company.repository.entity;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_company", updatable = false)
    private Long idCompany;

    @NonNull
    @Column(name = "id", updatable = false, unique = true, length = 50)
    private String id;

    @NonNull
    @Column(name = "password", length = 50)
    private String password;

    @NonNull
    @Column(name = "company_name", length = 50)
    private String companyName;
}

