package com.brain.servicedriveruser;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan("com.brain.servicedriveruser.mapper")
@EnableDiscoveryClient
public class ServcieDriverUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServcieDriverUserApplication.class, args);
    }
}
