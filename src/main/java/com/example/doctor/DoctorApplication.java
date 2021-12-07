package com.example.doctor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication
@EnableEurekaClient
public class DoctorApplication {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    LettuceConnectionFactory jedisConnectionFactory(){
        return new LettuceConnectionFactory();
    }
    @Bean
    RedisTemplate redisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }
    public static void main(String[] args) {
        SpringApplication.run(DoctorApplication.class, args);
    }
}
