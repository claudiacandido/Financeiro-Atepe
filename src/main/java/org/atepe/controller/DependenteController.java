package org.atepe.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.atepe.model.Associado;
import org.atepe.model.Dependente;
import org.atepe.model.Vinculo;
import org.atepe.repository.AssociadoRepository;
import org.atepe.repository.DependenteRepository;
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
public class DependenteController {

	@Autowired
	private DependenteRepository dr;

	@Autowired
	private AssociadoRepository ar;
	
	@Autowired
	private VinculoRepository vr;
	
	private Long codAssoc;


	@GetMapping(value = "/cadastrarDependente/{codAssociado}")
	public String Form(@PathVariable("codAssociado") Long codigo, Dependente dependente, Model model) {
		Associado assoc = ar.findByCodAssociado(codigo);
		codAssoc = assoc.getCodAssociado();
		model.addAttribute("associado", assoc);
		Iterable<Dependente> dependentes = dr.findByAssociado(assoc);
		model.addAttribute("lista_dependentes", dependentes);
		Iterable<Vinculo> vinculos = vr.findAll();
		model.addAttribute("lista_vinculos", vinculos);
		
		return "dependente/formDependente";
	}

	@GetMapping(value = "/cancelarDependente")
	public String Cancelar(Dependente dependente) {
		return "redirect:/cadastrarDependente";
	}

	@RequestMapping(value = "/salvarDependente/{idAssociado}", method = RequestMethod.POST)
	public ModelAndView salvarDependente(@Valid Dependente dependente,
			@PathVariable("idAssociado") Long codAssociado, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView("/dependente/formDependente");
		Associado associado = ar.findByCodAssociado(codAssociado);
		dependente.setAssociado(associado);
		List<String> msg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			Iterable<Dependente> dependentes = dr.findAll();
			mv.addObject("lista_dependentes", dependentes);
			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}
			mv.addObject("msg", msg);
			return mv;
		}
		dr.save(dependente);
		mv.addObject("associado", associado);
		
		Iterable<Dependente> dependentes = dr.findByAssociado(associado);
		mv.addObject("lista_dependentes", dependentes);
		mv.addObject("dependente", new Dependente());
	
		Iterable<Vinculo> vinculos = vr.findAll();
		mv.addObject("lista_vinculos", vinculos);
		
	
		msg.add("Registro salvo com sucesso...");
		mv.addObject("msg", msg);
		return mv;

	}

	@RequestMapping("/deletarDependente/{idDependente}")
	public ModelAndView deletarDependente(@PathVariable("idDependente") Long codDependente, Dependente dependente) {
		List<String> msg = new ArrayList<String>();
		ModelAndView model = new ModelAndView("/dependente/formDependente");
		Dependente dep = dr.findByCodDependente(codDependente);
		dr.delete(dep);
		Associado associado = ar.findByCodAssociado(codAssoc);
		dependente.setAssociado(associado);
		model.addObject("associado", associado);
		Iterable<Dependente> dependentes = dr.findByAssociado(associado);
		model.addObject("lista_dependentes", dependentes);
		msg.add("Registro excluido com sucesso...");
		model.addObject("msg", msg);
		return model;

	}

	@GetMapping("/editarDependente/{idDependente}")
	public String editarDependente(@PathVariable("idDependente") Long codDependente, Model model) {
		Dependente dep = dr.findByCodDependente(codDependente);
		model.addAttribute("dependente", dep);
		Associado associado = ar.findByCodAssociado(codAssoc);
		dep.setAssociado(associado);
		model.addAttribute("associado", associado);
		Iterable<Dependente> dependentes = dr.findByAssociado(associado);
		model.addAttribute("lista_dependentes", dependentes);
		return "dependente/formDependente";

	}

}
