package org.atepe.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.atepe.model.Associado;
import org.atepe.model.Movimento;
import org.atepe.model.TipoAssociado;
import org.atepe.repository.AssociadoRepository;
import org.atepe.repository.MovimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Controller
public class AssociadoController {

	@Autowired
	private AssociadoRepository ar;
	

	@Autowired
	private MovimentoRepository mr;

	

	@GetMapping(value = "/cadastrarAssociado")
	public ModelAndView Form(Associado associado) {
		ModelAndView model = new ModelAndView("/associado/formAssociado");
		model.addObject("listaAssoc", Arrays.asList(TipoAssociado.values()));
		model.addObject("associado", new Associado());
		findPaginated(1, model);
		return model;
	}

	@GetMapping(value = "/cancelarAssociado")
	public String Cancelar(Associado associado) {
		return "redirect:/cadastrarAssociado";
	}

	
	
	@GetMapping("/page/{pageNo}")
	public ModelAndView viewPaginated(@PathVariable(value = "pageNo") int pageNo, Associado associado) {
		ModelAndView model = new ModelAndView("/associado/formAssociado");
		findPaginated(pageNo, model);
		model.addObject("listaAssoc", Arrays.asList(TipoAssociado.values()));
		model.addObject("associado", new Associado());
		return model;

	}

	public void findPaginated(@PathVariable(value = "pageNo") int pageNo, ModelAndView model) {
		int pageSize = 8;
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<Associado> page = ar.findAll(pageable);
		List<Associado> listAssociado = page.getContent();
		model.addObject("totalPagina", page.getTotalPages());
		model.addObject("totalItem", page.getTotalElements());
		model.addObject("paginaAtual", pageNo);
		model.addObject("listaAssoc", Arrays.asList(TipoAssociado.values()));
		model.addObject("lista_associados", listAssociado);

	}

	@RequestMapping(value = "**/salvarAssociado", method = RequestMethod.POST)
	public ModelAndView salvarAssociado(@Valid Associado associado, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView("/associado/formAssociado");
		List<String> msg = new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
			mv.addObject("lista_associados", associados);
			mv.addObject("listaAssoc", Arrays.asList(TipoAssociado.values()));
			for (ObjectError objectErro : bindingResult.getAllErrors()) {
				msg.add(objectErro.getDefaultMessage());
			}

			mv.addObject("msg", msg);

			return mv;
		}
		Associado assocNovo = new Associado();
		assocNovo.setCodAssociado(associado.getCodAssociado());
		assocNovo.setNomeCompleto(associado.getNomeCompleto().toUpperCase());
		// assocNovo.setConvenio(associado.getConvenio());
		assocNovo.setCpf(associado.getCpf());
		assocNovo.setDataInclusao(associado.getDataInclusao());
		assocNovo.setEmail(associado.getEmail());
		assocNovo.setMargem(associado.getMargem());
		assocNovo.setMatricula(associado.getMatricula());
		assocNovo.setTelefone(associado.getTelefone());
		assocNovo.setTipoAssociado(associado.getTipoAssociado());
		ar.save(assocNovo);
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		mv.addObject("lista_associados", associados);
		mv.addObject("associado", new Associado());
		msg.add("Registro salvo com sucesso...");
		mv.addObject("msg", msg);

