package com.yourcompany.rates.api.controller.advice;

import com.yourcompany.rates.api.controller.admin.CurrencyController;
import com.yourcompany.rates.api.dto.ApiResponse;
import com.yourcompany.rates.common.exception.DomainException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.net.ConnectException;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = Logger.getLogger(CurrencyController.class.getName());

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomain(
        DomainException ex
    ) {
        log.severe("Request failed with DomainException: " + ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiResponse<>(false, null, ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleIntegrity(
        DataIntegrityViolationException ex
    ) {
        log.severe("Request failed with data integrity violation: " + ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ApiResponse<>(false, null, "Data integrity violation: " + ex.getMessage()));
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleDbConnection(
        DataAccessResourceFailureException ex
    ) {
        log.severe("Request failed as database is unavailable: " + ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(new ApiResponse<>(false, null, "Database unavailable"));
    }

    @ExceptionHandler({ IOException.class, ConnectException.class })
    public ResponseEntity<ApiResponse<Void>> handleNetwork(Exception ex) {
        log.severe("Request failed with network error: " + ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.BAD_GATEWAY)
            .body(new ApiResponse<>(false, null, "Network error"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleOther(Exception ex) {
        log.severe("Request failed with unexpected error: " + ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse<>(false, null, "Unexpected error: " + ex.getMessage()));
    }
}

