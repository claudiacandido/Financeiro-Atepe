package org.atepe.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.atepe.model.Convenio;
import org.atepe.model.Lancamento;
import org.atepe.model.Movimento;
import org.atepe.model.Usuario;
import org.atepe.repository.ConvenioRepository;
import org.atepe.repository.LancamentoRepository;
import org.atepe.repository.MovimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
public class LancamentoController {

	@Autowired
	private LancamentoRepository lr;

	@Autowired
	private ConvenioRepository cr;

	@Autowired
	private MovimentoRepository mr;
	public String periodo;


	@GetMapping(value = "/cadastrarLancamento")
	public ModelAndView Form(Lancamento lancamento, Pageable pageable) {
		ModelAndView model = new ModelAndView("/lancamento/formLancamento");

		Iterable<Lancamento> lancamentos = lr.findAllByPeriodo(periodo,Sort.by("convenio.razaoSocial"));
		
		model.addObject("lanc_periodo", periodo);
		model.addObject("lista_lancamentos", lancamentos);
		Iterable<Convenio> convenios = cr.findAll();
		model.addObject("lista_convenios", convenios);
		return model;

	}

	@RequestMapping(value = "**/salvarLancamento", method = RequestMethod.POST)
	public String salvarLancamento(@Valid Lancamento lancamento, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView("/lancamento/formLancamento");
		Iterable<Convenio> convenios = cr.findAll();
		Date dataHoje = new Date();
		List<String> msg = new ArrayList<String>();
		lancamento.setDataInclusao(dataHoje);
		mv.addObject("lanc_periodo", periodo);
		
		if (bindingResult.hasErrors()) {
			Iterable<Lancamento> lancamentos = lr.findAllByPeriodo(periodo,Sort.by("convenio.razaoSocial"));
			mv.addObject("lista_lancamentos", lancamentos);
			mv.addObject("lista_convenios", convenios);

			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}
			mv.addObject("msg", msg);
			return "redirect:/cadastrarLancamento";

		}
		lr.save(lancamento);
		return "redirect:/cadastrarLancamento";

	}

	@PostMapping("**/pesquisarLancamentos")
	public ModelAndView pesquisar(@RequestParam("nomePesquisa") Long nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("lancamento/formLancamento");
		Convenio convenio = cr.findByCodConvenio(nomepesquisa);
		modelAndView.addObject("lanc_periodo", periodo);
		modelAndView.addObject("lista_lancamentos", lr.findAllByConvenio(convenio));
		Iterable<Convenio> convenios = cr.findAll();
		modelAndView.addObject("lista_convenios", convenios);
		modelAndView.addObject("lancamento", new Lancamento());
		return modelAndView;

	}

	@PostMapping("**/pesqPeriodoLancamento")
	public ModelAndView pesquisar(@RequestParam("periodoPesquisa") String periodoPesquisa) {
		ModelAndView modelAndView = new ModelAndView("lancamento/formLancamento");
		modelAndView.addObject("lista_lancamentos",
				lr.findAllByPeriodo(periodoPesquisa, Sort.by("convenio.razaoSocial")));
		Iterable<Convenio> convenios = cr.findAll();
		periodo = periodoPesquisa;
		modelAndView.addObject("lanc_periodo", periodoPesquisa);
		modelAndView.addObject("lista_convenios", convenios);
		modelAndView.addObject("lancamento", new Lancamento());
		return modelAndView;

	}
	@PostMapping("**/repetirLancamento")
	public ModelAndView repetir(@RequestParam("mesAno") String periodoPesquisa, @RequestParam("periodo") String periodoDestino) {
		ModelAndView modelAndView = new ModelAndView("lancamento/formLancamento");
		List<Lancamento> lancamentos = lr.findAllByPeriodoFixo(periodoPesquisa);
		Date dataHoje = new Date();		
		for (int i = 0; i < lancamentos.size(); i++) {
			Lancamento lanc = new Lancamento();
			lanc.setConvenio(lancamentos.get(i).getConvenio());
			lanc.setMesAno(periodoDestino);
			lanc.setDataInclusao(dataHoje);
			lanc.setValorTotal(lancamentos.get(i).getValorTotal());
			lr.save(lanc);
		    List<Movimento> movimentos = mr.findAllByPeriodoLancamento(periodoPesquisa,lancamentos.get(i));
		  	for (int m = 0; m < movimentos.size(); m++) {
		 		Movimento mov = new Movimento();
				mov.setLancamento(lanc);
				mov.setAssociado(movimentos.get(m).getAssociado());
				mov.setDataInclusao(dataHoje);
				mov.setPeriodo(periodoDestino);
				mov.setValorTotal(movimentos.get(m).getValorTotal());
				mr.save(mov);				
			}	
		 	
		}	
		
		Iterable<Convenio> convenios = cr.findAll();
		modelAndView.addObject("lista_lancamentos",
				lr.findAllByPeriodo(periodoPesquisa, Sort.by("convenio.razaoSocial")));
		modelAndView.addObject("lanc_periodo", periodoPesquisa);
		modelAndView.addObject("lista_convenios", convenios);
		modelAndView.addObject("lancamento", new Lancamento());
		return modelAndView;

	}

	@RequestMapping("/lancamentos")
	public ModelAndView ListaLancamentos(Usuario usuario) {
		ModelAndView mv = new ModelAndView("/lancamento/listaLancamento");
		Iterable<Lancamento> lancamentos = lr.findAll();
		mv.addObject("lancamentos", lancamentos);
		return mv;
	}

	@GetMapping("/deletarLancamento/{idLancamento}")
	public ModelAndView deletarLancamento(@PathVariable("idLancamento") Long idLancamento) {
		Iterable<Convenio> convenios = cr.findAll();
		Lancamento lancamento = lr.findByCodLancamento(idLancamento);
		List<Movimento> movimentos = mr.findAllByLancamento(lancamento,Sort.by("associado.nomeCompleto"));
		ModelAndView mv = new ModelAndView("lancamento/formLancamento");
		List<String> msg = new ArrayList<String>();
		if (movimentos.size() > 0 && !movimentos.isEmpty()) {
			mv.addObject("lancamento", new Lancamento());
			Iterable<Lancamento> lancamentos = lr.findAll();
			mv.addObject("lista_lancamentos", lancamentos);
			mv.addObject("lista_convenios", convenios);
			msg.add("Registro não pode ser excluido, existe lançamento feitos...");
			mv.addObject("msg", msg);
			return mv;
		}
		lr.delete(lancamento);
		mv.addObject("lanc_periodo", periodo);
		mv.addObject("lancamento", new Lancamento());
		Iterable<Lancamento> lancamentos = lr.findAll();
		mv.addObject("lista_lancamentos", lancamentos);
		mv.addObject("lista_convenios", convenios);
		msg.add("Registro excluido com sucesso...");
		mv.addObject("msg", msg);
		return mv;
	}

	@RequestMapping(value = "**/editarLancamento/{codLancamento}", method = RequestMethod.GET)
	public String editarLancamento(@PathVariable("codLancamento") Long codLancamento, Model model) {
		Lancamento fecha = lr.findByCodLancamento(codLancamento);
		model.addAttribute("lanc_periodo", periodo);
		model.addAttribute("lancamento", fecha);
		Iterable<Lancamento> lancamentos = lr.findAllByPeriodo(periodo, Sort.by("convenio.razaoSocial"));
		
		model.addAttribute("lista_lancamentos", lancamentos);
		Iterable<Convenio> convenios = cr.findAll();
		model.addAttribute("lista_convenios", convenios);
		return "lancamento/formLancamento";
	}

	

}
