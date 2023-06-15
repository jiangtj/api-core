package com.jiangtj.utils.apicore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ApiCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCoreApplication.class, args);
	}

}
