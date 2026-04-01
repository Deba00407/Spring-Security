package com.debanjan.spring_security.controllers;

import com.debanjan.spring_security.exceptions.ConflictException;
import com.debanjan.spring_security.utils.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request){
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Invalid method arguments")
                .details(fieldErrors)
                .path(request.getRequestURI())
                .error(ex.getClass().getSimpleName())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictExceptions(ConflictException ex, HttpServletRequest request){
        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .error(ex.getClass().getSimpleName())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
