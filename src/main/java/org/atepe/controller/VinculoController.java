package org.atepe.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.atepe.model.Vinculo;
import org.atepe.repository.VinculoRepository;
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
public class VinculoController {

	@Autowired
	private VinculoRepository vr;

	@GetMapping(value = "/cadastrarVinculo")
	public String Form(Vinculo vinculo, Model model) {
		List<String> msg = new ArrayList<String>();
		model.addAttribute("msg", msg);
		Iterable<Vinculo> vinculos = vr.findAll();
		model.addAttribute("lista_vinculos", vinculos);
		return "vinculo/formVinculo";
	}

	@GetMapping(value = "/cancelarVinculo")
	public String Cancelar(Vinculo vinculo) {
		return "redirect:/cadastrarVinculo";
	}

	@RequestMapping(value = "/salvarVinculo", method = RequestMethod.POST)
	public ModelAndView salvarVinculo(@Valid Vinculo vinculo, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView("/vinculo/formVinculo");
		List<String> msg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			Iterable<Vinculo> vinculos = vr.findAll();
			mv.addObject("lista_vinculos", vinculos);
			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}
			mv.addObject("msg", msg);
			return mv;
		}
		vr.save(vinculo);
		Iterable<Vinculo> vinculos = vr.findAll();
		mv.addObject("lista_vinculos", vinculos);
		mv.addObject("vinculo", new Vinculo());
		msg.add("Registro salvo com sucesso...");
		mv.addObject("msg", msg);
		return mv;

	}

	@RequestMapping("/vinculos")
	public ModelAndView ListaVinculos(Vinculo vinculo) {
		ModelAndView mv = new ModelAndView("/vinculo/listaVinculo");
		Iterable<Vinculo> vinculos = vr.findAll();
		mv.addObject("vinculos", vinculos);
		return mv;
	}

	@PostMapping("**/pesquisarVinculos")
	public ModelAndView pesquisarVinculo(@RequestParam("nomePesquisa") String nomeCompleto, Vinculo vinculo) {
		ModelAndView modelAndView = new ModelAndView("vinculo/formVinculo");
		modelAndView.addObject("vinculo", new Vinculo());
		return modelAndView;

	}

	@RequestMapping("/deletarVinculo/{idVinculo}")
	public ModelAndView deletarVinculo(@PathVariable("idVinculo") Integer codVinculo, Vinculo vinculo) {
		List<String> msg = new ArrayList<String>();
		System.out.println("testeeeee");
		ModelAndView model = new ModelAndView("/vinculo/formVinculo");
		Vinculo dep = vr.findByCodVinculo(codVinculo);
		vr.delete(dep);
		Iterable<Vinculo> vinculos = vr.findAll();
		model.addObject("lista_vinculos", vinculos);
		msg.add("Registro excluido com sucesso...");
		model.addObject("msg", msg);
		return model;

	}

	@GetMapping("/editarVinculo/{idVinculo}")
	public String editarVinculo(@PathVariable("idVinculo") Integer codVinculo, Model model) {
		Vinculo dep = vr.findByCodVinculo(codVinculo);
		model.addAttribute("vinculo", dep);
		Iterable<Vinculo> vinculos = vr.findAll();
		model.addAttribute("lista_vinculos", vinculos);
		return "vinculo/formVinculo";

	}

}
