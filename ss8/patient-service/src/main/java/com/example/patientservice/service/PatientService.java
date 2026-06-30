package com.example.patientservice.service;

import com.example.patientservice.model.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PatientService {

    private final Map<Long, Patient> store = new ConcurrentHashMap<>();

    public PatientService() {
        store.put(1L, new Patient(1L, "Nguyen Van A", "vana@example.com", "0901000001"));
        store.put(2L, new Patient(2L, "Tran Thi B", "thib@example.com", "0901000002"));
        store.put(3L, new Patient(3L, "Le Van C", "vanc@example.com", "0901000003"));
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(store.values());
    }

    public Patient getPatientById(Long id) {
        return store.get(id);
    }
}
