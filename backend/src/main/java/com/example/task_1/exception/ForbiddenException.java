package com.example.task_1.exception;

// Thrown when a user tries to perform an action without proper permission
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
