package com.example.appointmentservice.service;

import com.example.appointmentservice.dto.DoctorDTO;
import com.example.appointmentservice.dto.PatientDTO;
import com.example.appointmentservice.entity.Appointment;
import com.example.appointmentservice.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Appointment createAppointment(Appointment appointment) {

        try {
            // Kiểm tra bệnh nhân
            PatientDTO patient = restTemplate.getForObject(
                    "http://PATIENT-SERVICE/api/v1/patients/" +
                            appointment.getPatientId(),
                    PatientDTO.class);

            if (patient == null) {
                throw new RuntimeException("Patient not found");
            }

            // Kiểm tra bác sĩ
            DoctorDTO doctor = restTemplate.getForObject(
                    "http://DOCTOR-SERVICE/api/v1/doctors/" +
                            appointment.getDoctorId(),
                    DoctorDTO.class);

            if (doctor == null) {
                throw new RuntimeException("Doctor not found");
            }

        } catch (Exception e) {
            throw new RuntimeException("Patient hoặc Doctor không tồn tại");
        }

        appointment.setStatus("PENDING");

        return appointmentRepository.save(appointment);
    }
}