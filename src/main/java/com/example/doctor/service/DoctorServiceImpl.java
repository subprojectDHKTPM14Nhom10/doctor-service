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
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
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
        if(doctor.getId() != null){
            Doctor doc = doctorRepository.findById(doctor.getId()).get();
            doc.setId(doc.getId());
            doc.setNameDoctor(doctor.getNameDoctor());
            doc.setAge(doctor.getAge());
            doc.setEmail(doctor.getEmail());
            doc.setPhone(doctor.getPhone());
            doc.setDepartmentId(doctor.getDepartmentId());
            return doctorRepository.save(doc);
        }
        else{
            Doctor d = new Doctor();
            d.setNameDoctor(doctor.getNameDoctor());
            d.setAge(doctor.getAge());
            d.setEmail(doctor.getEmail());
            d.setPhone(doctor.getPhone());
            d.setDepartmentId(doctor.getDepartmentId());
            return doctorRepository.save(d);
        }
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
    @Cacheable(value="Doctor", key = "#Id")
    public Doctor findDoctorById(Long Id) {
        return doctorRepository.findById(Id).get();
    }

    @CachePut(value="Doctor", key="#Id")
    public Doctor updateDoctor(Doctor inv, Long Id) {
        Doctor doctor = doctorRepository.findById(Id)
                .orElseThrow(() -> new DoctorNotFoundException("Invoice Not Found"));
        doctor.setNameDoctor(inv.getNameDoctor());
        doctor.setAge(inv.getAge());
        doctor.setEmail(inv.getEmail());
        doctor.setPhone(inv.getPhone());
        doctor.setDepartmentId(inv.getDepartmentId());
        return doctorRepository.save(doctor);
    }

    @CacheEvict(value="Doctor", key="#Id")
    public void deleteDoctor(Long Id) {
        Doctor doctor = doctorRepository.findById(Id)
                .orElseThrow(() -> new DoctorNotFoundException("Invoice Not Found"));
        doctorRepository.delete(doctor);
    }

    @Override
    @Cacheable(value="Department")
    public List<Object> getAllDepartment() {
        RestTemplate restTemplate = new RestTemplate();
        List<Object> list = new ArrayList<Object>();
        Object[] departments =
                restTemplate.getForObject("http://localhost:9001/department/alldepartment", Object[].class);

        for (Object o : departments) {
            list.add(o);
        }
        return list;
    }
    @Override
    @RateLimiter(name="basicExample")
    public Department createDepartment(Department department) {
        System.out.println("kkkkkkkkkkkkkk" + department);
        Department d =
                restTemplate.postForObject("http://localhost:9001/department/", department, Department.class);
        return d;
    }
    @Override
    public Department findDepartmentById(Long id) {
        Department department =
                restTemplate.getForObject("http://localhost:9001/department/" + id, Department.class);
        return department;
    }
    @Override
    public void deleteDepartment(Long id) {

                restTemplate.delete("http://localhost:9001/department/delete/" + id);
    }
}
