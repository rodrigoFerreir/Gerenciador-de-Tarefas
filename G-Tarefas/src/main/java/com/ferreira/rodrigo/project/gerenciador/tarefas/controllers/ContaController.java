package com.ferreira.rodrigo.project.gerenciador.tarefas.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ferreira.rodrigo.project.gerenciador.tarefas.models.Usuario;
import com.ferreira.rodrigo.project.gerenciador.tarefas.services.ServiceUsuario;

@Controller
public class ContaController {
	
	@Autowired
	private ServiceUsuario ServicoUsuario;
	
	@GetMapping("/login")
	public String login() {
		return "conta/login";
	}
	
	@GetMapping("/registration")
	public ModelAndView registration() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("conta/registrar");
		mv.addObject("usuario", new Usuario());
		return mv;
	}
	
	@PostMapping("/registration")
	public ModelAndView registration(@Valid Usuario usuario, BindingResult result) {
		ModelAndView mv = new ModelAndView();
		Usuario usr = ServicoUsuario.encontrarPorEmail(usuario.getEmail());
		
		if(usr != null) {
			result.rejectValue("email", "", "Usuario ja cadastrado");
		}
		if(result.hasErrors()) {
			mv.setViewName("conta/registrar");
			mv.addObject("usuario", usuario);
		}else {
			ServicoUsuario.salvar(usuario);
			mv.setViewName("redirect:/login");
			System.out.println("Login realizado com sucesso!");
		}
		return mv;
	}//verificando email pelo Service Usuario.
}
