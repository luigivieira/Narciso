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

import java.util.*;

import core.errors.CErrors;
import core.images.*;

/**
 * Classe para a implementa��o da opera��o de limiariza��o (thresholding) sobre todas as imagens dadas.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see COperation
 */

public class CThresholdingOperation extends COperation
{
	/**
	 * Construtor da classe. Executa o construtor da classe pai.
	 * 
	 * @param sName Nome da opera��o.
	 * @param sDescription Descri��o da opera��o.
	 */
	public CThresholdingOperation(String sName, String sDescription)
	{
		super(sName, sDescription);
	}

	/**
	 * M�todo sobrescrito da classe pai, para a implementa��o da execu��o da opera��o.
	 * 
	 *  @param pSource Vetor de objetos b�sicos do Java, contendo inst�ncias de uma ou mais imagens (CImage).
	 *  @param Objeto Properties do Java para conter o c�digo de erro caso a execu��o da opera��o n�o seja bem sucedida.
	 *  No momento da execu��o, esse objeto deve conter a seguinte propriedade: threshold (valor para limiariza��o, entre
	 *  0 e 255).
	 *  @return Vetor de objetos b�sicos do Java contendo o mesmo n�mero de imagens de entrada, resultado da opera��o de limiariza��o em cada uma delas. Se um erro
	 *  ocorrer, o retorno ser� null e o c�digo de erro poder� ser obtido no par�metro "error" definido em pParams.
	 */
	@Override
	public Vector<Object> execute(Vector<Object> pSource, Properties pParams)
	{
		String sThreshold = pParams.getProperty("threshold");
		
		if(sThreshold == null)
		{
			pParams.put("error", String.valueOf(CErrors.ERROR_MISSING_PARAMETER));
			return null;
		}
		
		int i, iThreshold = Integer.parseInt(sThreshold);
		Vector<Object> pRet = new Vector<Object>();
		
		if(iThreshold < 0 || iThreshold > 255)
		{
			pParams.put("error", String.valueOf(CErrors.ERROR_INVALID_PARAMETER));
			return null;
		}			
		
		if(pSource.size() <= 0)
		{
			pParams.put("error", String.valueOf(CErrors.ERROR_WRONG_NUMBER_OF_SOURCES));
			return null;
		}			
		
		for(i = 0; i < pSource.size(); i++)
		{
			int iWidth, iHeight;
			int x, y;
			
			Object pObj = pSource.get(i);
			if(!(pObj instanceof CImage))
			{
				pParams.put("error", String.valueOf(CErrors.ERROR_WRONG_SOURCE_TYPE));
				return null;
			}
			
			CImage pSrcImage = (CImage) pObj;
			
			iWidth = pSrcImage.getWidth();
			iHeight = pSrcImage.getHeight();

			CImage pTgtImage = new CImage(iWidth, iHeight, false);
			
			for(x = 0; x < iWidth; x++)
			{
				for(y = 0; y < iHeight; y++)
				{
					CPixel pTgtPixel;
					CGrayScalePixel pSrcPixel = null;
					
					if(pSrcImage.IsColored())
					{
						CColorPixel pAux = (CColorPixel) pSrcImage.getPixel(x, y); 
						if(pAux != null)
							pSrcPixel = pAux.toGrayScale();
					}
					else
						pSrcPixel = (CGrayScalePixel) pSrcImage.getPixel(x, y);
					
					int iBrightness = pSrcPixel.getBrightness();
					if(iBrightness <= iThreshold)
						pTgtPixel = new CGrayScalePixel(0);
					else
						pTgtPixel = new CGrayScalePixel(255);
					
					pTgtImage.setPixel(x, y, pTgtPixel);
				}
			}
			
			pRet.add((Object) pTgtImage);
		}
		
		return pRet;
	}
}
 
