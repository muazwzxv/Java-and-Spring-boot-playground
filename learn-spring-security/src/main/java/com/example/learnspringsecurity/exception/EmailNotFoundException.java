package com.example.learnspringsecurity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailNotFoundException extends EntityNotFoundException {
    public EmailNotFoundException(String email) {
        super("Email " + email + " is not found");
    }
}
