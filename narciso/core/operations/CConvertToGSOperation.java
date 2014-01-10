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
 * Classe para a implementa��o da opera��o de convers�o para escala de cinza, sobre todas as imagens dadas.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see COperation
 */

public class CConvertToGSOperation extends COperation
{
	/**
	 * Construtor da classe. Executa o construtor da classe pai.
	 * 
	 * @param sName Nome da opera��o.
	 * @param sDescription Descri��o da opera��o.
	 */
	public CConvertToGSOperation(String sName, String sDescription)
	{
		super(sName, sDescription);
	}

	/**
	 * M�todo sobrescrito da classe pai, para a implementa��o da execu��o da opera��o.
	 * 
	 *  @param pSource Vetor de objetos b�sicos do Java, contendo inst�ncias de uma ou mais imagens (CImage).
	 *  @param Objeto Properties do Java apenas para conter o c�digo de erro caso a execu��o da opera��o n�o seja bem sucedida.
	 *  @return Vetor de objetos b�sicos do Java contendo o mesmo n�mero de imagens de entrada, resultado da convers�o de cada uma delas para escala de cinza. Se um erro
	 *  ocorrer, o retorno ser� null e o c�digo de erro poder� ser obtido no par�metro "error" definido em pParams.
	 */
	@Override
	public Vector<Object> execute(Vector<Object> pSource, Properties pParams)
	{
		int i;
		Vector<Object> pRet = new Vector<Object>();
		
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

			if(!pSrcImage.IsColored())
			{
				pParams.put("error", String.valueOf(CErrors.ERROR_WRONG_SOURCE_TYPE));
				return null;
			}
			
			iWidth = pSrcImage.getWidth();
			iHeight = pSrcImage.getHeight();

			CImage pTgtImage = new CImage(iWidth, iHeight, false);
			
			for(x = 0; x < iWidth; x++)
			{
				for(y = 0; y < iHeight; y++)
				{
					CGrayScalePixel pSrcPixel = null;
					CColorPixel pAux = (CColorPixel) pSrcImage.getPixel(x, y); 
					if(pAux != null)
						pSrcPixel = pAux.toGrayScale();
					pTgtImage.setPixel(x, y, pSrcPixel);
				}
			}
			
			pRet.add((Object) pTgtImage);
		}
		
		return pRet;
	}
}
 
