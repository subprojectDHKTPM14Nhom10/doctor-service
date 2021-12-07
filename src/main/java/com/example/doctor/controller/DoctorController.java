package com.example.doctor.controller;

import com.example.doctor.VO.ResponseTemplateVO;
import com.example.doctor.entity.Account;
import com.example.doctor.entity.Doctor;
import com.example.doctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;

//@RestController
@Controller
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

    @GetMapping("/alldoctor")
    public String getAllInvoices(Model model){
        List<Doctor> list = doctorService.getAllDoctor();
        model.addAttribute("doctors", list);
        model.addAttribute("account", new Account());
        return "/Home_BacSi";
    }
    @PutMapping("/update/{id}")
    public Doctor updateDoctor(@RequestBody Doctor inv, @PathVariable Long id) {
        return doctorService.updateDoctor(inv, id);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return "Employee with id: "+id+ " Deleted !";
    }





}
