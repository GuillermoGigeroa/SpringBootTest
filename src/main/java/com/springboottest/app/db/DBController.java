package com.springboottest.app.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springboottest.app.entities.Usuario;
import com.springboottest.app.utils.Utils;

@Component
public class DBController implements IDBController {

	@Autowired
	private PostgreDBConnector dbConnector;
	
	@Override
	public List<Usuario> listarUsuarios() {
		List<Usuario> response = new ArrayList<Usuario>();
		dbConnector.findAll().forEach(usuario -> response.add(usuario));
		return response;
	}

	@Override
	public Usuario buscarUsuarioPorEmail(String email) {
		Usuario response = dbConnector.findByEmail(email);
		return response != null ? response : new Usuario();
	}

	@Override
	public Usuario buscarUsuarioPorID(Long id) {
		Optional<Usuario> response = dbConnector.findById(id);
		return response.isEmpty() ? response.get() : new Usuario();
	}

	@Override
	public String insertarUsuario(Usuario usuario) {
		dbConnector.save(usuario);
		return Utils.writeMessage("Se ha creado un nuevo usuario: "+usuario.toString());
	}
	
	@Override
	public String insertarUsuario(String nombre, String email) {
		Usuario usuario = new Usuario(nombre, email);
		dbConnector.save(usuario);
		return Utils.writeMessage("Se ha creado un nuevo usuario: "+usuario.toString());
	}

	@Override
	public String eliminarUsuarioPorEmail(String email) {
		Usuario usuario = dbConnector.findByEmail(email);
		dbConnector.delete(usuario);
		return Utils.writeMessage("Se ha eliminado usuario: "+usuario.toString());
	}
}
