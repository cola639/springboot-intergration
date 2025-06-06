package org.spring.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.spring.springboot")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}