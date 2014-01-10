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
 
package core.operations;

import java.awt.image.renderable.*;
import java.util.*;
import javax.media.jai.*;
import core.errors.*;
import core.images.*;

/**
 * Classe para a implementação da operação aritimética de adição sobre duas imagens.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see COperation
 */

public class CArithmeticAddOperation extends COperation
{
	/**
	 * Construtor da classe. Executa o construtor da classe pai.
	 * 
	 * @param sName Nome da operação.
	 * @param sDescription Descrição da operação.
	 */
	public CArithmeticAddOperation(String sName, String sDescription)
	{
		super(sName, sDescription);
	}

	/**
	 * Método sobrescrito da classe pai, para a implementação da execução da operação.
	 * 
	 *  @param pSource Vetor de objetos básicos do Java, contendo exatamente instâncias de duas imagens (CImage).
	 *  @param Objeto Properties do Java apenas para conter o código de erro caso a execução da operação não seja bem sucedida.
	 *  @return Vetor de objetos básicos do Java contendo apenas uma imagem, resultado da adição das duas imagens dadas. Se um erro
	 *  ocorrer, o retorno será null e o código de erro poderá ser obtido no parâmetro "error" definido em pParams.
	 */
	@Override
	public Vector<Object> execute(Vector<Object> pSource, Properties pParams)
	{
		if(pSource.size() < 2)
		{
			pParams.put("error", String.valueOf(CErrors.ERROR_WRONG_NUMBER_OF_SOURCES));
			return null;
		}
		
		Object pObj = pSource.get(0);
		if(!(pObj instanceof CImage))
		{
			pParams.put("error", String.valueOf(CErrors.ERROR_WRONG_SOURCE_TYPE));
			return null;
		}
			
		PlanarImage pSrc1 = ((CImage) pObj).getPlanarImage();

		pObj = pSource.get(1);
		if(!(pObj instanceof CImage))
		{
			pParams.put("error", String.valueOf(CErrors.ERROR_WRONG_SOURCE_TYPE));
			return null;
		}
			
		PlanarImage pSrc2 = ((CImage) pObj).getPlanarImage();

		ParameterBlock pPB = new ParameterBlock();
		pPB.addSource(pSrc1);
		pPB.addSource(pSrc2);
		
		PlanarImage pTemp = JAI.create("add", pPB, null);
		
		CImage pTarget = new CImage(10, 10, false);
		pTarget.setPlanarImage(pTemp);

		Vector<Object> pRet = new Vector<Object>();
		pRet.add((Object) pTarget);
		
		return pRet;
	}
}