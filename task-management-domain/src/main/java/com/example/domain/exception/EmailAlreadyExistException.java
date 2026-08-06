package com.example.domain.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String userNotFound) {
        super(userNotFound) ;
    }
}
