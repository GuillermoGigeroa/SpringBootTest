package com.springboottest.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.springboottest.controller.CommandHandler;
import com.springboottest.utils.Utils;

@RestController
@SpringBootApplication
public class AppApplication {
	private ModelAndView MAV = new ModelAndView("index");
	private CommandHandler commandHandler = new CommandHandler();
	private String logs = "Sistema activo y funcionando correctamente.";

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
	
	@GetMapping("/")
	public ModelAndView index() {
		logs = "Sistema activo y funcionando correctamente.";
		MAV.addObject("logs", logs);
		return MAV;
	}
	
	@GetMapping("/test")
	public ModelAndView test() {
		logs = "Sistema activo y funcionando correctamente. Para ejecutar comandos ingrese a /ejecutar/{comandos}";
		MAV.addObject("logs", logs);
		return MAV;
	}
	
	@GetMapping("/ejecutar/{comandos}")
	public ModelAndView ejecutarComandos(@PathVariable String[] comandos) {
		try {
			logs = Utils.logger(commandHandler.executeCommand(comandos));
		}
		catch (Exception e) {
			logs = Utils.logger(e.toString()+ ":\n" +e.getMessage().toString());
		}
		MAV.addObject("logs", logs);
		return MAV;
	}
	
	@ModelAttribute("logs")
	public String getLogs() {
	    return logs;
	}
	
}
