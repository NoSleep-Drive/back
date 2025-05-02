package com.nosleepdrive.nosleepdrivebackend.company.service;

import com.nosleepdrive.nosleepdrivebackend.common.CustomError;
import com.nosleepdrive.nosleepdrivebackend.common.Hash;
import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.company.dto.CompanySignUpRequestDto;
import com.nosleepdrive.nosleepdrivebackend.company.repository.CompanyRepository;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final Hash hash;

    public void signup(CompanySignUpRequestDto request) {
        if (companyRepository.existsById(request.getId())) {
            throw new CustomError(HttpStatus.CONFLICT.value(), Message.ERR_SIGNUP_DUPLICATION_ID.getMessage());
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
