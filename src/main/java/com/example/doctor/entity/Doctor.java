package com.example.doctor.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_doctor")
@Getter
@Setter
public class Doctor extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -291484714475192602L;

    @Column(columnDefinition = "nvarchar(255)")
    private String nameDoctor;

    private int age;

    private String email;

    private String phone;

    private Long departmentId;

    @Override
    public String toString() {
        return "Doctor{" +
                "nameDoctor='" + nameDoctor + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", departmentId=" + departmentId +
                '}';
    }
}
