package ru.clevertec.test.checkapp.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "ru.clevertec.test")
@EnableJpaRepositories(basePackages = "ru.clevertec.test")
public class RepositoryConfig {}
