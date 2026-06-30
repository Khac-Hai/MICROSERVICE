package com.example.pharmacyservice;

import org.springframework.http.HttpStatus;

public record ApiResponseError(
        int status,
        String error,
        String message,
        Object localInventory) {

    public static ApiResponseError serviceUnavailable(String message) {
        return serviceUnavailable(message, null);
    }

    public static ApiResponseError serviceUnavailable(String message, Object localInventory) {
        return new ApiResponseError(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(),
                message,
                localInventory);
    }
}
