package com.yourcompany.rates.api.dto;

public record ApiResponse<T>(
    boolean success,
    T data,
    String message
) {}

