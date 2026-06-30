package com.example.appointmentservice.service;

import com.example.appointmentservice.client.DoctorServiceClient;
import com.example.appointmentservice.client.PatientServiceClient;
import com.example.appointmentservice.model.Appointment;
import com.example.appointmentservice.model.Doctor;
import com.example.appointmentservice.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private final DoctorServiceClient doctorServiceClient;
    private final PatientServiceClient patientServiceClient;
    private final Map<Long, Appointment> store = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong(1);

    public AppointmentService(DoctorServiceClient doctorServiceClient,
                               PatientServiceClient patientServiceClient) {
        this.doctorServiceClient = doctorServiceClient;
        this.patientServiceClient = patientServiceClient;
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(store.values());
    }

    public Appointment getAppointmentById(Long id) {
        return store.get(id);
    }

    public Appointment createAppointment(Long doctorId, Long patientId, LocalDateTime appointmentTime) {
        boolean available = doctorServiceClient.isDoctorAvailable(doctorId);
        String status = available ? "CONFIRMED" : "PENDING_DOCTOR_UNAVAILABLE";

        Appointment appointment = new Appointment(
                idSeq.getAndIncrement(),
                doctorId,
                patientId,
                appointmentTime,
                status
        );
        store.put(appointment.getId(), appointment);
        logger.info("Created appointment id={} doctorId={} status={}", appointment.getId(), doctorId, status);
        return appointment;
    }

    public ResponseEntity<?> getAppointmentWithDoctorDetails(Long appointmentId) {
        Appointment appointment = store.get(appointmentId);
        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }
        ResponseEntity<?> doctorResponse = doctorServiceClient.getDoctorById(appointment.getDoctorId());
        if (!doctorResponse.getStatusCode().is2xxSuccessful()) {
            return doctorResponse;
        }
        Doctor doctor = (Doctor) doctorResponse.getBody();
        if (doctor != null) {
            logger.info("Fetched doctor name='{}' for appointmentId={}", doctor.getName(), appointmentId);
        }
        return ResponseEntity.ok(appointment);
    }

    public ResponseEntity<?> getAppointmentWithPatientDetails(Long appointmentId) {
        Appointment appointment = store.get(appointmentId);
        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }
        ResponseEntity<?> patientResponse = patientServiceClient.getPatientById(appointment.getPatientId());
        if (!patientResponse.getStatusCode().is2xxSuccessful()) {
            return patientResponse;
        }
        Patient patient = (Patient) patientResponse.getBody();
        if (patient != null) {
            logger.info("Fetched patient name='{}' for appointmentId={}", patient.getName(), appointmentId);
        }
        return ResponseEntity.ok(appointment);
    }

    public boolean deleteAppointment(Long id) {
        return store.remove(id) != null;
    }
}
