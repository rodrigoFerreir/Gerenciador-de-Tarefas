package com.ferreira.rodrigo.project.gerenciador.tarefas.controllers;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ferreira.rodrigo.project.gerenciador.tarefas.models.Tarefa;
import com.ferreira.rodrigo.project.gerenciador.tarefas.models.Usuario;
import com.ferreira.rodrigo.project.gerenciador.tarefas.repository.RepositoryTarefa;
import com.ferreira.rodrigo.project.gerenciador.tarefas.services.ServiceUsuario;

@Controller
@RequestMapping("/tarefas")
public class TarefasController {

	@Autowired
	private RepositoryTarefa repositoryTarefa;
	
	@Autowired
	private ServiceUsuario servicoUsuario;

	@GetMapping("/listar")
	public ModelAndView Listar(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("tarefas/listar");
		String emailUsuario = request.getUserPrincipal().getName();
		mv.addObject("tarefas", repositoryTarefa.carregarTarefasPorUsuario(emailUsuario));
		return mv;
	}

	@GetMapping("/inserir")
	public ModelAndView inserir() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("tarefas/inserir");
		mv.addObject("tarefa", new Tarefa());
		return mv;
	}
	
	@PostMapping("/inserir")
	public ModelAndView inserir(@Valid Tarefa tarefa, BindingResult result, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		if(tarefa.getDataExpiracao() == null) {
			result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoInvalida", "A data de expiração é obrigatória");

		}else {
			if(tarefa.getDataExpiracao().before(new Date())) {
				result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoInvalida", "A data de expiração não pode ser anterior à data atual");
			}
		}
		if(result.hasErrors()) {
			mv.setViewName("tarefas/inserir");
			mv.addObject(tarefa);
		}else {
			String emailUsuario = request.getUserPrincipal().getName();
			Usuario usuarioLogado = servicoUsuario.encontrarPorEmail(emailUsuario);
			tarefa.setUsuario(usuarioLogado);//vinculando usuario logado a uma tarefa
			
			repositoryTarefa.save(tarefa);
			System.out.println("Tarefa inserida com sucesso!");
			mv.setViewName("redirect:/tarefas/listar");
			
		}
		return mv;

		// verifica, salva e redireciona para a pagina listar ou para pagina de nova
		// tarefa.
	}
	
	@GetMapping("/alterar/{id}")
	public ModelAndView alterar(@PathVariable("id") Long id) {
		ModelAndView mv = new ModelAndView();
		Tarefa tarefa = repositoryTarefa.getOne(id);
		mv.addObject("tarefa", tarefa);
		 mv.setViewName("tarefas/alterar");
		return mv;
	}
	
	@PostMapping("/alterar")
	public ModelAndView alterar(@Valid Tarefa tarefa, BindingResult result) {
		ModelAndView mv = new ModelAndView();
		
		if(tarefa.getDataExpiracao() == null) {
			result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoInvalida", "A data de expiração é obrigatória");

		}else {
			if(tarefa.getDataExpiracao().before(new Date())) {
				result.rejectValue("dataExpiracao", "tarefa.dataExpiracaoInvalida", "A data de expiração não pode ser anterior à data atual");
			}
		}
		if(result.hasErrors()) {
			mv.setViewName("tarefas/alterar");
			mv.addObject(tarefa);
		}else {
			mv.setViewName("redirect:/tarefas/listar");
			repositoryTarefa.save(tarefa);
			System.out.println("Tarefa alterada com sucesso!");
		}
		return mv;

		// verifica, salva e redireciona para a pagina listar ou para pagina de nova
		// tarefa.
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id) {
		repositoryTarefa.deleteById(id);
		return "redirect:/tarefas/listar";
	}
	
	@GetMapping("/concluir/{id}")
	public String concluir(@PathVariable("id") Long id) {
		Tarefa tarefa = repositoryTarefa.getOne(id);
		tarefa.setConcluida(true);
		repositoryTarefa.save(tarefa);
		return "redirect:/tarefas/listar";
	}
}
