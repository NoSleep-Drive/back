package com.nosleepdrive.nosleepdrivebackend.common;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomError.class)
    public ResponseEntity<SimpleResponse<?>> handleCustomException(CustomError ex, HttpServletResponse res) {
        try {
            if (ex.getMessage().compareTo(String.valueOf(Message.ERR_INVALID_VIDEO)) == 0) {
                res.reset();
                res.getOutputStream().close();
            }
            SimpleResponse<?> response = SimpleResponse.withoutData(ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatus()));
        }
        catch (Exception e) {
            SimpleResponse<?> response = SimpleResponse.withoutData(ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatus()));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SimpleResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(SimpleResponse.withoutData(Message.ERR_INVALID_INPUT.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<SimpleResponse<?>> handleNullPointerException(NullPointerException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(SimpleResponse.withoutData(Message.ERR_NULL_INPUT.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<SimpleResponse<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        if (message.contains("UK_")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(SimpleResponse.withoutData(Message.ERR_SQL_DEPLICATION.getMessage()));
        } else if (message.contains("FK_")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SimpleResponse.withoutData(Message.ERR_SQL_FK.getMessage()));
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(SimpleResponse.withoutData(Message.ERR_SQL_DATA_INTEGRITY_VIOLATION.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<SimpleResponse<?>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(SimpleResponse.withoutData(Message.ERR_SQL_NOT_FOUND.getMessage()));
    }
}
