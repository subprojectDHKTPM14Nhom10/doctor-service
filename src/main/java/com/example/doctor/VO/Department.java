package com.example.doctor.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long Id;
    private String DepartmentName;
}
