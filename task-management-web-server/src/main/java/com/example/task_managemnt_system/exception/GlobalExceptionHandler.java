package com.example.task_managemnt_system.exception;

import com.example.api.model.ErrorResponse;
import com.example.domain.exception.EmailAlreadyExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleException(EmailAlreadyExistException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(OffsetDateTime.now());
        return ResponseEntity.status(409).body(errorResponse);
    }

}
