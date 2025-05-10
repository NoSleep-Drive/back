package com.nosleepdrive.nosleepdrivebackend.company.controller;

import com.nosleepdrive.nosleepdrivebackend.common.CustomError;
import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.common.SimpleResponse;
import com.nosleepdrive.nosleepdrivebackend.company.dto.*;
import com.nosleepdrive.nosleepdrivebackend.company.repository.entity.Company;
import com.nosleepdrive.nosleepdrivebackend.company.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/me")
    public ResponseEntity<SimpleResponse<CompanyDataDto>> getCompanyData(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany =  companyService.authCompany(token);

        SimpleResponse<CompanyDataDto> response = SimpleResponse.withData(
                Message.GET_COMPANY_DATA_SUCCESS.getMessage(),
                CompanyDataDto.of(curCompany)
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }


    @PostMapping("/signup")
    public ResponseEntity<SimpleResponse<?>> signup(@Valid @RequestBody CompanySignUpRequestDto request) {
        companyService.signup(request);

        SimpleResponse<?> response = SimpleResponse.withoutData(
                Message.SIGNUP_SUCCESS.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<CompanyLoginResponseDto> login(@Valid @RequestBody CompanyLoginRequestDto request){
        String token = companyService.login(request);

        CompanyLoginResponseDto response = new CompanyLoginResponseDto(
                Message.LOGIN_SUCCESS.getMessage(),
                token
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @DeleteMapping("/me")
    public ResponseEntity<SimpleResponse<?>> delete(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany =  companyService.authCompany(token);
        companyService.deleteCompany(curCompany);

        SimpleResponse<?> response = SimpleResponse.withoutData(
                Message.DELETE_COMPANY_SUCCESS.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }

    @PatchMapping("/me")
    public ResponseEntity<SimpleResponse<CompanyDataDto>> changeCompanyData(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody CompanyChangeRequestDto request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new CustomError(HttpStatus.UNAUTHORIZED.value(), Message.ERR_VERIFY_TOKEN.getMessage());
        }

        String token = authHeader.substring(7);
        Company curCompany =  companyService.authCompany(token);
        companyService.updateCompany(curCompany, request.getPassword(), request.getCompanyName(), request.getBusinessNumber());

        SimpleResponse<CompanyDataDto> response = SimpleResponse.withData(
                Message.PATCH_COMPANY_SUCCESS.getMessage(),
                CompanyDataDto.of(curCompany)
        );

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(response);
    }
}
