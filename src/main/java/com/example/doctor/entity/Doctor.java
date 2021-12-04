package com.example.doctor.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_doctor")
@Getter
@Setter
public class Doctor extends BaseEntity {

    private String nameDoctor;

    private int age;

    private String email;

    private String phone;

    private Long departmentId;

}
