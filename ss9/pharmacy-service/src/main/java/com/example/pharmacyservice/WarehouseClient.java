package com.example.pharmacyservice;

import java.util.List;
import java.util.Map;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WarehouseClient {

    private static final Logger log = LoggerFactory.getLogger(WarehouseClient.class);

    private final RestClient restClient;

    public WarehouseClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://warehouse-service")
                .build();
    }

    @CircuitBreaker(
            name = "warehouseCB",
            fallbackMethod = "checkWarehouseFallback"
    )
    public ResponseEntity<?> getWarehouseAvailability() {
        return restClient.get()
                .uri("/api/v1/warehouse/availability")
                .retrieve()
                .toEntity(Object.class);
    }

    public ResponseEntity<?> checkWarehouseFallback(Throwable throwable) {
        log.warn("Circuit Breaker fallback execution");
        log.warn("Original exception message: {}", throwable.getMessage());

        Map<String, Object> localInventory = Map.of(
                "source", "LOCAL_INVENTORY",
                "items", List.of()
        );

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponseError.serviceUnavailable(
                        "Kh\u00f4ng th\u1ec3 k\u1ebft n\u1ed1i kho t\u1ed5ng. H\u1ec7 th\u1ed1ng s\u1ebd s\u1eed d\u1ee5ng d\u1eef li\u1ec7u t\u1ed3n kho c\u1ee5c b\u1ed9 \u0111\u1ec3 ti\u1ebfp t\u1ee5c giao d\u1ecbch.",
                        localInventory));
    }
}
