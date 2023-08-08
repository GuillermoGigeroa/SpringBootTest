package com.springboottest.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
	
	@RequestMapping("/")
	public String home() {
		return "Sistema activo y funcionando correctamente.";
	}
	
	@GetMapping("/sqs/{command}")
	public String sqsCommand(@PathVariable String command) {
		return "sqs command: "+command;
	}
	
	@GetMapping("/test")
	public String test() {
		return "Nuevo test Guille";
	}

}
