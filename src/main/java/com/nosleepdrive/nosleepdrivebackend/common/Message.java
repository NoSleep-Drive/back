package com.nosleepdrive.nosleepdrivebackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message{
    SIGNUP_SUCCESS("회원 가입이 완료되었습니다."),
    LOGIN_SUCCESS("로그인 성공."),
    GET_COMPANY_DATA_SUCCESS("회원 정보 조회 성공."),
    DELETE_COMPANY_SUCCESS("회원 탈퇴가 완료되었습니다."),
    PATCH_COMPANY_SUCCESS("회원 정보가 수정되었습니다."),
    ERR_SIGNUP_DUPLICATION_ID("이미 존재하는 ID입니다."),
    ERR_INVALID_INPUT("입력 형식이 올바르지 않습니다."),
    ERR_NULL_INPUT("입력값이 누락되었습니다."),
    ERR_INVALID_ID_OR_PASSWORD("ID 또는 비밀번호가 올바르지 않습니다."),
    ERR_IN_TOKEN("토큰 관련 메소드 실행 도중 에러가 발생했습니다."),
    ERR_VERIFY_TOKEN("인증 정보가 유효하지 않습니다."),
    ERR_SQL_DATA_INTEGRITY_VIOLATION("데이터 무결성 위반."),
    ERR_SQL_NOT_FOUND("데이터 찾기 실패."),
    ERR_SQL_DEPLICATION("이미 존재하는 값입니다."),
    ERR_SQL_FK("존재하지 않는 외래키 참조입니다."),

    CREATE_VEHICLE_SUCCESS("차량이 등록되었습니다."),
    DELETE_VEHICLE_SUCCESS("차량 등록이 삭제되었습니다."),
    UPDATE_VEHICLE_SUCCESS("차량 정보가 수정되었습니다."),
    UPDATE_VEHICLE_STATUS_SUCCESS("장치 상태가 업데이트되었습니다."),
    GET_VEHICLES_LIST("차량 목록 조회 성공."),
    GET_VEHICLES_COUNT_SUCCESS("등록 차량 수 조회 성공"),
    GET_ABNORMAL_VEHICLES_COUNT_SUCCESS("센서 이상 차량 수 조회 성공"),
    RENT_SUCCESS("차량 렌트를 시작했습니다."),
    RETURN_SUCCESS("차량 반납이 완료되었습니다."),
    ERR_DEPLICATION_VEHICLE("이미 등록된 차량입니다."),
    ERR_NOT_FOUND_VEHICLE("해당 차량을 찾을 수 없습니다."),
    ERR_FORBIDDEN("해당 요청에 대한 권한이 없습니다."),
    ERR_ALREADY_RENT("이미 렌트 중인 차량입니다."),
    ERR_NOT_RENT("렌트 중이 아닌 차량입니다.");

    private final String message;
}
