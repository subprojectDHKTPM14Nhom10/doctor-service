package com.example.doctor.controller;


import com.example.doctor.util.JwtUtil;
import com.example.doctor.VO.Department;
import com.example.doctor.VO.ResponseTemplateVO;
import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Account;
import com.example.doctor.entity.Doctor;
import com.example.doctor.entity.Token;
import com.example.doctor.service.AccountService;
import com.example.doctor.service.DoctorService;
import com.example.doctor.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @RequestMapping("/")
    public String welcome(){
        return "DangNhap";
    }

    @GetMapping("/register")
    public String create(Model model){
        model.addAttribute("account", new Account());
        return "/Dangki";
    }

    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public String register(Model model, @ModelAttribute Account account, RedirectAttributes redirect){
        account.setPassword(new BCryptPasswordEncoder().encode(account.getPassword()));
        Account acc = accountService.createAccount(account);
        redirect.addFlashAttribute("success", "Register account successfully!");
        return "redirect:/";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute Account account){
        UserPrincipal userPrincipal =
                accountService.findByUsername(account.getUsername());

        if (null == account || !new BCryptPasswordEncoder()
                .matches(account.getPassword(), userPrincipal.getPassword())) {
            return "/DangNhap";
        }
        Token token = new Token();
        token.setToken(jwtUtil.generateToken(userPrincipal));

        token.setTokenExpDate(jwtUtil.generateExpirationDate());
        token.setCreatedBy(userPrincipal.getUserId());
        tokenService.createToken(token);
        ResponseEntity.ok(token.getToken());
        System.out.println("Token dc sinh ra : "+token.getToken());
        return "redirect:/alldoctor";
    }


    @RequestMapping(value = "/alldoctor", method = RequestMethod.GET)
    public String getAllInvoices(Model model){
        model.addAttribute("account", new Account());
        List<ResponseTemplateVO> list = doctorService.getAllDoctorWithDepartment();
        model.addAttribute("listVO", list);
        return "/Home_BacSi";
    }

    @GetMapping(value = "/showFormForAdd")
    public String showFormForAdd(Model model){
        Doctor doctor = new Doctor();
        model.addAttribute("doctor", doctor);
        return "/Form_BacSi";
    }

    @PostMapping("/save")
    public String saveDoctor(@ModelAttribute("doctor") Doctor doctor){
        doctorService.createUser(doctor);
        return "redirect:/alldoctor";
    }

    @GetMapping("/showFormForUpdate")
    public String updateDoctor(@RequestParam("doctorId") Long id, Model model) {
        Doctor doctor = doctorService.findDoctorById(id);
        model.addAttribute("doctor", doctor);
        return "/Form_BacSi";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("doctorId") Long id){
        doctorService.deleteDoctor(id);
        return "redirect:/alldoctor";
    }

    @RequestMapping(value = "/allkhoa", method = RequestMethod.GET)
    public String getAllDepartment(Model model){
        List<Object> list = doctorService.getAllDepartment();
        model.addAttribute("listKhoa", list);
        return "/Home_Khoa";
    }

    @GetMapping(value = "/showFormForAddDepartment")
    public String showFormForAddDepartment(Model model){
        Department department = new Department();
        model.addAttribute("department", department);
        return "/Form_Khoa";
    }

    @PostMapping("/saveDepartment")
    public String saveDepartment(@ModelAttribute("department") Department department){
        doctorService.createDepartment(department);
        return "redirect:/allkhoa";
    }

    @GetMapping("/showFormForUpdateDepartment")
    public String updateDepartment(@RequestParam("departmentId") Long id, Model model) {
        Department department = doctorService.findDepartmentById(id);
        model.addAttribute("department", department);
        return "/Form_Khoa";
    }

    @GetMapping("/deleteDepartment")
    public String deleteDepartment(@RequestParam("departmentId") Long id){
        doctorService.deleteDepartment(id);
        return "redirect:/allkhoa";
    }


}
