package org.atepe.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.atepe.model.Associado;
import org.atepe.repository.AssociadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.ModelAndView;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class GerarRelatorio {
	
	@Autowired
	private AssociadoRepository ar;


	public String gerarRelatorioAssociado(String nomeRel, String caminho) throws JRException, IOException {
		List<Associado> listAssociado = (List<Associado>) ar.findAll();
		// para compilar
		System.out.println("teste lista" + listAssociado.size());
		String path = "C:/Fatepe/Fatepe/src/main/resources/report";
		File file = ResourceUtils.getFile(path + "/ListaAssociadosNovo.jrxml");
		JasperReport jr = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listAssociado);
		JasperPrint jp = JasperFillManager.fillReport(jr, null, dataSource);
		JasperExportManager.exportReportToPdfFile(jp, path + "/listadeAssociados.pdf");
		List<String> msg = new ArrayList<String>();
		ModelAndView mv = new ModelAndView("/associado/formAssociado");
		msg.add("Arquivo gerado com sucesso...");
		mv.addObject("msg", msg);

		return "redirect:/cadastrarAssociado";
	}

	public void reportAssociado(HttpServletResponse response, String nomeRel, String caminho) throws Exception {
		response.setContentType("text/html");
		List<Associado> listAssociado = (List<Associado>) ar.findAll();
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(listAssociado);

		InputStream jrmlInput = this.getClass().getResourceAsStream("/report/ListaAssociadosNovo.jrxml");
		JasperDesign design = JRXmlLoader.load(jrmlInput);

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
