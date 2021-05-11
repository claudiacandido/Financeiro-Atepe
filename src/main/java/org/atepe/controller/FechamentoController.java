package org.atepe.controller;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.atepe.model.Associado;
import org.atepe.model.Fechamento;
import org.atepe.model.Lancamento;
import org.atepe.model.Movimento;
import org.atepe.model.TipoAssociado;
import org.atepe.repository.AssociadoRepository;
import org.atepe.repository.FechamentoRepository;
import org.atepe.repository.MovimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.opencsv.exceptions.CsvException;

@Controller
public class FechamentoController {

	@Autowired
	private FechamentoRepository fr;

	@Autowired
	private MovimentoRepository mr;

	@Autowired
	private AssociadoRepository ar;
	
	private String periodo;

	@GetMapping(value = "/cadastrarFechamento")
	public ModelAndView Form(Fechamento fechamento, Pageable pageable) {
		ModelAndView model = new ModelAndView("/fechamento/formFechamento");
		Iterable<Associado> associados = ar.findAll();
		model.addObject("lista_associados", associados);
		findPaginatedFecha(1, model);
		return model;

	}
	
	@GetMapping(value = "/cancelarFechamento")
	public String Cancelar(Fechamento fechamento) {
		return "redirect:/cadastrarFechamento";
	}

	@GetMapping("/pageFecha/{pageNo}")
	public ModelAndView viewPaginatedFecha(@PathVariable(value = "pageNo") int pageNo, Fechamento fechamento) {
		ModelAndView model = new ModelAndView("/fechamento/formFechamento");
		findPaginatedFecha(pageNo, model);
		model.addObject("fechamento", new Fechamento());
		return model;

	}

	public void findPaginatedFecha(@PathVariable(value = "pageNo") int pageNo, ModelAndView model) {
		int pageSize = 8;
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<Fechamento> page = fr.findAll(pageable);
		List<Fechamento> listFechamento = page.getContent();
		Iterable<Associado> associados = ar.findAll();
		model.addObject("lista_associados", associados);
		model.addObject("totalPagina", page.getTotalPages());
		model.addObject("totalItem", page.getTotalElements());
		model.addObject("paginaAtual", pageNo);
		model.addObject("lista_fechamentos", listFechamento);

	}

