package org.atepe.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.atepe.model.Associado;
import org.atepe.model.Lancamento;
import org.atepe.model.Movimento;
import org.atepe.model.TipoAssociado;
import org.atepe.repository.AssociadoRepository;
import org.atepe.repository.LancamentoRepository;
import org.atepe.repository.MovimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class MovimentoController {

	@Autowired
	private MovimentoRepository mr;

	@Autowired
	private AssociadoRepository ar;

	@Autowired
	private LancamentoRepository fr;

	private Long codigoFC;

	private Scanner leitor;

	@GetMapping(value = "/cadastrarMovimento{codLancamento}")
	public String Form(@PathVariable("codLancamento") Long codigo, Model model, Movimento movimento) {
		Lancamento lanc = fr.findByCodLancamento(codigoFC);
		model.addAttribute("lancamento", lanc);
		Iterable<Movimento> movimentos = mr.findAllByLancamento(lanc,Sort.by("associado.nomeCompleto"));
		model.addAttribute("lista_movimentos", movimentos);
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		model.addAttribute("lista_associados", associados);
		return "movimento/formMovimento";

	}

	@RequestMapping(value = "/cadastrarMovimento", method = RequestMethod.POST)
	public String Form(Movimento movimento) {
		return "redirect:/cadastrarMovimento";

	}

	@GetMapping("/pageLanc/{pageNo}")
	public ModelAndView viewPaginatedMov(@PathVariable(value = "pageNo") int pageNo, Movimento movimento) {
		ModelAndView model = new ModelAndView("/movimento/formMovimento");
		findPaginatedMov(pageNo, model);
		model.addObject("movimento", new Movimento());
		return model;

	}

	public void findPaginatedMov(@PathVariable(value = "pageNo") int pageNo, ModelAndView model) {
		int pageSize = 8;
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<Movimento> page = mr.findAll(pageable);
		List<Movimento> listFechamento = page.getContent();
		model.addObject("totalPagina", page.getTotalPages());
		model.addObject("totalItem", page.getTotalElements());
		model.addObject("paginaAtual", pageNo);
		model.addObject("lista_fechamentos", listFechamento);

	}

	@RequestMapping(value = "**/salvarMovimento", method = RequestMethod.POST)
	public ModelAndView salvarMovimento(@Valid Movimento movimento, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView("/movimento/formMovimento");
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		List<String> msg = new ArrayList<String>();
		Lancamento lanc = fr.findByCodLancamento(codigoFC);
		mv.addObject("lancamento", lanc);
		movimento.setLancamento(lanc);
		movimento.setPeriodo(lanc.getMesAno());
		if (bindingResult.hasErrors()) {
			Iterable<Movimento> movimentos = mr.findAll();
			mv.addObject("lista_movimentos", movimentos);
			mv.addObject("lista_associados", associados);
			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}
			mv.addObject("msg", msg);
			return mv;
		}
		mr.save(movimento);
		Iterable<Movimento> movimentos = mr.findAllByLancamento(lanc, Sort.by("associado.nomeCompleto"));
		mv.addObject("lista_movimentos", movimentos);
		mv.addObject("movimento", new Movimento());
		mv.addObject("lista_associados", associados);
		msg.add("Registro salvo com sucesso...");
		mv.addObject("msg", msg);
		return mv;

	}

	@RequestMapping("/movimentos")
	public ModelAndView ListaMovimentos() {
		ModelAndView mv = new ModelAndView("/movimento/listaMovimento");
		Iterable<Movimento> movimentos = mr.findAll();
		mv.addObject("movimentos", movimentos);
		return mv;
	}

	@GetMapping("/deletarMovimento/{idMovimento}")
	public ModelAndView deletarMovimento(@PathVariable("idMovimento") Long idMovimento) {
		ModelAndView mv = new ModelAndView("movimento/formMovimento");
		Movimento movimento = mr.findByCodMovimento(idMovimento);
		List<String> msg = new ArrayList<String>();
		mr.delete(movimento);
		Lancamento lanc = fr.findByCodLancamento(codigoFC);
		mv.addObject("lancamento", lanc);
		Iterable<Movimento> movimentos = mr.findAllByLancamento(lanc,Sort.by("associado.nomeCompleto"));
		mv.addObject("lista_movimentos", movimentos);
		mv.addObject("movimento", new Movimento());
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		mv.addObject("lista_associados", associados);
		msg.add("Registro excluido com sucesso...");
		mv.addObject("msg", msg);

		return mv;
	}

	@GetMapping("/editarMovimento/{idMovimento}")
	public String editarMovimento(@PathVariable("idMovimento") Long idMovimento, Model model) {
		Movimento mov = mr.findByCodMovimento(idMovimento);
		model.addAttribute("movimento", mov);

		Lancamento lanc = fr.findByCodLancamento(mov.getLancamento().getCodLancamento());
		model.addAttribute("lancamento", lanc);

		Iterable<Movimento> movimentos = mr.findAll();
		model.addAttribute("lista_movimentos", movimentos);
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		model.addAttribute("lista_associados", associados);

		return "movimento/formMovimento";
	}

	@RequestMapping(value = "/importeDados", method = RequestMethod.POST)
	public String uploadFile(@RequestParam("arquivoImportado") MultipartFile multipartFile)
			throws IOException, CsvException, ParseException {
		Lancamento lanc = fr.findByCodLancamento(codigoFC);

		ModelAndView model = new ModelAndView("movimento/formMovimento");
		model.addObject("lancamento", lanc);

		String filePath = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		Date dataHoje = new Date();
		LocalDate dhoje = LocalDate.now();
		String extensaoDoArquivo = FilenameUtils.getExtension(filePath);
		Locale ptBR = new Locale("pt", "BR");
		DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(ptBR));
		double valTotal = 0;

		// Abrindo o arquivo e recuperando a planilha
		int verba;

		if (extensaoDoArquivo.equals("xls")) {
			Workbook workbook = new HSSFWorkbook(multipartFile.getInputStream());
			// Seta a aba
			Sheet sheet = workbook.getSheetAt(0);
			int linhas = sheet.getLastRowNum();
			for (int i = 1; i < linhas; i++) {
				if (sheet.getRow(i) != null) {
					verba = (int) sheet.getRow(i).getCell(3).getNumericCellValue();
					if (i == 1) {
						if (verba != lanc.getConvenio().getVerba()) {
							System.out.println("Arquivo não é da Verba informada ou está com outro formato");
						}
					}
					int ca = (int) sheet.getRow(i).getCell(0).getNumericCellValue();
					String cb = sheet.getRow(i).getCell(1).toString();
					String cc = sheet.getRow(i).getCell(2).toString();
					Associado associado = new Associado();
					associado = ar.findByMatricula(String.valueOf(ca));
					double ce = sheet.getRow(i).getCell(4).getNumericCellValue();

					if (associado == null) {

						Associado assoc = new Associado();
						assoc.setMatricula(Integer.toString(ca));
						assoc.setCpf(cb);
						assoc.setNomeCompleto(cc);
						assoc.setDataInclusao(dhoje);
						assoc.setMargem(5000.00);
						TipoAssociado tipoAssociado = TipoAssociado.ATIVO;
						assoc.setTipoAssociado(tipoAssociado);
						ar.save(assoc);
						associado = ar.findByMatricula(String.valueOf(ca));
					}
					Movimento movlanc = mr.findByLancAssocPeriodo(associado, lanc, lanc.getMesAno());
					if (movlanc == null) {
						Movimento movimento = new Movimento();
						movimento.setLancamento(lanc);
						movimento.setValorTotal(ce);
						movimento.setValorParcela(ce);
						movimento.setQuantidade(1);
						movimento.setAssociado(associado);
						movimento.setPeriodo(lanc.getMesAno());
						movimento.setDataInclusao(dataHoje);
						mr.save(movimento);
					} else {
						movlanc.setValorTotal(ce);
						movlanc.setValorParcela(ce);
						movlanc.setQuantidade(1);
						movlanc.setDataInclusao(dataHoje);
						mr.save(movlanc);
					}
				}

			}
			workbook.close();
		} else {
			InputStreamReader reader = new InputStreamReader(multipartFile.getInputStream());
			String linhaarquivo = new String();
			leitor = new Scanner(reader);
			String valor;
			String csvSeparadorCampo = ";";
			int linha = 0;
			leitor.nextLine();
			while (leitor.hasNext()) {
				linha++;
				linhaarquivo = leitor.nextLine();
				if (linhaarquivo.length() > 30) {
					String[] col = linhaarquivo.split(csvSeparadorCampo);
					verba = Integer.parseInt(col[3]);
					if (linha == 1) {
						if (verba != lanc.getConvenio().getVerba()) {
							System.out.println("Arquivo não é da Verba informada ou está com outro formato");
						}
					}

					Associado associado = new Associado();
					associado = ar.findByMatricula(String.valueOf(col[0].toString()));
					if (associado == null) {
						Associado assoc = new Associado();
						assoc.setMatricula(String.valueOf(col[0].toString()));
						assoc.setCpf(col[1].toString());
						assoc.setNomeCompleto(col[2].toString());
						assoc.setDataInclusao(dhoje);
						assoc.setMargem(5000.00);
						TipoAssociado tipoAssociado = TipoAssociado.ATIVO;
						assoc.setTipoAssociado(tipoAssociado);
						ar.save(assoc);
						associado = ar.findByMatricula(col[0].toString());
					}
					valor = col[4].replaceAll("[\"R$ ]", "");
					valTotal = df.parse(valor).doubleValue();
					Movimento movlanc = mr.findByLancAssocPeriodo(associado, lanc, lanc.getMesAno());
					if (movlanc == null) {
						Movimento movim = new Movimento();
						movim.setLancamento(lanc);
						movim.setValorTotal(valTotal);
						movim.setValorParcela(valTotal);
						movim.setQuantidade(1);
						movim.setAssociado(associado);
						movim.setPeriodo(lanc.getMesAno());
						movim.setDataInclusao(dataHoje);
						mr.save(movim);
					}
				}
			}
		}
		Iterable<Movimento> movimentos = mr.findAll();
		model.addObject("lista_movimentos", movimentos);
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		model.addObject("lista_associados", associados);
		return "redirect:/cadastrarMovimento";
	}

	@RequestMapping("/detalhesMovimento/{idLancamento}")
	public String detalhesMovimento(@PathVariable("idLancamento") Long codLancamento, Movimento movimento) {
		ModelAndView mv = new ModelAndView("/movimento/formMovimento");
		Lancamento lancamento = fr.findByCodLancamento(codLancamento);
		this.codigoFC = lancamento.getCodLancamento();
		mv.addObject("lancamento", lancamento);
		Iterable<Movimento> movimentos = mr.findAllByLancamento(lancamento, Sort.by("associado.nomeCompleto"));
		mv.addObject("lista_movimentos", movimentos);
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		mv.addObject("lista_associados", associados);
		Associado associado = new Associado();
		mv.addObject("associado", associado);
		return "redirect:/cadastrarMovimento";

	}

	@PostMapping("**/pesquisarMovimento")
	public ModelAndView pesquisarMovimento(@RequestParam("nomePesquisa") Long nomepesquisa) {
		ModelAndView model = new ModelAndView("movimento/formMovimento");
		Lancamento lanc = fr.findByCodLancamento(codigoFC);
		model.addObject("lancamento", lanc);
		Associado associado = ar.findByCodAssociado(nomepesquisa);
		Iterable<Movimento> movimentos = mr.findAllByLancamentoAssociado(associado, lanc);
		model.addObject("lista_movimentos", movimentos);
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		model.addObject("lista_associados", associados);
		model.addObject("movimento", new Movimento());
		return model;

	}

	@PostMapping("**/limparMovimento")
	public ModelAndView limparMovimento(@RequestParam("periodoPesquisa") String periodo) {
		ModelAndView model = new ModelAndView("movimento/formMovimento");
		Lancamento lanc = fr.findByCodLancamento(codigoFC);
		model.addObject("lancamento", lanc);
		Iterable<Movimento> movExiste = mr.findAllByPeriodoLancamento(periodo, lanc);
		if (movExiste != null) {
			mr.deleteAll(movExiste);
		}
		Iterable<Movimento> movimentos = mr.findAllByLancamento(lanc,Sort.by("associado.nomeCompleto"));
		model.addObject("lista_movimentos", movimentos);
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		model.addObject("lista_associados", associados);
		model.addObject("movimento", new Movimento());
		return model;

	}
}
