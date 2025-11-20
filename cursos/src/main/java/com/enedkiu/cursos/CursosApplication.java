package com.enedkiu.cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CursosApplication {	
	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "prod");
		SpringApplication.run(CursosApplication.class, args);
	}

}
