package org.atepe.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.atepe.model.Parametro;
import org.atepe.model.TipoPagamento;
import org.atepe.repository.ParametroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ParametroController {

	@Autowired
	private ParametroRepository pr;

	@GetMapping(value = "/cadastrarParametro")
	public String Form(Parametro parametro, Model model) {
		Iterable<Parametro> parametros = pr.findAll();
		model.addAttribute("lista_parametros", parametros);
		model.addAttribute("listaPagam", Arrays.asList(TipoPagamento.values()));
		return "parametro/formParametro";
	}

	@RequestMapping(value = "/salvarParametro", method = RequestMethod.POST)
	public ModelAndView salvarParametro(@Valid Parametro parametro, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView("/parametro/formParametro");
		List<String> msg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			Iterable<Parametro> parametros = pr.findAll();
			mv.addObject("lista_parametros", parametros);
			mv.addObject("listaPagam", Arrays.asList(TipoPagamento.values()));
			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}
			mv.addObject("msg", msg);
			System.out.println("teve errossss");
			return mv;
		}
		pr.save(parametro);
		Iterable<Parametro> parametros = pr.findAll();
		mv.addObject("lista_parametros", parametros);
		mv.addObject("listaPagam", Arrays.asList(TipoPagamento.values()));
		mv.addObject("parametro", new Parametro());
		msg.add("Registro salvo com sucesso...");
		mv.addObject("msg", msg);
		return mv;

	}

	@RequestMapping("/parametros")
	public ModelAndView ListaParametros(Parametro parametro) {
		ModelAndView mv = new ModelAndView("/parametro/listaParametro");
		Iterable<Parametro> parametros = pr.findAll();
		mv.addObject("parametros", parametros);
		return mv;
	}

	@GetMapping("/editarParametro/{codParametro}")
	public String editarParametro(@PathVariable("codParametro") Long codParametro, Model model) {
		Optional<Parametro> con = pr.findById(codParametro);
		model.addAttribute("parametro", con);
		Iterable<Parametro> parametros = pr.findAll();
		model.addAttribute("lista_parametros", parametros);
		model.addAttribute("listaPagam", Arrays.asList(TipoPagamento.values()));
		return "parametro/formParametro";

	}

}
