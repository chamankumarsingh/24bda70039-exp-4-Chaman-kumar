package com.example.scalablereadapis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ScalableReadApisApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScalableReadApisApplication.class, args);
    }
}
