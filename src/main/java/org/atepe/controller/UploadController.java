package org.atepe.controller;

import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController {

	@RequestMapping(value = "/importarArquivo", method = RequestMethod.GET)
	public String Form() {
		return "importar/importarArquivo";

	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uploadFile(@RequestParam("arquivoImportado") MultipartFile multipartFile) throws IOException {
		String filePath = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		System.out.println("aquiii foiiiii"+filePath);
		
		String extensaoDoArquivo = FilenameUtils.getExtension(filePath);
        System.out.println("esctensssaoooo::"+extensaoDoArquivo);
		// Abrindo o arquivo e recuperando a planilha
	
		Workbook  workbook = new HSSFWorkbook(multipartFile.getInputStream()); 
		  // Seta a aba 
		Sheet sheet = workbook.getSheetAt(0);
		int linhas = sheet.getLastRowNum(); 
		for (int i = 1; i < linhas; i++) { 
		 int cd = (int) sheet.getRow(i).getCell(3).getNumericCellValue(); 
		 if (i==1) {
		    
		 }	
			 int ca = (int) sheet.getRow(i).getCell(0).getNumericCellValue(); 
			  Cell cb = sheet.getRow(i).getCell(1); 
			  Cell cc = sheet.getRow(i).getCell(2); 
			  double ce = sheet.getRow(i).getCell(4).getNumericCellValue();
		 
	  System.out.println(ca + "=" + cb + "=" + cc + "=" + cd + "=" + ce); 
	  }
		  System.out.println("aquiii foiiiii acabou");
		  workbook.close();
		return "redirect:/";
	}
}
