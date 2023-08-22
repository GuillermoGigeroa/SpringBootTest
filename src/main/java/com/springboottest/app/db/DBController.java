package com.springboottest.app.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springboottest.app.entities.Usuario;

@Component
public class DBController implements IDBController {

	@Autowired
	private PostgreDBConnector dbConnector;
	
	@Override
	public List<Usuario> listarUsuarios() {
		List<Usuario> lista = new ArrayList<Usuario>();
		dbConnector.findAll().forEach(usuario -> lista.add(usuario));
		return lista;
	}

}
