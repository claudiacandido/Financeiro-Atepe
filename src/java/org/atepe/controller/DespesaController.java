package org.atepe.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.atepe.model.Despesa;
import org.atepe.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DespesaController {

	@Autowired
	private DespesaRepository dr;

	@GetMapping(value = "/cadastrarDespesa")
	public String Form(Despesa despesa, Model model) {
		List<String> msg = new ArrayList<String>();
		model.addAttribute("msg", msg);
		Iterable<Despesa> despesas = dr.findAll();
		model.addAttribute("lista_despesas", despesas);
		return "despesa/formDespesa";
	}

	@GetMapping(value = "/cancelarDespesa")
	public String Cancelar(Despesa despesa) {
		return "redirect:/cadastrarDespesa";
	}

	@RequestMapping(value = "/salvarDespesa", method = RequestMethod.POST)
	public ModelAndView salvarDespesa(@Valid Despesa despesa, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView("/despesa/formDespesa");
		List<String> msg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			Iterable<Despesa> despesas = dr.findAll();
			mv.addObject("lista_despesas", despesas);
			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}
			mv.addObject("msg", msg);
			return mv;
		}
		dr.save(despesa);
		Iterable<Despesa> despesas = dr.findAll();
		mv.addObject("lista_despesas", despesas);
		mv.addObject("despesa", new Despesa());
		msg.add("Registro salvo com sucesso...");
		mv.addObject("msg", msg);
		return mv;

	}

	@RequestMapping("/despesas")
	public ModelAndView ListaDespesas(Despesa despesa) {
		ModelAndView mv = new ModelAndView("/despesa/listaDespesa");
		Iterable<Despesa> despesas = dr.findAll();
		mv.addObject("despesas", despesas);
		return mv;
	}

	@PostMapping("**/pesquisarDespesas")
	public ModelAndView pesquisarDespesa(@RequestParam("nomePesquisa") String nomeCompleto, Despesa despesa) {
		ModelAndView modelAndView = new ModelAndView("despesa/formDespesa");
		modelAndView.addObject("despesa", new Despesa());
		return modelAndView;

	}

	@RequestMapping("/deletarDespesa/{idDespesa}")
	public ModelAndView deletarDespesa(@PathVariable("idDespesa") Integer codDespesa, Despesa despesa) {
		List<String> msg = new ArrayList<String>();
		System.out.println("testeeeee");
		ModelAndView model = new ModelAndView("/despesa/formDespesa");
		Despesa dep = dr.findByCodDespesa(codDespesa);
		dr.delete(dep);
		Iterable<Despesa> despesas = dr.findAll();
		model.addObject("lista_despesas", despesas);
		msg.add("Registro excluido com sucesso...");
		model.addObject("msg", msg);
		return model;

	}

	@GetMapping("/editarDespesa/{idDespesa}")
	public String editarDespesa(@PathVariable("idDespesa") Integer codDespesa, Model model) {
		Despesa dep = dr.findByCodDespesa(codDespesa);
		model.addAttribute("despesa", dep);
		Iterable<Despesa> despesas = dr.findAll();
		model.addAttribute("lista_despesas", despesas);
		return "despesa/formDespesa";

	}

}
