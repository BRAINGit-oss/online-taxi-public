package com.brain.servicepassengerusers;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.brain.servicepassengerusers.mapper")
public class ServicePassengerUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServicePassengerUsersApplication.class, args);
    }

}
