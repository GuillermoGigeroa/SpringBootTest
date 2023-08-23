package com.springboottest.app.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import com.springboottest.app.db.DBController;
import com.springboottest.app.entities.Usuario;
import com.springboottest.app.utils.Utils;

@RestController
public class Controller {
	@Autowired
	private CommandHandler commandHandler;
	
	@Autowired
	private DBController dbController;

	private ModelAndView MAV = new ModelAndView("index");
	private String logs;
	private Boolean console = false;
	
	@GetMapping("/")
	public ModelAndView index() {
		logs = "Sistema activo y funcionando correctamente. Para ejecutar comandos ingrese a /ejecutar/{comandos}";
		MAV.addObject("logs", logs);
		MAV.addObject("console", console);
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
		MAV.addObject("console", console);
		return MAV;
	}
	
	@GetMapping("/test/consola")
	public ModelAndView enableConsole() {
		console = !console;
		logs = console ? "Se ha desplegado la consola python de prueba." : "Se ha ocultado la consola python de prueba.";
		MAV.addObject("logs", logs);
		MAV.addObject("console", console);
		return MAV;
	}
	
	@GetMapping("/getUsersData")
	public ResponseEntity<List<Usuario>> getUsers() {
		List<Usuario> listaUsuarios = dbController.listarUsuarios();
		System.out.println(listaUsuarios);
		return new ResponseEntity<>(listaUsuarios, HttpStatus.OK);
	}
	
	@GetMapping("usuarios/listar")
	public ModelAndView insertUser() {
		logs = dbController.listarUsuarios().toString();
		MAV.addObject("logs", logs);
		MAV.addObject("console", console);
		return MAV;
	}
	
	@GetMapping("usuarios/insertar/{nombre}/{email}")
	public ModelAndView insertUser(@PathVariable String nombre, @PathVariable String email) {
		logs = dbController.insertarUsuario(nombre, email);
		MAV.addObject("logs", logs);
		MAV.addObject("console", console);
		return MAV;
	}

	@GetMapping("usuarios/eliminarPorEmail/{email}")
	public ModelAndView deleteUser(@PathVariable String email) {
		logs = dbController.eliminarUsuarioPorEmail(email);
		MAV.addObject("logs", logs);
		MAV.addObject("console", console);
		return MAV;
	}
	
	@ModelAttribute("logs")
	public String getLogs() {
	    return logs;
	}
	
}
