package com.example.doctor.service;
import com.example.doctor.VO.Department;
import com.example.doctor.VO.ResponseTemplateVO;
import com.example.doctor.entity.Doctor;
import com.example.doctor.repository.DoctorNotFoundException;
import com.example.doctor.repository.DoctorRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DoctorRepository doctorRepository;
    private HashOperations hashOperations;
    @Autowired
    private RedisTemplate redisTemplate;
    public DoctorServiceImpl(RedisTemplate redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
        this.redisTemplate = redisTemplate;
    }
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
            Doctor kq1 = doctorRepository.save(doc);
            ResponseTemplateVO vo1 = getDepartmentWithDoctor(kq1.getId());
            hashOperations.put("DOCTOR", vo1.getDoctor().getId(), vo1);
            return kq1;
        }
        else{
            Doctor d = new Doctor();
            d.setNameDoctor(doctor.getNameDoctor());
            d.setAge(doctor.getAge());
            d.setEmail(doctor.getEmail());
            d.setPhone(doctor.getPhone());
            d.setDepartmentId(doctor.getDepartmentId());
            Doctor kq2 = doctorRepository.save(d);
            ResponseTemplateVO vo2 = getDepartmentWithDoctor(kq2.getId());
            hashOperations.put("DOCTOR", vo2.getDoctor().getId(), vo2);
            return kq2;
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
    @Retry(name = "basic")
    public List<ResponseTemplateVO> getAllDoctorWithDepartment() {
        if(hashOperations.values("DOCTOR").size()>0){
            return hashOperations.values("DOCTOR");
        }
        List<ResponseTemplateVO> voList = new ArrayList<ResponseTemplateVO>();
        List<Doctor> doctors = getAllDoctor();
        for (Doctor d : doctors) {
            ResponseTemplateVO vo = new ResponseTemplateVO();
            Doctor user = doctorRepository.findById(d.getId()).get();
            vo.setDoctor(user);
            Department order =restTemplate.getForObject("http://localhost:9001/department/"+user.getDepartmentId(),Department.class);
            vo.setDepartment(order);
            //setOperations.add("DOCTOR", vo);
            hashOperations.put("DOCTOR", vo.getDoctor().getId(), vo);
            voList.add(vo);
        }
        return voList;
    }
    @Override
    @Retry(name = "basic")
    public Doctor findDoctorById(Long Id) {
        return doctorRepository.findById(Id).get();
    }
    public Doctor updateDoctor(Doctor inv, Long Id) {
        ResponseTemplateVO vo = getDepartmentWithDoctor(Id);
        hashOperations.put("DOCTOR", vo.getDoctor().getId(), vo);
        Doctor doctor = doctorRepository.findById(Id)
                .orElseThrow(() -> new DoctorNotFoundException("Invoice Not Found"));
        doctor.setNameDoctor(inv.getNameDoctor());
        doctor.setAge(inv.getAge());
        doctor.setEmail(inv.getEmail());
        doctor.setPhone(inv.getPhone());
        doctor.setDepartmentId(inv.getDepartmentId());
        return doctorRepository.save(doctor);
    }
    public void deleteDoctor(Long Id) {
        Doctor doctor = doctorRepository.findById(Id)
                .orElseThrow(() -> new DoctorNotFoundException("Invoice Not Found"));
        hashOperations.delete("DOCTOR", Id);
        doctorRepository.delete(doctor);
    }
    @Override
    @Retry(name = "basic")
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
        Department d =
                restTemplate.postForObject("http://localhost:9001/department/", department, Department.class);
        return d;
    }
    @Override
    @Retry(name = "basic")
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
