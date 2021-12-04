package com.example.doctor.service;
import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Doctor;
import com.example.doctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Doctor createUser(Doctor doctor) {
        return doctorRepository.saveAndFlush(doctor);
    }

    @Override
    public Doctor findByNameDoctor(String username) {
        return doctorRepository.findByNameDoctor(username);
    }
}
