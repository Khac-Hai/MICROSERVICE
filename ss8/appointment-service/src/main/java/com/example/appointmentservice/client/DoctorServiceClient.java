package com.example.appointmentservice.client;

import com.example.appointmentservice.model.ApiResponseError;
import com.example.appointmentservice.model.Doctor;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DoctorServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(DoctorServiceClient.class);
    private static final String CB_NAME = "doctorServiceCB";

    private final RestClient restClient;

    public DoctorServiceClient(
            RestClient.Builder restClientBuilder,
            @Value("${doctor-service.url:http://doctor-service}") String doctorServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(doctorServiceUrl).build();
    }

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "getDoctorFallback")
    public ResponseEntity<?> getDoctorById(Long doctorId) {
        logger.debug("Calling doctor-service GET /doctors/{}", doctorId);
        Doctor doctor = restClient.get()
                .uri("/doctors/{id}", doctorId)
                .retrieve()
                .body(Doctor.class);
        return ResponseEntity.ok(doctor);
    }

    @CircuitBreaker(name = CB_NAME, fallbackMethod = "isDoctorAvailableFallback")
    public boolean isDoctorAvailable(Long doctorId) {
        logger.debug("Calling doctor-service to check availability for doctorId={}", doctorId);
        Doctor doctor = restClient.get()
                .uri("/doctors/{id}", doctorId)
                .retrieve()
                .body(Doctor.class);
        return doctor != null && doctor.isAvailable();
    }

    public ResponseEntity<?> getDoctorFallback(Long doctorId, Exception e) {
        logger.error("Circuit breaker fallback [getDoctorById] doctorId={} reason={}",
                doctorId, e.getMessage(), e);
        ApiResponseError error = new ApiResponseError(
                false,
                503,
                "Hiện tại không thể kiểm tra thông tin bác sĩ, vui lòng thử lại sau vài giây",
                null
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    public boolean isDoctorAvailableFallback(Long doctorId, Throwable t) {
        logger.warn("Circuit breaker fallback [isDoctorAvailable] doctorId={} reason={}",
                doctorId, t.getMessage());
        return false;
    }
}
