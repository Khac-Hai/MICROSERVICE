package com.example.doctorservice.service;

import com.example.doctorservice.model.Doctor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DoctorService {

    private final Map<Long, Doctor> store = new ConcurrentHashMap<>();

    public DoctorService() {
        store.put(1L, new Doctor(1L, "Dr. Alice Smith", "Cardiology", true));
        store.put(2L, new Doctor(2L, "Dr. Bob Jones", "Neurology", true));
        store.put(3L, new Doctor(3L, "Dr. Carol White", "Pediatrics", false));
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(store.values());
    }

    public Doctor getDoctorById(Long id) {
        return store.get(id);
    }
}