		return mv;

	}

	@RequestMapping("/associados")
	public ModelAndView ListaAssociados(Associado usuario) {
		ModelAndView mv = new ModelAndView("/associado/listaAssociado");
		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		mv.addObject("associados", associados);
		return mv;
	}

	@PostMapping("**/pesquisarAssociados")
	public ModelAndView pesquisarAssociado(@RequestParam("nomePesquisa") String nomeCompleto, 
		@RequestParam("matriculaPesquisa") String matricula, Associado associado) {
		ModelAndView modelAndView = new ModelAndView("/associado/formAssociado");
		modelAndView.addObject("listaAssoc", Arrays.asList(TipoAssociado.values()));
		if ((!matricula.equals("")) && (matricula.length()>=3)) {
			modelAndView.addObject("lista_associados", ar.findByMatricula(matricula));
		}else {
			String nome = nomeCompleto.toUpperCase();
			modelAndView.addObject("lista_associados", ar.findByNomeCompleto(nome));
		}
		modelAndView.addObject("associado", new Associado());
		return modelAndView;

	}

	@RequestMapping(value = "/{codAssociado}", method = RequestMethod.GET)
	public String detalhesAssociado(@PathVariable("codAssociado") Long codAssociado) {
		Associado associado = ar.findByCodAssociado(codAssociado);
		ar.save(associado);
		return "redirect:/{codAssociado}";
	}

	@GetMapping(value = "/deletarAssociado/{idAssociado}")
	public ModelAndView deletarAssociado(@PathVariable("idAssociado") Long idAssociado, Associado associado) {
		List<String> msg = new ArrayList<String>();
		ModelAndView model = new ModelAndView("/associado/formAssociado");
		model.addObject("listaAssoc", Arrays.asList(TipoAssociado.values()));
		Associado assoc = ar.findByCodAssociado(idAssociado);
		List<Movimento> mov = mr.findAllByAssociado(assoc);
		findPaginated(1, model);
		if (mov== null) {		
			ar.delete(assoc);
			msg.add("Registro excluido com sucesso...");
			model.addObject("msg", msg);
			return model;
		}
		 msg.add("Registro não pode ser excluido...");
		
		model.addObject("msg", msg);
		return model;
	}	
		

	@GetMapping("/editarAssociado/{idAssociado}")
	public ModelAndView editarAssociado(@PathVariable("idAssociado") Long idAssociado) {
		ModelAndView model = new ModelAndView("/associado/formAssociado");
		Associado assoc = ar.findByCodAssociado(idAssociado);
		model.addObject("associado", assoc);
		if (assoc.getTipoAssociado() == null) {
			TipoAssociado tipoAssociado = TipoAssociado.ATIVO;
			assoc.setTipoAssociado(tipoAssociado);
		}

		Iterable<Associado> associados = ar.findAll(Sort.by("nomeCompleto"));
		model.addObject("lista_associados", associados);
		model.addObject("listaAssoc", Arrays.asList(TipoAssociado.values()));
		return model;
	}

	/*
	 * Rotina para criar arquivo pdf para baixar
	 * 
	 * @GetMapping("/relatorioAssociado") public String gerarRelatorioAssociado()
	 * throws JRException, IOException { List<Associado> listAssociado =
	 * (List<Associado>) ar.findAll(); // String caminhoReal =
	 * "C:\\Fatepe\\financeiroAtepe\\src\\main\\resources\\static\\report\\Associado.jrxml";
	 * String nome = "/listadeAssociados2.pdf"; // String caminhoDestino =
	 * "/relatorios/"; String path =
	 * "C:\\\\Fatepe\\\\financeiroAtepe\\\\src\\\\main\\\\resources\\\\static\\\\report\\";
	 * File file = ResourceUtils.getFile(path + "ListaAssociados2.jrxml");
	 * JasperReport jr = JasperCompileManager.compileReport(file.getAbsolutePath());
	 * JRBeanCollectionDataSource dataSource = new
	 * JRBeanCollectionDataSource(listAssociado); JasperPrint jp =
	 * JasperFillManager.fillReport(jr, null, dataSource);
	 * JasperExportManager.exportReportToPdfFile(jp, path +
	 * "/listadeAssociados.pdf"); List<String> msg = new ArrayList<String>();
	 * ModelAndView mv = new ModelAndView("/associado/formAssociado");
	 * msg.add("Arquivo gerado com sucesso..."); mv.addObject("msg", msg);
	 * 
	 * return "redirect:/cadastrarAssociado"; }
	 */
	@RequestMapping(value = "**/reportAssociado", method = RequestMethod.GET)
	public void reportAssociado(HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		List<Associado> listAssociado = (List<Associado>) ar.findAll(Sort.by("nomeCompleto"));

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listAssociado);
		String path ="classpath:templates/";
		File file = ResourceUtils.getFile(path + "relatorio/listaAssociados.jrxml");

		JasperDesign design = JRXmlLoader.load(file);
		// Compila o template.
		JasperReport jasperReport = JasperCompileManager.compileReport(design);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

		JRPdfExporter pdfEsporter = new JRPdfExporter();
		pdfEsporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		ByteArrayOutputStream pdfReportStream = new ByteArrayOutputStream();
		pdfEsporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfReportStream));
		pdfEsporter.exportReport();

		// Obtém a resposta.
		response.setContentType("application/pdf");
		response.setHeader("Content-length", String.valueOf(pdfReportStream.size()));
		response.addHeader("Content-Disposition", "inline.filename=jasper.pdf.");

		OutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(pdfReportStream.toByteArray());
		responseOutputStream.close();
		pdfReportStream.close();

	}
}
