package com.springboottest.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.test.aws.controller.Controller;

@RestController
@SpringBootApplication
public class AppApplication {

	@RequestMapping("/")
    String home() {
        return "Prueba de SpringBoot de Guille.";
    }

	@GetMapping("/sqs")
	String sqsGet() {
		try {
			Controller controller = new Controller();
			controller.testSQSConnection();
		}
		catch (Exception e) {
			return e.toString();
		}
		return "Consulta de SQS realizada correctamente.";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
