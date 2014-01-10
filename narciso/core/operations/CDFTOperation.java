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

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.renderable.ParameterBlock;
import java.util.Properties;
import java.util.Vector;

import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.operator.DFTDescriptor;

import core.images.CImage;

/**
 * Classe para a implementação da operação de obtenção da imagem de magnitude das freqüências segundo a Transformada de Fourier
 * para todas as imagens dadas.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see COperation
 */

public class CDFTOperation extends COperation
{
	/**
	 * Construtor da classe. Executa o construtor da classe pai.
	 * 
	 * @param sName Nome da operação.
	 * @param sDescription Descrição da operação.
	 */
	public CDFTOperation(String sName,String sDescription)
	{
		super(sName,sDescription);
	}

	/**
	 * Método sobrescrito da classe pai, para a implementação da execução da operação.
	 * 
	 *  @param pSource Vetor de objetos básicos do Java, contendo instâncias de uma ou mais imagens (CImage).
	 *  @param Objeto Properties do Java apenas para conter o código de erro caso a execução da operação não seja bem sucedida.
	 *  @return Vetor de objetos básicos do Java contendo o mesmo número de imagens de entrada, resultado da obtenção da imagem de magnitude de cada uma delas. Se um erro
	 *  ocorrer, o retorno será null e o código de erro poderá ser obtido no parâmetro "error" definido em pParams.
	 */
	@Override
	public Vector<Object> execute(Vector<Object> pSource, Properties pParams)
	{
		Vector<Object> pRet = new Vector<Object>();
		
		for(int i = 0; i < pSource.size(); i++)
		{
			Object pObj = pSource.get(i);
			if(!(pObj instanceof CImage))
				return null;
			
			PlanarImage pSrcImage = ((CImage) pObj).getPlanarImage();

			ParameterBlock pPB = new ParameterBlock();
			
			pPB.addSource(pSrcImage);
			pPB.add(DFTDescriptor.SCALING_NONE);
			pPB.add(DFTDescriptor.REAL_TO_COMPLEX);
			
			PlanarImage pTemp = (PlanarImage) JAI.create("dft", pPB);
			if(pTemp == null)
				return null;
		
			ColorModel cCM = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_GRAY), null, false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);			
			
			pPB.removeSources(); 
			pPB.removeParameters();
			ImageLayout iIL = new ImageLayout();
			iIL.setColorModel(cCM);
	        iIL.setSampleModel(cCM.createCompatibleSampleModel(pTemp.getWidth(),pTemp.getHeight()));
	        RenderingHints rRH = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, iIL);
	        pPB.addSource(pTemp);		    
		    // Calculate the magnitude.
		    PlanarImage pMagnitudeImage = JAI.create("magnitude", pPB, rRH);
		   		    
			pPB.removeSources(); 
			pPB.removeParameters();
			pPB.addSource(pMagnitudeImage);
			pPB.add(pMagnitudeImage.getWidth()/2);
			pPB.add(pMagnitudeImage.getWidth()/2);
			pPB.add(pMagnitudeImage.getHeight()/2);
			pPB.add(pMagnitudeImage.getHeight()/2);			
			pPB.add(BorderExtender.createInstance(BorderExtender.BORDER_WRAP));
			PlanarImage pMagnitudeImageExtended = JAI.create("border", pPB,null); 
			
			pPB.removeSources(); 
			pPB.removeParameters();
			pPB.addSource(pMagnitudeImageExtended);
			pPB.add((float)pMagnitudeImageExtended.getMinX());
			pPB.add((float)pMagnitudeImageExtended.getMinY());
			pPB.add((float)pMagnitudeImageExtended.getWidth()/2);
			pPB.add((float)pMagnitudeImageExtended.getHeight()/2);		
			PlanarImage pMagnitudeImageCropped = JAI.create("crop", pPB,null); 
			 
			CImage pTgtImage = new CImage(pMagnitudeImageCropped.getWidth(),pMagnitudeImageCropped.getHeight(), false);
			pTgtImage.setPlanarImage(pMagnitudeImageCropped);
			
			pRet.add((Object) pTgtImage);
		}
		
		if(pRet.size() > 0)
			return pRet;
		else
			return null;
	}
}
 
