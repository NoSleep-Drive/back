package com.nosleepdrive.nosleepdrivebackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message{
    SIGNUP_SUCCESS("회원 가입이 완료되었습니다."),
    LOGIN_SUCCESS("로그인 성공."),
    GET_COMPANY_DATA_SUCCESS("회원 정보 조회 성공."),
    ERR_SIGNUP_DUPLICATION_ID("이미 존재하는 ID입니다."),
    ERR_INVALID_INPUT("입력 형식이 올바르지 않습니다."),
    ERR_NULL_INPUT("입력값이 누락되었습니다."),
    ERR_INVALID_ID_OR_PASSWORD("ID 또는 비밀번호가 올바르지 않습니다."),
    ERR_IN_TOKEN("토큰 관련 메소드 실행 도중 에러가 발생했습니다."),
    ERR_VERIFY_TOKEN("인증 정보가 유효하지 않습니다.");

    private final String message;
}
