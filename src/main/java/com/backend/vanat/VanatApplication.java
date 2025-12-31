package com.backend.vanat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VanatApplication {

	public static void main(String[] args) {
		System.out.println("DB URL is: " + System.getenv("DB_URL"));
		SpringApplication.run(VanatApplication.class, args);
	}

}
