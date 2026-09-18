package com.example.scalablereadapis.web;

import com.example.scalablereadapis.dto.ErrorResponse;
import com.example.scalablereadapis.exception.PostNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.Instant;
import java.util.LinkedHashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PostNotFoundException.class)
    ResponseEntity<ErrorResponse> notFound(PostNotFoundException ex, HttpServletRequest req) { return response(HttpStatus.NOT_FOUND, ex.getMessage(), req, null); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var errors = new LinkedHashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST, "Request validation failed", req, errors);
    }
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, ConstraintViolationException.class, IllegalArgumentException.class})
    ResponseEntity<ErrorResponse> badRequest(Exception ex, HttpServletRequest req) { return response(HttpStatus.BAD_REQUEST, ex.getMessage(), req, null); }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> general(Exception ex, HttpServletRequest req) { return response(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", req, null); }
    private ResponseEntity<ErrorResponse> response(HttpStatus status, String message, HttpServletRequest req, java.util.Map<String, String> errors) {
        return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, req.getRequestURI(), errors));
    }
}
