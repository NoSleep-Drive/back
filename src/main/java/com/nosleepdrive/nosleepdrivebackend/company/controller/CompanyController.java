package com.nosleepdrive.nosleepdrivebackend.company.controller;

import com.nosleepdrive.nosleepdrivebackend.common.SimpleResponse;
import com.nosleepdrive.nosleepdrivebackend.company.dto.CompanySignUpRequestDto;
import com.nosleepdrive.nosleepdrivebackend.company.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<SimpleResponse> signup(@RequestBody CompanySignUpRequestDto request) {
        try {
            companyService.signup(request);

            SimpleResponse response = new SimpleResponse(
                    HttpStatus.CREATED.value(),
                    "회원 가입이 완료되었습니다."
            );

            return ResponseEntity.ok(response);
        }
        catch (ResponseStatusException ex) {
            SimpleResponse response = new SimpleResponse(
                    ex.getStatusCode().value(),
                    ex.getReason()
            );

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
