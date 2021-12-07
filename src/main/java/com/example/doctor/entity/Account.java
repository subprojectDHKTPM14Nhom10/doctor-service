package com.example.doctor.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_account")
@Getter
@Setter
public class Account extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "t_account_role", joinColumns = {@JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles = new HashSet<>();
}
