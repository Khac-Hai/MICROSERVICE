package com.example.appointmentservice.controller;

import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Appointment appointment = appointmentService.getAppointmentById(id);
        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> getAppointmentWithDoctorDetails(@PathVariable Long id) {
        return appointmentService.getAppointmentWithDoctorDetails(id);
    }

    @GetMapping("/{id}/patient-details")
    public ResponseEntity<?> getAppointmentWithPatientDetails(@PathVariable Long id) {
        return appointmentService.getAppointmentWithPatientDetails(id);
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(
            @RequestParam Long doctorId,
            @RequestParam Long patientId,
            @RequestParam(required = false) String appointmentTime) {
        LocalDateTime time = (appointmentTime != null)
                ? LocalDateTime.parse(appointmentTime)
                : LocalDateTime.now().plusDays(1);
        Appointment appointment = appointmentService.createAppointment(doctorId, patientId, time);
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        return appointmentService.deleteAppointment(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
