package com.devnus.belloga.point.common.exception;

import com.devnus.belloga.point.common.dto.CommonResponse;
import com.devnus.belloga.point.common.dto.ErrorResponse;
import com.devnus.belloga.point.common.exception.error.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Bean Validation에 실패했을 때, 에러메시지를 내보내기 위한 Exception Handler
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<CommonResponse> handleParamViolationException(BindException ex) {
        // 파라미터 validation에 걸렸을 경우
        ErrorCode errorCode = ErrorCode.REQUEST_PARAMETER_BIND_FAILED;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(InsufficientPointException.class)
    protected ResponseEntity<CommonResponse> handleInsufficientPointException(InsufficientPointException ex) {
        ErrorCode errorCode = ErrorCode.INSUFFICIENT_POINT;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(NotFoundTempPointException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundDataException(NotFoundTempPointException ex) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_TEMP_POINT;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(NotFoundLabelerIdException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundLabelerIdException(NotFoundLabelerIdException ex) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_LABELER;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(InsufficientStampException.class)
    protected ResponseEntity<CommonResponse> handleInsufficientStampException(InsufficientStampException ex) {
        ErrorCode errorCode = ErrorCode.INSUFFICIENT_STAMP;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(NotFoundGiftIdException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundGiftIdException(NotFoundGiftIdException ex) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND_GIFT;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();
        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}