	@RequestMapping(value = "**/salvarFechamento", method = RequestMethod.POST)
	public String salvarFechamento(@Valid Fechamento fechamento, Pageable pageable, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView("/fechamento/formFechamento");
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		mv.addObject("lanc_periodo", periodo);
		List<String> msg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			Iterable<Fechamento> fechamentos = fr.findAll();
			mv.addObject("lista_fechamentos", fechamentos);
			mv.addObject("lista_associados", associados);
			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}
			mv.addObject("msg", msg);
			return "redirect:/cadastrarFechamento";
		}
		fr.save(fechamento);
		mv.addObject("lista_associados", associados);
		// Date dataHoje = new Date();
		mv.addObject("fechamento", new Fechamento());
		Iterable<Fechamento> fechamentos = fr.findAll();
		mv.addObject("lista_fechamentos", fechamentos);
		msg.add("Registro salvo com sucesso...");
		mv.addObject("msg", msg);
		return "redirect:/cadastrarFechamento";
	}

	@PostMapping("**/pesquisarFechamento")
	public ModelAndView pesquisarFechamento(@RequestParam("nomePesquisa") Long nomepesquisa, 
			@RequestParam("matriculaPesquisa") String matricula) {
		ModelAndView modelAndView = new ModelAndView("fechamento/formFechamento");
		Associado associado = new Associado();
		if ((!matricula.equals("")) && (matricula.length()>=3)) {
			 associado = ar.findByMatricula(matricula);
		}else {
			 associado = ar.findByCodAssociado(nomepesquisa);
		}
		Fechamento fechamentos = fr.findByAssociadoPeriodo(associado,periodo);
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		modelAndView.addObject("lanc_periodo", periodo);
		
		modelAndView.addObject("lista_associados", associados);
		modelAndView.addObject("lista_fechamentos", fechamentos);
		modelAndView.addObject("fechamento", new Fechamento());
		return modelAndView;

	}

	@PostMapping("**/pesqPeriodoFechamento")
	public ModelAndView pesqPeriodoFechamento(@RequestParam("periodoPesquisa") String periodoPesquisa) {
		ModelAndView modelAndView = new ModelAndView("fechamento/formFechamento");
		Iterable<Fechamento> fechamentos = fr.findAllByPeriodo(periodoPesquisa, Sort.by("associado.nomeCompleto"));
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		periodo = periodoPesquisa;
		modelAndView.addObject("lanc_periodo", periodoPesquisa);
		modelAndView.addObject("lista_associados", associados);
		modelAndView.addObject("lista_fechamentos", fechamentos);
		modelAndView.addObject("fechamento", new Fechamento());
		return modelAndView;

	}

	@RequestMapping(value = "**/editarFechamento/{codFechamento}", method = RequestMethod.GET)
	public ModelAndView editarFechamento(@PathVariable("codFechamento") Long codFechamento, Fechamento fechamento) {
		ModelAndView model = new ModelAndView("fechamento/formFechamento");
		Optional<Fechamento> fecha = fr.findById(codFechamento);
		model.addObject("lanc_periodo", periodo);
		model.addObject("fechamento", fecha);
		Iterable<Fechamento> fechamentos = fr.findAll();
		model.addObject("lista_fechamentos", fechamentos);
		Iterable<Associado> associados = ar.findAll();
		model.addObject("lista_associados", associados);
		return model;
	}

	@PostMapping("**/efetuarFechamento")
	public ModelAndView efetuarFechamento(@RequestParam("periodoPesquisa") String periodoPesquisa) {
		ModelAndView modelAndView = new ModelAndView("fechamento/formFechamento");
		Date dataHoje = new Date();
		
		double valorTotal = 0.00;
		double valorAcima = 0.00;
		double valorBoleto = 0.00;
		double valorFolha = 0.00;
		double valorMargem = 0.00;
		String matricula = "";
		List<Fechamento> fechaExiste = fr.findAllByPeriodo(periodoPesquisa, Sort.by("associado.nomeCompleto"));
		if (fechaExiste!= null) {
			 fr.deleteAll(fechaExiste);
		}

		List<Movimento> movimentos = mr.findAllByPeriodo(periodoPesquisa, Sort.by("associado"));

		for (int i = 0; i < movimentos.size(); i++) {
			if (movimentos.get(i).getAssociado().getMatricula().equals(matricula)) {
				if (movimentos.get(i).getLancamento().getConvenio().getTipoPagamento().toString().equals("FOLHA")) {
					valorFolha = valorFolha + movimentos.get(i).getValorTotal();
				} else {
					valorBoleto = valorBoleto + movimentos.get(i).getValorTotal();
				}
				valorTotal = valorTotal + movimentos.get(i).getValorTotal();
			} else {
				if (matricula.equals("")) {
					valorAcima = 0.0;
					valorMargem = 0.0;
					matricula = movimentos.get(i).getAssociado().getMatricula().toString();
					valorTotal = movimentos.get(i).getValorTotal();
					if (movimentos.get(i).getAssociado().getMargem()!=null) {
						valorMargem = movimentos.get(i).getAssociado().getMargem();
					}	
					if (movimentos.get(i).getLancamento().getConvenio().getTipoPagamento().toString().equals("FOLHA")) {
						valorFolha = movimentos.get(i).getValorTotal();
					} else {
						valorBoleto = movimentos.get(i).getValorTotal();
					}
				} else {
					if (valorMargem > 0) {
						if (valorFolha > valorMargem) {
							valorAcima = valorFolha - valorMargem;
							valorFolha = valorMargem;
							if (valorBoleto > 0) {
								valorAcima = valorAcima + valorBoleto;

							}
						} else {
							if (valorBoleto > 0) {
								valorAcima = valorAcima + valorBoleto;
							}
						}
					} else {
						valorBoleto =valorBoleto + valorFolha;
						valorAcima = valorAcima + valorBoleto;
						valorFolha= 0.0;
						
					}
					Associado associado = ar.findByMatricula(matricula);

					Fechamento fechaAlterar = fr.findByAssociadoPeriodo(associado, periodoPesquisa);
					if (fechaAlterar == null) {
						Fechamento fecha = new Fechamento();
						fecha.setAssociado(associado);
						fecha.setPeriodo(periodoPesquisa);
						fecha.setDataInclusao(dataHoje);
						fecha.setValorDevido(valorAcima);
						fecha.setValorTotal(valorTotal);
						fecha.setValorFolha(valorFolha);
						fecha.setValorBoleto(valorBoleto);
						fr.save(fecha);

					} else {
						fechaAlterar.setValorDevido(valorAcima);
						fechaAlterar.setValorTotal(valorTotal);
						fechaAlterar.setValorFolha(valorFolha);
						fechaAlterar.setDataInclusao(dataHoje);
						fechaAlterar.setValorBoleto(valorBoleto);
						fr.save(fechaAlterar);

					}
					valorAcima = 0.00;
					valorTotal = 0.00;
					valorBoleto = 0.00;
					valorFolha = 0.00;
					valorMargem = 0.00;
					matricula = "";
					if (movimentos.get(i).getLancamento().getConvenio().getTipoPagamento().toString().equals("FOLHA")) {
						valorFolha = movimentos.get(i).getValorTotal();
					} else {
						valorBoleto = movimentos.get(i).getValorTotal();
					}
					matricula = movimentos.get(i).getAssociado().getMatricula().toString();
					valorTotal = movimentos.get(i).getValorTotal();
					if (movimentos.get(i).getAssociado().getMargem()!=null) {
						valorMargem = movimentos.get(i).getAssociado().getMargem();
					}

				}
			}
		}
		Iterable<Associado> associados = ar.findAll();
		modelAndView.addObject("lanc_periodo", periodo);
		modelAndView.addObject("lista_associados", associados);
		modelAndView.addObject("fechamento", new Fechamento());
		Iterable<Fechamento> fechamentos = fr.findAllByPeriodo(periodoPesquisa, Sort.by("associado.nomeCompleto"));
		modelAndView.addObject("lista_fechamentos", fechamentos);
		return modelAndView;
	}

	
	
	@RequestMapping(value = "/importarPagamentos", method = RequestMethod.POST)
	public String uploadFile(@RequestParam("arquivoImportado") MultipartFile multipartFile, 
			@RequestParam("periodoPesquisa") String periodo)
			throws IOException, CsvException, ParseException {
		ModelAndView model = new ModelAndView("fechamento/formFechamento");
		String filePath = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		String extensaoDoArquivo = FilenameUtils.getExtension(filePath);
		// Abrindo o arquivo e recuperando a planilha
		if (extensaoDoArquivo.equals("xls")) {
			Workbook workbook = new HSSFWorkbook(multipartFile.getInputStream());
			// Seta a aba
			Sheet sheet = workbook.getSheetAt(0);
			Locale ptBR = new Locale("pt", "BR");
			DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(ptBR));
		
			int linhas = sheet.getLastRowNum();
			for (int i = 18; i <= linhas; i++) {
				if (sheet.getRow(i) != null) {
					String ca = sheet.getRow(i).getCell(1).toString();
					Associado associado = new Associado();
					associado = ar.findByMatricula(String.valueOf(ca));
					if (associado!=null) {
					 String cc= sheet.getRow(i).getCell(5).toString().replaceAll("[\"']","");
					 String valor =sheet.getRow(i).getCell(7).toString().replaceAll("[\"']","");
					 double cd = df.parse(valor).doubleValue();
					 Fechamento fecha = fr.findByAssociadoPeriodo(associado, periodo);
					 if (fecha != null) {
						fecha.setValorPago(cd);
						fecha.setDataPagamento(cc);
						fr.save(fecha);
					 }
					} else {
					   String cb = sheet.getRow(i).getCell(3).toString();
					//   System.out.println("NOme"+cb);
					   Associado associadoNome =   ar.findByNome(cb);
						if (associadoNome!=null) {
							 String cc= sheet.getRow(i).getCell(5).toString().replaceAll("[\"']","");
							 String valor =sheet.getRow(i).getCell(7).toString().replaceAll("[\"']","");
							 double cd = df.parse(valor).doubleValue();
							 Fechamento fecha = fr.findByAssociadoPeriodo(associadoNome, periodo);
							 if (fecha != null) {
								fecha.setValorPago(cd);
								fecha.setDataPagamento(cc);
								fr.save(fecha);
							}
						}	 
					}
				}

			}
			workbook.close();
		}
		Iterable<Associado> associados = ar.findAll();
		model.addObject("lanc_periodo", periodo);
		model.addObject("lista_associados", associados);
		model.addObject("fechamento", new Fechamento());
		Iterable<Fechamento> fechamentos = fr.findAllByPeriodo(periodo, Sort.by("associado.nomeCompleto"));
		model.addObject("lista_fechamentos", fechamentos);
		
		return "redirect:/cadastrarFechamento";
	}

}