package com.springboottest.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.aws.controller.Controller;

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
	public String sqsCommand(@PathVariable String[] commands) {
		try {
			Controller controller = new Controller();
			return controller.executeCommand(commands);
		}
		catch (Exception e) {
			return e.toString();
		}
	}
	
	@GetMapping("/test")
	public String test() {
		return "Nuevo test Guille";
	}

}
