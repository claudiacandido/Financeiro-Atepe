package org.atepe.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.atepe.model.Lancamento;
import org.atepe.repository.LancamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PagamentoController {

	@Autowired
	private LancamentoRepository fr;

	@RequestMapping(value = "/fazerPagamento/{codLancamento}", method = RequestMethod.GET)
	public String fazerLancamento(@PathVariable("codLancamento") Long codLancamento, Model model) {
		Lancamento fecha = fr.findByCodLancamento(codLancamento);
		model.addAttribute("lancamento", fecha);
		Iterable<Lancamento> lancamentos = fr.findAll();
		model.addAttribute("lista_lancamentos", lancamentos);
		return "lancamento/formPagamento";
	}
	


	@RequestMapping(value = "**/salvarPagamento", method = RequestMethod.POST)
	public ModelAndView salvarPagamento(@Valid Lancamento lancamento, BindingResult bindingResult) {
		ModelAndView model = new ModelAndView("/lancamento/formPagamento");
		List<String> msg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			System.out.println("entrou errorr");
			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}
			model.addObject("msg", msg);

			return model;
		}
		fr.save(lancamento);
		msg.add("Registro salvo com sucesso...");
		model.addObject("msg", msg);
		model.addObject("lancamento", new Lancamento());
		return model;
	}

	@GetMapping(value = "/fazerPagamento")
	public ModelAndView fazerPagamento(Pageable pageable) {
		Lancamento lancamento = new Lancamento();
		ModelAndView model = new ModelAndView("/lancamento/formPagamento");
		model.addObject("lancamento", lancamento);

		return model;

	}


	@GetMapping(value = "/cancelarPagamento")
	public String Cancelar(Lancamento lancamento) {
		return "redirect:/fazerPagamento";
	}
	
	@RequestMapping(value = "/pesquisarPagamentos", method = RequestMethod.POST)
	public ModelAndView pesquisarPagtos(@RequestParam("periodoPesquisa") String periodoPesquisa) {
		ModelAndView modelAndView = new ModelAndView("/lancamento/formPagamento");
		List<Lancamento> lancamentosApagar = fr.findAllByPeriodoApagar(periodoPesquisa);
		modelAndView.addObject("lista_lac_apagar", lancamentosApagar);
		Double TotalApagar = 0.0;
		for (int i = 0; i < lancamentosApagar.size(); i++) {
			System.out.println("total" + TotalApagar);
			TotalApagar = TotalApagar + lancamentosApagar.get(i).getValorTotal();
		}
		modelAndView.addObject("valor_apagar", TotalApagar);
		modelAndView.addObject("mes_consulta", periodoPesquisa);
		
		Double TotalPago = 0.0;
		List<Lancamento> lancamentosPago = fr.findAllByPeriodoPago(periodoPesquisa);
		modelAndView.addObject("lista_lac_pago", lancamentosPago);
		for (int i = 0; i < lancamentosPago.size(); i++) {
			System.out.println("total" + TotalPago);
			if (lancamentosPago.get(i).getValorPago() > 0) {
				TotalPago = TotalPago + (Double) lancamentosPago.get(i).getValorPago();
			}
		}
		modelAndView.addObject("valor_pago", TotalPago);

		modelAndView.addObject("lancamento", new Lancamento());
		return modelAndView;
	}

}
