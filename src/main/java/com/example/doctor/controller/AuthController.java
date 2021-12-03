package com.example.doctor.controller;


import com.example.doctor.Util.JwtUtil;
import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Doctor;
import com.example.doctor.entity.Token;
import com.example.doctor.service.DoctorService;
import com.example.doctor.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@Controller
public class AuthController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenService tokenService;

    @RequestMapping("/")
    public String welcome(){
        return "index";
    }

    @PostMapping("/register")
    public Doctor register(@RequestBody Doctor doctor){
        doctor.setPassword(new BCryptPasswordEncoder().encode(doctor.getPassword()));
        return doctorService.createUser(doctor);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Doctor doctor){

        UserPrincipal userPrincipal =
                doctorService.findByUsername(doctor.getUsername());

        if (null == doctor || !new BCryptPasswordEncoder()
                .matches(doctor.getPassword(), userPrincipal.getPassword())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Account or password is not valid!");
        }

        Token token = new Token();
        token.setToken(jwtUtil.generateToken(userPrincipal));

        token.setTokenExpDate(jwtUtil.generateExpirationDate());
        token.setCreatedBy(userPrincipal.getUserId());
        tokenService.createToken(token);

        return ResponseEntity.ok(token.getToken());
    }
}
