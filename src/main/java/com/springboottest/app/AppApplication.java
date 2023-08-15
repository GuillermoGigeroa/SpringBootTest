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
	private ModelAndView MAV;

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}
	
	@GetMapping("/")
	public ModelAndView index() {
		MAV = new ModelAndView("index");
		return MAV;
	}
	
	@GetMapping("/test")
	public ModelAndView test() {
		MAV = new ModelAndView("test");
		MAV.addObject("test","Sistema activo y funcionando correctamente. Para ejecutar comandos ingrese a /ejecutar/{comandos}");
		return MAV;
	}
	
	@GetMapping("/ejecutar/{comandos}")
	public ModelAndView ejecutarComandos(@PathVariable String[] comandos) {
		String log = "Sistema activo y funcionando correctamente.";
		try {
			CommandHandler commandHandler = new CommandHandler();
			log = Utils.logger(commandHandler.executeCommand(comandos));
		}
		catch (Exception e) {
			log = Utils.logger(e.toString()+ ":\n" +e.getMessage().toString());
		}
		MAV = new ModelAndView("test");
		MAV.addObject("test", log);
		return MAV;
	}
	
}
