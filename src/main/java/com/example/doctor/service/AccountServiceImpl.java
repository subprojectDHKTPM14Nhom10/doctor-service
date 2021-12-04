package com.example.doctor.service;

import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Account;
import com.example.doctor.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.saveAndFlush(account);
    }
    @Override
    public UserPrincipal findByUsername(String username) {
        Account doctor = accountRepository.findByUsername(username);
        UserPrincipal userPrincipal = new UserPrincipal();

        if (null != doctor) {
            Set<String> authorities = new HashSet<>();
            if (null != doctor.getRoles())

                doctor.getRoles().forEach(r -> {
                    authorities.add(r.getRoleKey());
                    r.getPermissions().forEach(
                            p -> authorities.add(p.getPermissionKey()));
                });
            userPrincipal.setUserId(doctor.getId());
            userPrincipal.setUsername(doctor.getUsername());
            userPrincipal.setPassword(doctor.getPassword());
            userPrincipal.setAuthorities(authorities);
        }
        return userPrincipal;
    }
}
