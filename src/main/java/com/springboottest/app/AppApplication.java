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
	private String logs = "Sistema activo y funcionando correctamente.";

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
	
	@GetMapping("/")
	public ModelAndView index() {
		return MAV;
	}
	
	@GetMapping("/test")
	public ModelAndView test() {
		this.logs = "Sistema activo y funcionando correctamente. Para ejecutar comandos ingrese a /ejecutar/{comandos}";
		this.MAV.addObject("logs",this.logs);
		return MAV;
	}
	
	@GetMapping("/ejecutar/{comandos}")
	public ModelAndView ejecutarComandos(@PathVariable String[] comandos) {
		try {
			CommandHandler commandHandler = new CommandHandler();
			this.logs = Utils.logger(commandHandler.executeCommand(comandos));
		}
		catch (Exception e) {
			this.logs = Utils.logger(e.toString()+ ":\n" +e.getMessage().toString());
		}
		this.MAV.addObject("logs",this.logs);
		return MAV;
	}
	
	@ModelAttribute("logs")
	public String getLogs() {
	    return this.logs;
	}
	
}
