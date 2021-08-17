package com.example.learnspringsecurity.exception;

import javax.persistence.EntityNotFoundException;

public class EmailNotFoundException extends EntityNotFoundException {
    public EmailNotFoundException(String email) {
        super("Email " + email + " is not found");
    }
}
