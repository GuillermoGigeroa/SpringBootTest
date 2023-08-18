package com.springboottest.db;

import org.springframework.data.repository.CrudRepository;
import com.springboottest.entities.Usuario;

public interface PostgreDBConnector extends CrudRepository<Usuario, Long> {
	Usuario findByEmail(String email);
	Usuario findById(long id);
}
