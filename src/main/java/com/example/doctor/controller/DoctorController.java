package com.example.doctor.controller;

import com.example.doctor.VO.ResponseTemplateVO;
import com.example.doctor.entity.Doctor;
import com.example.doctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public Doctor saveDoctor(@RequestBody Doctor doctor){
        return doctorService.createUser(doctor);
    }

    @GetMapping("/{id}")
    public ResponseTemplateVO getUserWithOrder(@PathVariable("id") Long userId){
        return doctorService.getDepartmentWithDoctor(userId);
    }
}
