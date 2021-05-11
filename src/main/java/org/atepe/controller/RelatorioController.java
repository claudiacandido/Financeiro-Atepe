package org.atepe.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.atepe.model.Fechamento;
import org.atepe.model.Lancamento;
import org.atepe.model.Movimento;
import org.atepe.repository.FechamentoRepository;
import org.atepe.repository.LancamentoRepository;
import org.atepe.repository.MovimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
public class RelatorioController {

	@Autowired
	private FechamentoRepository fr;

	@Autowired
	private MovimentoRepository mr;

	@Autowired
	private LancamentoRepository lr;

	@RequestMapping(value = "**/reportFechamento", method = RequestMethod.GET)
	public void reportFechamento(HttpServletResponse response, @RequestParam("periodoPesquisa") String periodoPesquisa)
			throws Exception {
		response.setContentType("text/html;charset=UTF-8");
		List<Fechamento> listFechamento = fr.findAllByPeriodo(periodoPesquisa, Sort.by("associado.nomeCompleto"));
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listFechamento);
		String path = "classpath:templates/";
		File file = ResourceUtils.getFile(path + "relatorio/listaFecPeriodo.jrxml");
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

	@GetMapping(value = "/consFechamentoPeriodo")
	public String consFechamentoPeriodo(Model model) {
		return "visualizar/formListaFecPeriodo";
	}

	@RequestMapping(value = "**/reportPagtoFecAssociado", method = RequestMethod.GET)
	public void reportPagtoFecAssociado(HttpServletResponse response,
			@RequestParam("periodoPesquisa") String periodoPesquisa) throws Exception {
		response.setContentType("text/html");
		List<Fechamento> listFechamento = fr.findAllByPeriodoPago(periodoPesquisa, Sort.by("associado.nomeCompleto"));

		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listFechamento);
		String path = "classpath:templates/";
		File file = ResourceUtils.getFile(path + "relatorio/fecPagtoPeriodo.jrxml");

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

	@GetMapping(value = "/consPagAssocPeriodo")
	public String consPagAssocPeriodo(Model model) {
		return "visualizar/formFecPagtoPeriodo";
	}

	@RequestMapping(value = "**/reportRecFecAssociado", method = RequestMethod.GET)
	public void reportRecFecAssociado(HttpServletResponse response,
			@RequestParam("periodoPesquisa") String periodoPesquisa) throws Exception {
		response.setContentType("text/html");
		List<Fechamento> listFechamento = fr.findAllByPeriodoApagar(periodoPesquisa, Sort.by("associado.nomeCompleto"));
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listFechamento);
		String path = "classpath:templates/";
		File file = ResourceUtils.getFile(path + "relatorio/fecApagarPeriodo.jrxml");

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

	@GetMapping(value = "/consRecAssocPeriodo")
	public String consRecAssocPeriodo(Model model) {
		return "visualizar/formFecRecPeriodo";
	}

	@RequestMapping(value = "**/reportExtratoAssociado", method = RequestMethod.GET)
	public void reportExtratoAssociado(HttpServletResponse response,
			@RequestParam("periodoPesquisa") String periodoPesquisa) throws Exception {
		response.setContentType("text/html");
		List<Movimento> listMovim = mr.findAllByPeriodo(periodoPesquisa, Sort.by("associado.nomeCompleto"));
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listMovim);
		String path = "classpath:templates/";

		File file = ResourceUtils.getFile(path + "relatorio/lanExtratoPeriodo.jrxml");

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

	@GetMapping(value = "/consExtratoPeriodo")
	public String consExtratoPeriodo(Model model) {
		return "visualizar/formExtratoPeriodo";
	}

	@RequestMapping(value = "**/reportLancamConvenio", method = RequestMethod.GET)
	public void reportLancamConvenio(HttpServletResponse response,
			@RequestParam("periodoPesquisa") String periodoPesquisa) throws Exception {
		response.setContentType("text/html");
		List<Lancamento> listLanc = lr.findAllByPeriodo(periodoPesquisa, Sort.by("convenio.razaoSocial"));
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listLanc);
		String path = "classpath:templates/";
		File file = ResourceUtils.getFile(path + "relatorio/pagamentoConvenio.jrxml");
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

	@GetMapping(value = "/consConvenioPeriodo")
	public String consConvenioPeriodo(Model model) {
		return "visualizar/formConvenioPeriodo";
	}

}
