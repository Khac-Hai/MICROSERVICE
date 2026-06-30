package com.example.doctorservice.controller;

import com.example.doctorservice.model.ApiResponseError;
import com.example.doctorservice.model.Doctor;
import com.example.doctorservice.service.DoctorService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping
    @RateLimiter(name = "searchDoctorLimit", fallbackMethod = "searchDoctorRateLimitFallback")
    public ResponseEntity<?> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        if (doctor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doctor);
    }

    public ResponseEntity<?> searchDoctorRateLimitFallback(Throwable t) {
        logger.warn("Rate limiter [searchDoctorLimit] rejected request — reason={}", t.getMessage());
        ApiResponseError error = new ApiResponseError(
                false,
                429,
                "Bạn đã gửi quá nhiều yêu cầu. Vui lòng thử lại sau 10 giây.",
                null
        );
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error);
    }
}
