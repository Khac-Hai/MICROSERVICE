package com.example.pharmacyservice;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@RefreshScope
@Service
public class InsuranceVerificationService {

    private static final Logger log = LoggerFactory.getLogger(InsuranceVerificationService.class);

    private final RestClient restClient;
    private final Duration insuranceTimeout;

    public InsuranceVerificationService(
            RestClient.Builder restClientBuilder,
            @Value("${insurance.timeout}") Duration insuranceTimeout) {
        this.restClient = restClientBuilder
                .baseUrl("http://insurance-service")
                .build();
        this.insuranceTimeout = insuranceTimeout;
    }

    @TimeLimiter(
            name = "insuranceTimeLimiter"
    )
    @Retry(
            name = "insuranceRetry"
    )
    @CircuitBreaker(
            name = "insuranceCB",
            fallbackMethod = "insuranceFallback"
    )
    public CompletableFuture<ResponseEntity<?>> verifyInsurance(InsuranceVerificationRequest request) {
        return CompletableFuture
                .supplyAsync(() -> restClient.post()
                        .uri("/api/v1/insurance/verify")
                        .body(request)
                        .retrieve()
                        .toEntity(Object.class))
                .orTimeout(insuranceTimeout.toMillis(), TimeUnit.MILLISECONDS);
    }

    public CompletableFuture<ResponseEntity<?>> insuranceFallback(
            InsuranceVerificationRequest request,
            Throwable throwable) {
        log.warn("Insurance verification fallback execution");
        log.warn("Original exception message: {}", throwable.getMessage());

        return CompletableFuture.completedFuture(ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponseError.serviceUnavailable(
                        "Kh\u00f4ng th\u1ec3 x\u00e1c th\u1ef1c b\u1ea3o hi\u1ec3m l\u00fac n\u00e0y. H\u1ec7 th\u1ed1ng s\u1ebd ti\u1ebfp t\u1ee5c b\u00e1n thu\u1ed1c theo gi\u00e1 ch\u01b0a chi\u1ebft kh\u1ea5u v\u00e0 x\u00e1c th\u1ef1c b\u1ea3o hi\u1ec3m sau.")));
    }

    public record InsuranceVerificationRequest(
            String insuranceNumber,
            String customerName,
            BigDecimal drugPrice) {
    }
}
