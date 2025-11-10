package com.example.task_1.exception;

// Thrown when a requested resource cannot be found
public class NotFoundException extends RuntimeException {
    public NotFoundException(String code) {
        super(code);
    }
}
