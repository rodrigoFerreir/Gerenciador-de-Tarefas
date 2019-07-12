package com.ferreira.rodrigo.project.gerenciador.tarefas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.ferreira.rodrigo.project.gerenciador.tarefas.models.Usuario;

@Repository
public interface RepositoryUsuario extends JpaRepository<Usuario, Long> {
	
	Usuario findByEmail(String email);
}
