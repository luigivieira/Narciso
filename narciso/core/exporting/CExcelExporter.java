/*
 * Copyright (C) 2006 Kiran Mantripragada & Luiz Carlos Vieira
 * http://researcher.ibm.com/researcher/view.php?person=br-kiran
 * http://www.luiz.vieira.nom.br
 *
 * This file is part of the Narciso (Ambiente de Suporte ao Processamento
 * de Imagens para Visão Computacional).
 *
 * Narciso is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Narciso is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
 
package core.exporting;

import java.io.*;
import java.util.*;

import org.apache.poi.hssf.usermodel.*;

import GUI.*;
import core.errors.*;
import core.images.*;
import core.info.*;

/**
 * Essa classe implementa a exportação das propriedades de imagens para o formato XLS. O formato XLS é 
 * o formato das planilhas desenvolvidas pela Microsoft para o Microsoft Excel(R).
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see IExporter
 */
public class  CExcelExporter implements IExporter
{
	/**
	 * Método utilizado para exportar as propriedades da imagem dada para o arquivo dado, no formato XLS. 
	 * 
	 * @param pImage Objeto CImage com a imagem de onde as propriedades serão extraídas.
	 * @param sFile Nome do arquivo a ser gerado.
	 * 
	 * @return Retorna CErrors.SUCCESS se a exportação foi realizada com sucesso, ou um código com o erro ocorrido.
	 */	
	public int exportInfo(CImage pImage, String sFile)
	{
		FileOutputStream fsExcelFile ;		
		try
		{
			fsExcelFile = new FileOutputStream(sFile);	
			HSSFWorkbook wWorkBook =  new HSSFWorkbook();
		
			HSSFSheet sSheet= wWorkBook.createSheet("Propriedades da Imagem");
			
			HSSFRow row = sSheet.createRow(0);
			HSSFCell cell0 = row.createCell((short)0) ;
			HSSFCell cell1 = row.createCell((short)1) ; 	
			cell0.setCellValue("Propriedade");
			cell1.setCellValue("Valor");				

			CPropertyBag pBag = CPropertyExtractor.getImageProperties(pImage);

			Map.Entry pPair;
			int i = 1;
			Map<String, String> pMap = CPropertyBag.toMap(pBag); 

			Iterator it = pMap.entrySet().iterator();
			while(it.hasNext())
			{
				pPair = (Map.Entry) it.next();
				String sName = (String) pPair.getKey();
				String sValue = (String) pPair.getValue();

				row = sSheet.createRow(i);
				cell0 = row.createCell((short)0) ;
				cell1 = row.createCell((short)1) ;
				cell0.setCellValue(sName);
				cell1.setCellValue(sValue);
				
				i++;
			}
			
			wWorkBook.write(fsExcelFile);
			fsExcelFile.close();
		}	
		catch(Exception e)
		{
			CExceptionDialog.showException(e);
			return CErrors.ERROR_UNEXPECTED_ERROR;
		}
		
		return CErrors.SUCCESS;
	}
}