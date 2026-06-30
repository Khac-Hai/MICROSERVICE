package com.example.pharmacyservice;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Resilience4jEventLogger {

    private static final Logger log = LoggerFactory.getLogger(Resilience4jEventLogger.class);

    public Resilience4jEventLogger(
            RetryRegistry retryRegistry,
            RateLimiterRegistry rateLimiterRegistry,
            TimeLimiterRegistry timeLimiterRegistry,
            CircuitBreakerRegistry circuitBreakerRegistry) {
        retryRegistry.retry("invoiceRetry")
                .getEventPublisher()
                .onRetry(event -> log.warn(
                        "Electronic invoice retry attempt {}",
                        event.getNumberOfRetryAttempts()));

        retryRegistry.retry("insuranceRetry")
                .getEventPublisher()
                .onRetry(event -> log.warn(
                        "Insurance verification retry attempt {}",
                        event.getNumberOfRetryAttempts()));

        rateLimiterRegistry.rateLimiter("invoiceRateLimiter")
                .getEventPublisher()
                .onFailure(event -> log.warn("Electronic invoice Rate Limiter rejection"));

        timeLimiterRegistry.timeLimiter("insuranceTimeLimiter")
                .getEventPublisher()
                .onTimeout(event -> log.warn("Insurance verification timeout event"));

        circuitBreakerRegistry.circuitBreaker("insuranceCB")
                .getEventPublisher()
                .onStateTransition(event -> log.warn(
                        "Insurance Circuit Breaker state change: {}",
                        event.getStateTransition()));
    }
}
