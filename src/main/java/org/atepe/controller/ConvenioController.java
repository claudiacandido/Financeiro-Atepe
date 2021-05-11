package org.atepe.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.atepe.model.Convenio;
import org.atepe.model.Lancamento;
import org.atepe.model.TipoPagamento;
import org.atepe.repository.ConvenioRepository;
import org.atepe.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
public class ConvenioController {

	@Autowired
	private ConvenioRepository cr;
	
	@Autowired
	private LancamentoRepository lr;


	@GetMapping(value = "/cadastrarConvenio")
	public String Form(Convenio convenio, Model model) {
		List<String> msg = new ArrayList<String>();
		model.addAttribute("msg", msg);
		Iterable<Convenio> convenios = cr.findAll();
		model.addAttribute("lista_convenios", convenios);
		model.addAttribute("listaPagam",Arrays.asList(TipoPagamento.values()));
		return "convenio/formConvenio";
	}
	
	@GetMapping(value = "/cancelarConvenio")
	public String Cancelar(Convenio convenio) {
		return "redirect:/cadastrarConvenio";
	}

	@RequestMapping(value = "/salvarConvenio", method = RequestMethod.POST)
	public ModelAndView salvarConvenio(@Valid Convenio convenio, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView("/convenio/formConvenio");
		List<String> msg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			Iterable<Convenio> convenios = cr.findAll();
			mv.addObject("lista_convenios", convenios);
			mv.addObject("listaPagam",Arrays.asList(TipoPagamento.values()));
			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}
			mv.addObject("msg", msg);
			return mv;
		}
		cr.save(convenio);
		Iterable<Convenio> convenios = cr.findAll(Sort.by("razaoSocial"));
		mv.addObject("lista_convenios", convenios);
		mv.addObject("listaPagam",Arrays.asList(TipoPagamento.values()));
		mv.addObject("convenio", new Convenio());
		msg.add("Registro salvo com sucesso...");
		mv.addObject("msg", msg);
		return mv;

	}

	@RequestMapping("/convenios")
	public ModelAndView ListaConvenios(Convenio convenio) {
		ModelAndView mv = new ModelAndView("/convenio/listaConvenio");
		Iterable<Convenio> convenios = cr.findAll();
		mv.addObject("convenios", convenios);
		return mv;
	}

	
	@PostMapping("**/pesquisarConvenios")
	public ModelAndView pesquisarConvenio(@RequestParam("nomePesquisa") String nomeCompleto, Convenio convenio) {
		String nomeConvenio = nomeCompleto.toUpperCase();
		ModelAndView modelAndView = new ModelAndView("convenio/formConvenio");
		modelAndView.addObject("lista_convenios", cr.findByRazaoSocial(nomeConvenio));
		modelAndView.addObject("convenio", new Convenio());
		return modelAndView;

	}

	@RequestMapping("/deletarConvenio/{idConvenio}")
	public ModelAndView deletarConvenio(@PathVariable("idConvenio") Long codConvenio, Convenio convenio) {
		List<String> msg = new ArrayList<String>();
		ModelAndView model = new ModelAndView("/convenio/formConvenio");
		Convenio conv = cr.findByCodConvenio(codConvenio);
		List<Lancamento> lanc  = lr.findAllByConvenio(conv);
		if (lanc == null) {
			cr.delete(conv);
			Iterable<Convenio> convenios = cr.findAll();
			
			model.addObject("lista_convenios", convenios);
			model.addObject("listaPagam",Arrays.asList(TipoPagamento.values()));
		
			msg.add("Registro excluido com sucesso...");
			model.addObject("msg", msg);
			return model;
		}
		Iterable<Convenio> convenios = cr.findAll();
		
		model.addObject("lista_convenios", convenios);
		model.addObject("listaPagam",Arrays.asList(TipoPagamento.values()));
	
		msg.add("Registro não pode ser excluido...");
		
		model.addObject("msg", msg);
		return model;
		
	}
	
	@GetMapping("/editarConvenio/{idConvenio}")
	public String editarConvenio(@PathVariable("idConvenio") Long codConvenio, Model model) {
		Convenio con = cr.findByCodConvenio(codConvenio);
		model.addAttribute("convenio", con);
		Iterable<Convenio> convenios = cr.findAll();
		model.addAttribute("lista_convenios", convenios);
		model.addAttribute("listaPagam",Arrays.asList(TipoPagamento.values()));
		return "convenio/formConvenio";
		
	}

}
