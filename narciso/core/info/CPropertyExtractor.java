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
 
package core.info;

import java.awt.image.Raster;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.DFTDescriptor;

import core.images.*;

/**
 * Classe utilizada para a extração de propriedades a partir de imagens do Narciso (CImage). 
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see CImage
 * @see CPropertyBag
 * @see CPropertyItem
 * @see CPropertyComposite
 *
 */

public abstract class CPropertyExtractor
{
	/**
	 * Construtor da classe.
	 */
	protected CPropertyExtractor()
	{
	}

	/**
	 * Método estático e público utilizado para obter um pacote de propriedades a partir de uma imagem dada.
	 * 
	 * @param pImage Objeto CImage com a imagem a ser analisada.
	 * @return Instância de CPropertyBag com todas as propriedades extraídas da imagem.
	 */
	public static CPropertyBag getImageProperties(CImage pImage)
	{
		int iNumBins = 256;
		int iMaxValue = Integer.MIN_VALUE;
		int iMinValue = Integer.MAX_VALUE;
		
		CPropertyComposite pRoot, pComp;
		CPropertyItem pItem;
		
		pRoot = new CPropertyComposite("Propriedades");

		/*
		 * Nome da imagem ou do arquivo de imagem
		 */
		
		pItem = new CPropertyItem("Nome", CPropertyBag.CPropertyTypeEnum.STRING);
		pItem.setString(pImage.getCurrentFileName());
		pRoot.addProperty(pItem);

		/*
		 * Imagem colorida ou em tons de cinza
		 */
		
		pItem = new CPropertyItem("Colorida", CPropertyBag.CPropertyTypeEnum.BOOLEAN);
		pItem.setBool(pImage.IsColored());
		pRoot.addProperty(pItem);

		/*
		 * Tamanho da imagem
		 */
		
		pItem = new CPropertyItem("Largura (em pixels)", CPropertyBag.CPropertyTypeEnum.INTEGER);
		pItem.setInt(pImage.getWidth());
		pRoot.addProperty(pItem);

		/*
		 * Altura da imagem
		 */
		
		pItem = new CPropertyItem("Altura (em pixels)", CPropertyBag.CPropertyTypeEnum.INTEGER);
		pItem.setInt(pImage.getHeight());
		pRoot.addProperty(pItem);

		/*
		 * Número de bandas da imagem
		 */
		
		pItem = new CPropertyItem("Número de bandas", CPropertyBag.CPropertyTypeEnum.INTEGER);
		pItem.setInt(pImage.getPlanarImage().getNumBands());
		pRoot.addProperty(pItem);

		/*
		 * Histograma da imagem
		 */

		CHistogram pHist = new CHistogram(iNumBins, pImage);
		int iBands = pHist.getNumBands();
		
		for(int iBand = 0; iBand < iBands; iBand++)
		{
			pComp = new CPropertyComposite("Histograma - Banda " + String.valueOf(iBand));
			
			// Identificação do maior e menor valor da contagem do histograma
			
			for(int i = 0; i < iNumBins; i++)
			{
				int iCount = pHist.getCountingForValue(i, iBand);
				iMaxValue = Math.max(iMaxValue, iCount);
				iMinValue = Math.min(iMinValue, iCount);
			}
			
			// Maior valor de contagem
			
			pItem = new CPropertyItem("Maior Contagem", CPropertyBag.CPropertyTypeEnum.INTEGER);
			pItem.setInt(iMaxValue);
			pComp.addProperty(pItem);
			
			// Menor valor de contagem
			
			pItem = new CPropertyItem("Menor Contagem", CPropertyBag.CPropertyTypeEnum.INTEGER);
			pItem.setInt(iMinValue);
			pComp.addProperty(pItem);
			
			// Informação dos Bins 
			
			CPropertyComposite pBins = new CPropertyComposite("Bins de Contagem");
			for(int i = 0; i < iNumBins; i++)
			{
				// Contagem do bin de número #i
				pItem = new CPropertyItem("Bin de número: " + i, CPropertyBag.CPropertyTypeEnum.INTEGER);
				pItem.setInt(pHist.getCountingForValue(i, iBand));
				pBins.addProperty(pItem);
			}
			pComp.addProperty(pBins);
			pRoot.addProperty(pComp);
		}

		/*
		 * Histograma da imagem
		 */		
		
		pComp = new CPropertyComposite("Domínio das Freqüências");
		
		ParameterBlock pPB = new ParameterBlock();
		pPB.addSource(pImage.getPlanarImage());
		pPB.add(DFTDescriptor.SCALING_NONE);
		pPB.add(DFTDescriptor.REAL_TO_COMPLEX);
		
		RenderedOp pFreqs = JAI.create("dft", pPB);
		if(pFreqs != null)
		{
			Raster pDftData = pFreqs.getData();
			double aReal[] = pDftData.getSamples(0, 0, pImage.getWidth(), pImage.getHeight(), 0, (double []) null);
			double aImag[] = pDftData.getSamples(0, 0, pImage.getWidth(), pImage.getHeight(), 1, (double []) null);
			
			// Total de freqüências
			
			pItem = new CPropertyItem("Total de freqüências componentes", CPropertyBag.CPropertyTypeEnum.INTEGER);
			pItem.setInt(aReal.length);
			pComp.addProperty(pItem);
			
			// Valores reais e imaginários da freqüência básica
			
			CPropertyComposite pBas = new CPropertyComposite("Freqüência básica");
			
			pItem = new CPropertyItem("Valor no real", CPropertyBag.CPropertyTypeEnum.DOUBLE);
			pItem.setDouble(aReal[0]);
			pBas.addProperty(pItem);
			
			pItem = new CPropertyItem("Valor no imaginário", CPropertyBag.CPropertyTypeEnum.DOUBLE);
			pItem.setDouble(aImag[0]);
			pBas.addProperty(pItem);
			
			pComp.addProperty(pBas);

			// Valores reais e imaginários da freqüência n
			
			for(int iFreq = 1; iFreq <= 3; iFreq++)
			{
				CPropertyComposite pFreq = new CPropertyComposite("Freqüência de ordem " + String.valueOf(iFreq));
				
				pItem = new CPropertyItem("Valor no real", CPropertyBag.CPropertyTypeEnum.DOUBLE);
				pItem.setDouble(aReal[iFreq]);
				pFreq.addProperty(pItem);
				
				pItem = new CPropertyItem("Valor no imaginário", CPropertyBag.CPropertyTypeEnum.DOUBLE);
				pItem.setDouble(aImag[iFreq]);
				pFreq.addProperty(pItem);
				
				pComp.addProperty(pFreq);
			}
		}
		else
		{
			pItem = new CPropertyItem("Erro", CPropertyBag.CPropertyTypeEnum.STRING);
			pItem.setString("Não foi possível executar a Transformada de Fourier sobre a imagem para obter informações sobre suas freqüências componentes");
			pComp.addProperty(pItem);
		}
		pRoot.addProperty(pComp);
		
		// Terminando...
		
		return pRoot;
	}
}