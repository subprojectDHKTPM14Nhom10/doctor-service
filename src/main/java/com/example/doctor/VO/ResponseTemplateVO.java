package com.example.doctor.VO;

import com.example.doctor.entity.Doctor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTemplateVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Doctor doctor;
    private Department department;
}
