package com.example.doctor.service;

import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Account;

public interface AccountService {
    Account createAccount(Account account);
    UserPrincipal findByUsername(String username);
}
