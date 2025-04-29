package com.nosleepdrive.nosleepdrivebackend.company.service;

import com.nosleepdrive.nosleepdrivebackend.common.Hash;
import com.nosleepdrive.nosleepdrivebackend.company.dto.CompanySignUpRequestDto;
import com.nosleepdrive.nosleepdrivebackend.company.repository.CompanyRepository;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final Hash hash;

    public void signup(CompanySignUpRequestDto request) {
        if (companyRepository.existsById(request.getId())) {
            throw new RuntimeException("ID already exists");
        }

        Company company = Company.builder()
                .id(request.getId())
                .password(hash.HashEncode(request.getPassword()))
                .companyName(request.getCompanyName())
                .businessNumber(request.getBusinessNumber())
                .build();

        companyRepository.save(company);
    }
}
