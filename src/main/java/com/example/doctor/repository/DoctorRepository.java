package com.example.doctor.repository;

import com.example.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Doctor, Long> {
    Doctor findByUsername(String username);
}
