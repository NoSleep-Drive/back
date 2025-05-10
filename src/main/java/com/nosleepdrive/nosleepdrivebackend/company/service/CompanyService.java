package com.nosleepdrive.nosleepdrivebackend.company.service;

import com.nosleepdrive.nosleepdrivebackend.common.CustomError;
import com.nosleepdrive.nosleepdrivebackend.common.Hash;
import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.common.Token;
import com.nosleepdrive.nosleepdrivebackend.company.dto.CompanyLoginRequestDto;
import com.nosleepdrive.nosleepdrivebackend.company.dto.CompanySignUpRequestDto;
import com.nosleepdrive.nosleepdrivebackend.company.repository.CompanyRepository;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final Hash hash;
    private final static long expireTime = 3600;

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

    public String login(CompanyLoginRequestDto request) {
        Company target = companyRepository.findById(request.getId())
                .orElseThrow(()-> new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_INVALID_ID_OR_PASSWORD.getMessage()));

        boolean loginResult = hash.Match(request.getPassword(), target.getPassword());
        if (!loginResult) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_INVALID_ID_OR_PASSWORD.getMessage());
        }
        return Token.generateToken(target.getIdCompany().toString(), expireTime);
    }

    public Company authCompany(String token){
        Long uid = Token.verifyToken(token);

        return companyRepository.findById(uid)
                .orElseThrow(()->new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage()));
    }

    public void deleteCompany(Company company) {
        companyRepository.delete(company);
    }

    @Transactional()
    public void updateCompany(Company company, String password, String companyName, String businessNumber) {
        company.updateCompany(
                companyName
                , businessNumber
                , password!=null?hash.HashEncode(password):null);
    }
}
