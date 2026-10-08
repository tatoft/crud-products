package com.example.products.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// Convierte las excepciones del service en respuestas HTTP claras, en un solo lugar.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Datos inválidos del cliente -> 400 con { "message": "..." }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
    }
}
