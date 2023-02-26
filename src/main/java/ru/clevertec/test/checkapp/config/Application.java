package ru.clevertec.test.checkapp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.clevertec.test.checkapp")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
