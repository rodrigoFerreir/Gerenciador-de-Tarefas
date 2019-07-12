package com.ferreira.rodrigo.project.gerenciador.tarefas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ferreira.rodrigo.project.gerenciador.tarefas.models.Usuario;
import com.ferreira.rodrigo.project.gerenciador.tarefas.repository.RepositoryUsuario;

@Service
public class ServiceUsuario {
	
	@Autowired
	private RepositoryUsuario repositorioUsuario;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; //metodo para criptografar senha de usuario
	
	public Usuario encontrarPorEmail(String email) {
		return repositorioUsuario.findByEmail(email);
	}
	
	public void salvar(Usuario usuario) {
		usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		repositorioUsuario.save(usuario);
	}
}
