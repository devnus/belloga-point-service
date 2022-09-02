package com.devnus.belloga.point.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    REQUEST_PARAMETER_BIND_FAILED(HttpStatus.BAD_REQUEST, "REQ_001", "PARAMETER_BIND_FAILED"),
    NOT_FOUND_LABELER(HttpStatus.NOT_FOUND, "USER_001", "해당하는 라벨러가 없음"),
    INSUFFICIENT_POINT(HttpStatus.FORBIDDEN, "POINT_001", "포인트가 부족함"),
    NOT_FOUND_TEMP_POINT(HttpStatus.NOT_FOUND, "POINT_001", "해당 라벨링UUID로 임시 포인트를 찾을 수 없음");

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message){
        this.status = status;
        this.message = message;
        this.code = code;
    }
}

