package com.springboottest.app.db;

import java.util.List;
import com.springboottest.app.entities.Usuario;

public interface IDBController {
	List<Usuario> listarUsuarios();
}
