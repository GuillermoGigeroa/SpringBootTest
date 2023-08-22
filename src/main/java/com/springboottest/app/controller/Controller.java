package com.springboottest.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.springboottest.app.utils.Utils;

@RestController
public class Controller {
	@Autowired
	private CommandHandler commandHandler;

	private ModelAndView MAV = new ModelAndView("index");
	private String logs;
	
	@GetMapping("/")
	public ModelAndView index() {
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
