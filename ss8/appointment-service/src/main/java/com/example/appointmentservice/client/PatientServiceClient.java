package com.example.appointmentservice.client;

import com.example.appointmentservice.model.ApiResponseError;
import com.example.appointmentservice.model.Patient;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
public class PatientServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(PatientServiceClient.class);
    private static final String RETRY_NAME = "patientRetry";

    private final RestClient restClient;

    public PatientServiceClient(
            RestClient.Builder restClientBuilder,
            @Value("${patient-service.url:http://patient-service}") String patientServiceUrl) {
        this.restClient = restClientBuilder.baseUrl(patientServiceUrl).build();
    }

    @Retry(name = RETRY_NAME, fallbackMethod = "getPatientFallback")
    public ResponseEntity<?> getPatientById(Long patientId) {
        logger.debug("Calling patient-service GET /patients/{} (attempt in progress)", patientId);
        try {
            Patient patient = restClient.get()
                    .uri("/patients/{id}", patientId)
                    .retrieve()
                    .body(Patient.class);
            return ResponseEntity.ok(patient);
        } catch (ResourceAccessException e) {
            logger.warn("Retry attempt failed for patientId={} reason={}", patientId, e.getMessage());
            throw e;
        }
    }

    public ResponseEntity<?> getPatientFallback(Long patientId, Exception e) {
        logger.error("Retry exhausted — executing fallback [getPatientById] patientId={} reason={}",
                patientId, e.getMessage(), e);
        ApiResponseError error = new ApiResponseError(
                false,
                503,
                "Hiện tại không thể kết nối tới Patient Service, vui lòng thử lại sau.",
                null
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }
}
