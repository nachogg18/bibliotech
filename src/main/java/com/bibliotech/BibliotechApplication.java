package com.bibliotech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotechApplication {

	public static void main(String[] args) {
		SpringApplication application =  new SpringApplication(BibliotechApplication.class);
		application.run(args);
	}
}
