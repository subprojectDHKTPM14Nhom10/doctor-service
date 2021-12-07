package com.example.doctor.service;
import com.example.doctor.VO.Department;
import com.example.doctor.VO.ResponseTemplateVO;
import com.example.doctor.authen.UserPrincipal;
import com.example.doctor.entity.Doctor;
import com.example.doctor.repository.DoctorRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DoctorRepository doctorRepository;

    private HashOperations hashOperations;

    private RedisTemplate redisTemplate;

    public DoctorServiceImpl(RedisTemplate redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
        this.redisTemplate = redisTemplate;
    }
    @Override
    @RateLimiter(name="basicExample")
    public Doctor createUser(Doctor doctor) {
        return doctorRepository.saveAndFlush(doctor);
    }

    @Override
    @Retry(name = "basic")
    public ResponseTemplateVO getDepartmentWithDoctor(Long userId) {
        ResponseTemplateVO vo = new ResponseTemplateVO();
        Doctor user = doctorRepository.findById(userId).get();
        vo.setDoctor(user);
        Department order =restTemplate.getForObject("http://localhost:9001/department/"+user.getDepartmentId(),Department.class);
        Department order2 =restTemplate.
        vo.setDepartment(order);
        return vo;
    }
}
