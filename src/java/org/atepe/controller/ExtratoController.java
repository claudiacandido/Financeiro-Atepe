package org.atepe.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.atepe.model.Associado;
import org.atepe.model.Fechamento;
import org.atepe.model.Movimento;
import org.atepe.model.TipoAssociado;
import org.atepe.model.TipoPagamento;
import org.atepe.repository.AssociadoRepository;
import org.atepe.repository.FechamentoRepository;
import org.atepe.repository.MovimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExtratoController {

	@Autowired
	private MovimentoRepository mr;

	@Autowired
	private AssociadoRepository ar;

	@Autowired
	private FechamentoRepository fr;

	@GetMapping(value = "/consultarExtrato")
	public String Form(Associado associado, Model model) {

		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		model.addAttribute("lista_associados", associados);
		return "extrato/formExtrato";
	}

	@PostMapping("**/pesquisarExtrato")
	public ModelAndView pesquisar(@RequestParam("nomePesquisa") Long nomePesquisa,
			@RequestParam("matriculaPesquisa") String matricula,
			@RequestParam("periodoPesquisa") String periodoPesquisa) {
		ModelAndView model = new ModelAndView("extrato/formExtrato");
		Associado associado = new Associado();
		if ((!matricula.equals("")) && (matricula.length()>=3)) {
			associado = ar.findByMatricula(matricula);
		} else {
			associado = ar.findByCodAssociado(nomePesquisa);
		}
		if (associado != null) {
			List<Movimento> movimentos = mr.findAllByPeriodoAssociado(periodoPesquisa, associado);
			if (movimentos != null) {
				model.addObject("associado", associado);
				model.addObject("lista_lancamentos", movimentos);
				Double ValorTotal = 0.0;
				Double ValorFolha = 0.0;
				Double ValorBoleto = 0.0;
				Fechamento fecha = fr.findByAssociadoPeriodo(associado, periodoPesquisa);
				if (fecha !=null) {
					ValorFolha = fecha.getValorFolha().doubleValue();
					ValorBoleto = fecha.getValorBoleto().doubleValue();
					for (int i = 0; i < movimentos.size(); i++) {
						ValorTotal = ValorTotal + movimentos.get(i).getValorTotal();
					}
					model.addObject("valor_boleto", ValorBoleto);
					model.addObject("valor_folha", ValorFolha);
					
					model.addObject("valor_total", ValorTotal);
				}
			} else {
				List<String> msg = new ArrayList<String>();
				msg.add("Não existe lançamento para esse associado...");
				model.addObject("msg", msg);
			}
		}
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		model.addObject("lista_associados", associados);
		return model;

	}

	@GetMapping(value = "/consTipoAssociado")
	public String FormTipo(Associado associado, Model model) {
		model.addAttribute("listaAssoc", Arrays.asList(TipoAssociado.values()));
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		model.addAttribute("lista_associados", associados);
		return "visualizar/formTipoAssociado";
	}

	@PostMapping("**/pesquisarTipoAssociado")
	public ModelAndView pesquisarTipo(@RequestParam("nomePesquisa") TipoAssociado nomePesquisa) {
		ModelAndView model = new ModelAndView("/visualizar/formTipoAssociado");
		List<Associado> associados = ar.findByTipoAssociado(nomePesquisa);
		model.addObject("tipoAssociado", nomePesquisa);
		model.addObject("lista_associados", associados);
		model.addObject("listaAssoc", Arrays.asList(TipoAssociado.values()));

		return model;

	}

	@GetMapping(value = "/consTipoPagto")
	public String FormTipoPagamento(Model model) {
		model.addAttribute("listaPagam", Arrays.asList(TipoPagamento.values()));
		Iterable<Movimento> movimentos = mr.findAll(Sort.by("associado.nomeCompleto"));
		model.addAttribute("lista_lancamentos", movimentos);
		return "visualizar/formExtratoTipoPgto";
	}

	@PostMapping("**/pesquisarTipoPagto")
	public ModelAndView pesquisarTipoPgto(@RequestParam("nomePesquisa") TipoPagamento nomePesquisa,
			@RequestParam("periodoPesquisa") String periodoPesquisa) {
		ModelAndView model = new ModelAndView("/visualizar/formExtratoTipoPgto");
		List<Movimento> movimentos = mr.findAllByPeriodoTipo(periodoPesquisa, nomePesquisa, Sort.by("associado"));
		model.addObject("periodo", periodoPesquisa);
		model.addObject("tipoPagto", nomePesquisa);
		model.addObject("listaPagam", Arrays.asList(TipoPagamento.values()));
		model.addObject("lista_lancamentos", movimentos);
		return model;
	}

	@GetMapping(value = "/consPagtoTipoAssociado")
	public String FormPagtoTipo(Model model) {
		model.addAttribute("listaAssoc", Arrays.asList(TipoAssociado.values()));
		Iterable<Movimento> movimentos = mr.findAll(Sort.by("associado.nomeCompleto"));
		model.addAttribute("lista_lancamentos", movimentos);
		return "visualizar/formExtratoPTAssociado";
	}

	@PostMapping("**/pesqPagtoTipoAssociado")
	public ModelAndView pesquisarPgtoTipoAssoc(@RequestParam("nomePesquisa") TipoAssociado nomePesquisa,
			@RequestParam("periodoPesquisa") String periodoPesquisa) {
		ModelAndView model = new ModelAndView("/visualizar/formExtratoPTAssociado");
		List<Movimento> movimentos = mr.findAllByPeriodoTipoAssociado(periodoPesquisa, nomePesquisa,
				Sort.by("associado"));
		model.addObject("tipoAssociado", nomePesquisa);
		model.addObject("periodo", periodoPesquisa);
		model.addObject("listaAssoc", Arrays.asList(TipoAssociado.values()));
		model.addObject("lista_lancamentos", movimentos);
		return model;
	}

}
