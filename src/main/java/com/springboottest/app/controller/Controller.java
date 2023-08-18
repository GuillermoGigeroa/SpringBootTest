package com.springboottest.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.springboottest.app.db.PostgreDBConnector;
import com.springboottest.app.entities.Usuario;
import com.springboottest.app.utils.Utils;

@RestController
public class Controller {
	@Autowired
	private PostgreDBConnector dbConnector;
	private ModelAndView MAV = new ModelAndView("index");
	private CommandHandler commandHandler = new CommandHandler();
	private String logs;
	
	@GetMapping("/")
	public ModelAndView index() {
		logs = "Sistema activo y funcionando correctamente. Para ejecutar comandos ingrese a /ejecutar/{comandos}";
		MAV.addObject("logs", logs);
		return MAV;
	}
	
	@GetMapping("/test")
	public String test() {
		Utils.logger("Usuarios encontrados con findAll():");
		for (Usuario usuario : dbConnector.findAll()) {
			Utils.logger(usuario.toString());
		}

		Usuario usuario = dbConnector.findById(1);
		Utils.logger("Usuario encontrado con findById(1):");
		Utils.logger(usuario != null ? usuario.toString() : "usuario: null");

		Utils.logger("Usuario encontrado con findByEmail(\"guille@gmail.com\"):");
		usuario = dbConnector.findByEmail("test@gmail.com");
		Utils.logger(usuario != null ? usuario.toString() : "usuario: null");
		return "Prueba ejecutada.";
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
