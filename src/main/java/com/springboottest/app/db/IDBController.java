package com.springboottest.app.db;

import java.util.List;
import com.springboottest.app.entities.Usuario;

public interface IDBController {
	List<Usuario> listarUsuarios();
	Usuario buscarUsuarioPorEmail(String email);
	Usuario buscarUsuarioPorID(Long id);
	String insertarUsuario(Usuario usuario);
	String insertarUsuario(String nombre, String email);
	String eliminarUsuarioPorEmail(String email);
}
