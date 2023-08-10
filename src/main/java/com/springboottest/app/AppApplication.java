package com.springboottest.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.test.aws.controller.CommandHandler;
import com.test.aws.utils.Utils;

@RestController
@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
	
	@RequestMapping("/")
	public String home() {
		return Utils.logger("Sistema activo y funcionando correctamente.");
	}
	
	@RequestMapping("/web")
	public ModelAndView index() {
		ModelAndView MAV = new ModelAndView("index");
		Utils.logger("Sistema activo y funcionando correctamente.");
		MAV.addObject("link","");
		return MAV;
	}
	
	@GetMapping("/execute/{commands}")
	public String sqsCommand(@PathVariable String[] commands) {
		try {
			CommandHandler commandHandler = new CommandHandler();
			return Utils.logger(commandHandler.executeCommand(commands));
		}
		catch (Exception e) {
			return Utils.logger(e.toString()+ ":\n" +e.getMessage().toString());
		}
	}
	
	@GetMapping("/test")
	public String test() {
		StringBuilder builder = new StringBuilder();
		builder.append("Sistema activo y funcionando correctamente.")
		.append('\n')
		.append("Se testea builder.");
		return Utils.logger(builder.toString());
	}

}
