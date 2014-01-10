/*
 * Copyright (C) 2006 Kiran Mantripragada & Luiz Carlos Vieira
 * http://researcher.ibm.com/researcher/view.php?person=br-kiran
 * http://www.luiz.vieira.nom.br
 *
 * This file is part of the Narciso (Ambiente de Suporte ao Processamento
 * de Imagens para Vis�o Computacional).
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
 
package core.operations;

import java.io.File;
import java.util.*;
import core.errors.*;
import core.images.*;
import core.info.CHistogram;

/**
 * Classe para a implementa��o da opera��o de gera��o de imagem de histograma sobre todas as imagens dadas.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see COperation
 */

public class CHistogramImageOperation extends COperation
{
	/**
	 * Construtor da classe. Executa o construtor da classe pai.
	 * 
	 * @param sName Nome da opera��o.
	 * @param sDescription Descri��o da opera��o.
	 */
	public CHistogramImageOperation(String sName, String sDescription)
	{
		super(sName, sDescription);
	}

	/**
	 * M�todo sobrescrito da classe pai, para a implementa��o da execu��o da opera��o.
	 * 
	 *  @param pSource Vetor de objetos b�sicos do Java, contendo inst�ncias de uma ou mais imagens (CImage).
	 *  @param Objeto Properties do Java para conter o c�digo de erro caso a execu��o da opera��o n�o seja bem sucedida.
	 *  No momento da chamada, esse objeto deve conter as seguintes propriedades: width (largura em pixels da imagem a ser gerada),
	 *  height (altura em pixels da imagem a ser gerada), numbins (n�mero de bins para o histograma gerado) e binwidth (largura em
	 *  pixels para as barras representantes dos bins).
	 *  @return Vetor de objetos b�sicos do Java contendo o mesmo n�mero de imagens de entrada, resultado da gera��o de um gr�fico
	 *  de histograma para cada uma delas. Se um erro ocorrer, o retorno ser� null e o c�digo de
	 *  erro poder� ser obtido no par�metro "error" definido em pParams.
	 */
	@Override
	public Vector<Object> execute(Vector<Object> pSource, Properties pParams)
	{
		String sWidth = pParams.getProperty("width");
		String sHeight = pParams.getProperty("height");
		String sNumBins = pParams.getProperty("numbins");
		String sBinWidth = pParams.getProperty("binwidth");
		int iWidth, iHeight;
		int iNumBins = 256;
		int iBinWidth = 3;
		
		if(sWidth == null || sHeight == null)
		{
			pParams.put("error", String.valueOf(CErrors.ERROR_MISSING_PARAMETER));
			return null;			
		}
		
		iWidth = Integer.parseInt(sWidth);
		iHeight = Integer.parseInt(sHeight);
		
		if(iWidth <= 0 || iHeight <= 0)
		{
			pParams.put("error", String.valueOf(CErrors.ERROR_INVALID_PARAMETER));
			return null;
		}
		
		if(sNumBins != null)
		{
			iNumBins = Integer.parseInt(sNumBins);
			if(iNumBins <= 0)
				iNumBins = 256;
		}
		if(sBinWidth != null)
		{
			iBinWidth = Integer.parseInt(sBinWidth);
			if(iBinWidth <= 0)
				iNumBins = 3;			
		}

		Vector<Object> pRet = new Vector<Object>();		
		
		for(int i = 0; i < pSource.size(); i++)
		{
			Object pObj = pSource.get(i);
			if(!(pObj instanceof CImage))
			{
				pParams.put("error", String.valueOf(CErrors.ERROR_WRONG_SOURCE_TYPE));
				return null;
			}
			
			CImage pSrcImage = (CImage) pObj;

			CHistogram pHist = new CHistogram(iNumBins, pSrcImage);
		
			File pFile = new File(pSrcImage.getCurrentFileName());
			
			for(int iBand = 0; iBand < pHist.getNumBands(); iBand++)
			{
				CImage pTarget = pHist.createHistogramImage(iBand, iWidth, iHeight, pFile.getName() + " - banda " + String.valueOf(iBand), true);
				pRet.add(pTarget);
			}
		}
		
		if(pRet.size() <= 0)
			return null;
		else
			return pRet;
	}
}