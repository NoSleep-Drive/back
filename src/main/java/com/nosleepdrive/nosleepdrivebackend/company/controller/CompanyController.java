package com.nosleepdrive.nosleepdrivebackend.company.controller;

import com.nosleepdrive.nosleepdrivebackend.common.Message;
import com.nosleepdrive.nosleepdrivebackend.common.SimpleResponse;
import com.nosleepdrive.nosleepdrivebackend.company.dto.CompanyLoginRequestDto;
import com.nosleepdrive.nosleepdrivebackend.company.dto.CompanyLoginResponseDto;
import com.nosleepdrive.nosleepdrivebackend.company.dto.CompanySignUpRequestDto;
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

    @GetMapping("/")
    public String hello() {
        return "Hello, NoSleepDrive! it's a company!";
    }


    @PostMapping("/signup")
    public ResponseEntity<SimpleResponse> signup(@Valid @RequestBody CompanySignUpRequestDto request) {
        companyService.signup(request);

        int customCode = HttpStatus.CREATED.value();
        SimpleResponse response = new SimpleResponse(
                customCode,
                Message.SIGNUP_SUCCESS.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.valueOf(customCode))
                .body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<CompanyLoginResponseDto> login(@Valid @RequestBody CompanyLoginRequestDto request){
        String token = companyService.login(request);

        int customCode = HttpStatus.OK.value();
        CompanyLoginResponseDto response = new CompanyLoginResponseDto(
                customCode,
                Message.LOGIN_SUCCESS.getMessage(),
                token
        );

        return ResponseEntity
                .status(HttpStatus.valueOf(customCode))
                .body(response);
    }
}
