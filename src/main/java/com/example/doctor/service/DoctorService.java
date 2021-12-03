package com.example.doctor.service;

import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Doctor;

public interface DoctorService {
    Doctor createUser(Doctor user);
    UserPrincipal findByUsername(String username);
}
