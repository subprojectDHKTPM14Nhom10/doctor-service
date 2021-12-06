package com.example.doctor.service;
import com.example.doctor.VO.Department;
import com.example.doctor.VO.ResponseTemplateVO;
import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Doctor;
import com.example.doctor.repository.DoctorNotFoundException;
import com.example.doctor.repository.DoctorRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    @RateLimiter(name="basicExample")
    public Doctor createUser(Doctor doctor) {
        Doctor doc = doctorRepository.findById(doctor.getId()).get();
        System.out.println("123456" + doc);
        if(doc.getId() != null){

            doc.setId(doc.getId());
            doc.setNameDoctor(doctor.getNameDoctor());
            doc.setAge(doctor.getAge());
            doc.setEmail(doctor.getEmail());
            doc.setPhone(doctor.getPhone());
            doc.setDepartmentId(doctor.getDepartmentId());
            return doctorRepository.save(doc);
        }
        return doctorRepository.save(doctor);
    }

    @Override
    @Retry(name = "basic")
    public ResponseTemplateVO getDepartmentWithDoctor(Long userId) {
        ResponseTemplateVO vo = new ResponseTemplateVO();
        Doctor user = doctorRepository.findById(userId).get();
        vo.setDoctor(user);
        Department order =restTemplate.getForObject("http://localhost:9001/department/"+user.getDepartmentId(),Department.class);
        vo.setDepartment(order);
        return vo;
    }

    @Cacheable(value="Doctor")
    public List<Doctor> getAllDoctor() {
        return doctorRepository.findAll();
    }

    @Override
    @Cacheable(value="Doctor")
    public List<ResponseTemplateVO> getAllDoctorWithDepartment() {

        List<ResponseTemplateVO> voList = new ArrayList<ResponseTemplateVO>();

        List<Doctor> doctors = getAllDoctor();
        for (Doctor d : doctors) {
            ResponseTemplateVO vo = new ResponseTemplateVO();
            Doctor user = doctorRepository.findById(d.getId()).get();
            vo.setDoctor(user);
            Department order =restTemplate.getForObject("http://localhost:9001/department/"+user.getDepartmentId(),Department.class);
            vo.setDepartment(order);
            voList.add(vo);
        }
        return voList;
    }

    @Override
    public Doctor findDoctorById(Long id) {
        return doctorRepository.findById(id).get();
    }

    @CachePut(value="Doctor", key="#id")
    public Doctor updateDoctor(Doctor inv, Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Invoice Not Found"));
        doctor.setNameDoctor(inv.getNameDoctor());
        doctor.setAge(inv.getAge());
        doctor.setEmail(inv.getEmail());
        doctor.setPhone(inv.getPhone());
        doctor.setDepartmentId(inv.getDepartmentId());
        return doctorRepository.save(doctor);
    }

    @CacheEvict(value="Doctor", key="#id")
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException("Invoice Not Found"));
        doctorRepository.delete(doctor);
    }
}
