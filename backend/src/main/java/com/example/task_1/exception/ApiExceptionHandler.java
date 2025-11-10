package com.example.task_1.exception;

import com.example.task_1.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@ControllerAdvice // global exception handler for all controllers
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // validation errors
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("validation-error"));
    }

    @ExceptionHandler(IllegalArgumentException.class) // invalid input or bad arguments
    public ResponseEntity<ErrorResponse> handleIllegal(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler({ NoSuchElementException.class, DataIntegrityViolationException.class }) // DB lookup or constraint errors
    public ResponseEntity<ErrorResponse> handleNotFoundOrIntegrity(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("invalid-request"));
    }

    @ExceptionHandler(ForbiddenException.class) // access denied
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class) // resource not found
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class) // fallback handler
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("server-error"));
    }
}
