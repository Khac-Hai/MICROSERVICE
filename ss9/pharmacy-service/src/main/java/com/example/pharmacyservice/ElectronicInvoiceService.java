package com.example.pharmacyservice;

import java.math.BigDecimal;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ElectronicInvoiceService {

    private static final Logger log = LoggerFactory.getLogger(ElectronicInvoiceService.class);

    private final RestClient restClient;

    public ElectronicInvoiceService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://invoice-service")
                .build();
    }

    @Retry(
            name = "invoiceRetry",
            fallbackMethod = "invoiceFallback"
    )
    @RateLimiter(
            name = "invoiceRateLimiter",
            fallbackMethod = "invoiceFallback"
    )
    public ResponseEntity<?> createElectronicInvoice(ElectronicInvoiceRequest request) {
        return restClient.post()
                .uri("/api/v1/electronic-invoices")
                .body(request)
                .retrieve()
                .toEntity(Object.class);
    }

    public ResponseEntity<?> invoiceFallback(ElectronicInvoiceRequest request, Throwable throwable) {
        log.warn("Electronic invoice fallback execution");
        log.warn("Original exception message: {}", throwable.getMessage());

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponseError.serviceUnavailable(
                        "Kh\u00f4ng th\u1ec3 xu\u1ea5t h\u00f3a \u0111\u01a1n \u0111i\u1ec7n t\u1eed t\u1ea1i th\u1eddi \u0111i\u1ec3m n\u00e0y. Y\u00eau c\u1ea7u \u0111\u00e3 \u0111\u01b0\u1ee3c x\u1eed l\u00fd an to\u00e0n, vui l\u00f2ng th\u1eed l\u1ea1i sau."));
    }

    public record ElectronicInvoiceRequest(
            String invoiceNumber,
            BigDecimal totalAmount,
            String customerTaxCode) {
    }
}
