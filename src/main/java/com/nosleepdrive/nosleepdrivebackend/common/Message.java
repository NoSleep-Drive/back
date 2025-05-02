package com.nosleepdrive.nosleepdrivebackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message{
    SIGNUP_SUCCESS("회원 가입이 완료되었습니다."),
    ERR_SIGNUP_DUPLICATION_ID("이미 존재하는 ID입니다."),
    ERR_INVALID_INPUT("입력 형식이 올바르지 않습니다."),
    ERR_NULL_INPUT("입력값이 누락되었습니다.");

    private final String message;
}
