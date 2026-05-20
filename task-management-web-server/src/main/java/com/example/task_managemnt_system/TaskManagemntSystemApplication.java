package com.example.task_managemnt_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
		"com.example.domain"
})
@EntityScan(basePackages = {
		"com.example.domain"
})
public class TaskManagemntSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskManagemntSystemApplication.class, args);
	}
}
