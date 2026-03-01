package com.apurv.canineAi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin(origins = "${client.url}")
public class CanineAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanineAiApplication.class, args);
	}

}
