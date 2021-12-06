package com.example.doctor.service;

import com.example.doctor.VO.ResponseTemplateVO;
import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Doctor;

import java.util.List;

public interface DoctorService {
    Doctor createUser(Doctor user);
//    Doctor findByNameDoctor(String username);
    ResponseTemplateVO getDepartmentWithDoctor(Long userId);
    List<Doctor> getAllDoctor();
    Doctor updateDoctor(Doctor inv, Long id);
    void deleteDoctor(Long id);
    List<ResponseTemplateVO> getAllDoctorWithDepartment();
    Doctor findDoctorById(Long id);
}
