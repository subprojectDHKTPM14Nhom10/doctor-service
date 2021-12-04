package com.example.doctor.service;

import com.example.doctor.VO.ResponseTemplateVO;
import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Doctor;

public interface DoctorService {
    Doctor createUser(Doctor user);
//    Doctor findByNameDoctor(String username);
    ResponseTemplateVO getDepartmentWithDoctor(Long userId);
}
