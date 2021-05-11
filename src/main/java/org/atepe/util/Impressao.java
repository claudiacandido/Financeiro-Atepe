package org.atepe.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;

import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class Impressao {
	public void gerarRelatorio(JRBeanCollectionDataSource dataSource, String caminhoReal, String caminhoDestino,
			String nome) throws JRException {
		try {
			File file;

			file = ResourceUtils.getFile(caminhoReal);

			JasperReport jr = JasperCompileManager.compileReport(file.getAbsolutePath());
			JasperPrint jp = JasperFillManager.fillReport(jr, null, (Connection) dataSource);
			JasperExportManager.exportReportToPdfFile(jp, caminhoDestino + nome);
		}

		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
