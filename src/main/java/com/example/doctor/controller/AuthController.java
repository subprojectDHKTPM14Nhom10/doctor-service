package com.example.doctor.controller;


import com.example.doctor.Util.JwtUtil;
import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Account;
import com.example.doctor.entity.Doctor;
import com.example.doctor.entity.Token;
import com.example.doctor.service.AccountService;
import com.example.doctor.service.DoctorService;
import com.example.doctor.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
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
        return "index";
    }

    @PostMapping("/register")
    @ResponseBody
    public Account register(@RequestBody Account account){
        account.setPassword(new BCryptPasswordEncoder().encode(account.getPassword()));
        return accountService.createAccount(account);
     //hien day
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account){

        UserPrincipal userPrincipal =
                accountService.findByUsername(account.getUsername());

        if (null == account || !new BCryptPasswordEncoder()
                .matches(account.getPassword(), userPrincipal.getPassword())) {

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
