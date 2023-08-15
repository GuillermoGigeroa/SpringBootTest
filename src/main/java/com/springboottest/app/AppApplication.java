package com.springboottest.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.springboottest.controller.CommandHandler;
import com.springboottest.utils.Utils;

@RestController
@SpringBootApplication
public class AppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
	
	@GetMapping("/")
	public ModelAndView index() {
		ModelAndView MAV = new ModelAndView("index");
		MAV.addObject("link","test");
		return MAV;
	}
	
	@GetMapping("/test")
	public String home() {
		return Utils.logger("Sistema activo y funcionando correctamente. Para ejecutar comandos ingrese a /ejecutar/{comandos}");
	}
	
	@GetMapping("/ejecutar/{comandos}")
	public String ejecutarComandos(@PathVariable String[] comandos) {
		try {
			CommandHandler commandHandler = new CommandHandler();
			return Utils.logger(commandHandler.executeCommand(comandos));
		}
		catch (Exception e) {
			return Utils.logger(e.toString()+ ":\n" +e.getMessage().toString());
		}
	}
	
}
