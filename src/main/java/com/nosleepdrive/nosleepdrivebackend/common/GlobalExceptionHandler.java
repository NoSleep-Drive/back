package com.nosleepdrive.nosleepdrivebackend.common;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomError.class)
    public ResponseEntity<SimpleResponse> handleCustomException(CustomError ex) {
        SimpleResponse response = new SimpleResponse(ex.getStatus(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SimpleResponse> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SimpleResponse(HttpStatus.BAD_REQUEST.value(), Message.ERR_INVALID_INPUT.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<SimpleResponse> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SimpleResponse(HttpStatus.BAD_REQUEST.value(), Message.ERR_NULL_INPUT.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<SimpleResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SimpleResponse(HttpStatus.BAD_REQUEST.value(), Message.ERR_SQL_DATA_INTEGRITY_VIOLATION.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<SimpleResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SimpleResponse(HttpStatus.BAD_REQUEST.value(), Message.ERR_SQL_NOT_FOUND.getMessage()));
    }
}
