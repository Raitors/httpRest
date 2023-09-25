package com.example.httpRest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class HttpRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpRestApplication.class, args);
	}

}
