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

import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.media.jai.*;
import core.images.*;

/**
 * Classe utilizada para a geração de histogramas de imagens.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see CImage
 *
 */

public class CHistogram
{
	/** Membro privado utilizado para armazenar o número de bandas do histograma. */
	private int m_iNumBands;
	
	/** Membro privado utilizado para armazenar o número de bins do histograma. */
	private int m_iNumBins;
	
	/** Membro privado utilizado para armazenar a contagem do histogram. */
	private int m_aData[][];
	
	/**
	 * Construtor da classe.
	 * 
	 * @param iNumBins Valor com o número de bins para a geração do histograma.
	 * @param pImage Imagem (CImage) para a geração do histograma.
	 */
	public CHistogram(int iNumBins, CImage pImage)
	{
	    ParameterBlock pPB = new ParameterBlock();
	    pPB.addSource(pImage.getPlanarImage());
	    pPB.add(null);
	    pPB.add(1);
	    pPB.add(1);
	    pPB.add(new int[]{iNumBins});
	    pPB.add(new double[]{0});
	    pPB.add(new double[]{iNumBins});
	    PlanarImage pPlanImg = JAI.create("histogram", pPB);
	    
	    m_iNumBands = pImage.getPlanarImage().getNumBands();
	    m_iNumBins = iNumBins;
	    
	    Histogram pHistogram = (javax.media.jai.Histogram) pPlanImg.getProperty("histogram");

	    m_aData = new int[m_iNumBins][m_iNumBands];
	    
	    boolean bFirstTime = true;
	    
	    for(int i = 0; i < iNumBins; i++)
	    {
    		for(int iBand = 0; iBand < m_iNumBands; iBand++)
    		{
    			if(bFirstTime)
    				m_aData[i][iBand] = pHistogram.getBins(iBand)[i];
    			else
    				m_aData[i][iBand] += pHistogram.getBins(iBand)[i];
    		}
    		bFirstTime = false;
	    }
	}

	/**
	 * Método getter de obtenção do número de bandas do histograma.
	 * 
	 * @return Número de bandas do histograma.
	 */
	public int getNumBands()
	{
		return m_iNumBands;
	}

	/**
	 * Método getter de obtenção do número de bins do histograma.
	 * 
	 * @return Número de bins do histograma.
	 */
	public int getNumBins()
	{
		return m_iNumBins;
	}
	
	/**
	 * Método getter de obtenção do valor de contagem do histograma para os dados bin e banda.
	 * 
	 * @param iValue Número do bin para obtenção do valor de contagem.
	 * @param iBand Número da banda para obtenção do valor de contagem.
	 * @return Valor da contagem, ou -1 se os valores do bin e banda estiverem fora dos limites do histograma.
	 */
	public int getCountingForValue(int iValue, int iBand)
	{
		if(iValue < 0 || iValue >= m_iNumBins)
			return -1;
		else if(iBand < 0 || iBand >= m_iNumBands)
			return -1;
		else
			return m_aData[iValue][iBand];
	}

	/**
	 * Cria uma imagem (CImage) colorida do gráfico do histograma, segundo definições dadas.
	 * 
	 * @param iBand Valor da banda para a geração da imagem.
	 * @param iWidth Largura em pixels da imagem a ser criada.
	 * @param iHeight Altura em pixels da imagem a ser criada.
	 * @param sTitle Texto para o título da imagem.
	 * @param bDecorated Valor lógico indicativo se a imagem do gráfico de histograma deverá ser decorada (inclusão de legendas, título, linhas, etc).
	 *  
	 * @return Objeto CImage com a imagem do gráfico de histograma criado.
	 */
	public CImage createHistogramImage(int iBand, int iWidth, int iHeight, String sTitle, boolean bDecorated)
	{
		Color pBackgroundColor = Color.BLACK;
		Color pBarColor = new Color(255,255,200);
		Color pMarksColor = new Color(100,180,255);
		
		int iTopMargin = 5, iBottomMargin = 7;
		
		if(bDecorated)
		{
			iTopMargin = 50;
			iBottomMargin = 30;			
		}
		else
		{
			iTopMargin = 0;
			iBottomMargin = 2;
		}
		
		Insets pArea = new Insets(iTopMargin, iTopMargin, iHeight - iBottomMargin, iWidth - iBottomMargin);
		int iAreaHeight = pArea.bottom - pArea.top;
		int iAreaWidth = pArea.right - pArea.left;
		
		int iNumBins = m_aData.length;

		int iBinWidth = (int) Math.ceil((pArea.right - pArea.left) / (1.0 * iNumBins));
		
		/*
		 * Se o numero de pixels por bin for menor que zero, significa que o tamanho informado
		 * não é suficientemente grande para comportar o numero de bins do histograma. Por isso,
		 * força um novo tamanho para a imagem com base em um pixel por bin.
		 */
		
		if(iBinWidth == 0)
		{
			iBinWidth = 1;
			iWidth = iNumBins + iTopMargin + iBottomMargin;
			pArea = new Insets(iTopMargin, iTopMargin, iHeight - iBottomMargin, iWidth - iBottomMargin);
			iAreaWidth = pArea.right - pArea.left;
		}
		
		/*
		 * Se o número de bins do histograma não cabem perfeitamente no tamanho de imagem requerido,
		 * o tamanho é então "levemente" ajustado para o número de bins de acordo com o tamanho de um bin
		 * calculado anteriormente  
		 */
		
		if(iAreaWidth % iNumBins != 0)
		{
			iWidth = (iNumBins * iBinWidth) + iTopMargin + iBottomMargin;
			pArea = new Insets(iTopMargin, iTopMargin, iHeight - iBottomMargin, iWidth - iBottomMargin);
			iAreaWidth = pArea.right - pArea.left;
		}

		/* Criação do device para desenho da imagem */ 
		
		BufferedImage pBuff = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D pGraph = pBuff.createGraphics();
		
		/* Desenho do fundo do gráfico */
		
		pGraph.setColor(pBackgroundColor);
		pGraph.fillRect(0, 0, iWidth, iHeight);

		/* Desenho do quadro delimitador da area de plotagem */
		
		pGraph.setColor(pMarksColor);
		pGraph.drawRect(pArea.left, pArea.top, iAreaWidth, iAreaHeight);
		
		/* Obtenção do maior valor de contagem de pixels */
		
	    double dMaxCount = Integer.MIN_VALUE;
	    for(int i = 0; i < m_aData.length; i++)
	    	dMaxCount = Math.max(dMaxCount, m_aData[i][iBand]);
		
	    /* Desenho das barras com as contagens dos pixels (bins) */
	    
	    pGraph.setColor(pBarColor);
		for(int i = 0; i < iNumBins; i++)
		{
			int x = pArea.left + i * iBinWidth;
			int iBarHeight = (int) (m_aData[i][iBand] * iAreaHeight / (1.0 * dMaxCount));
			int iBarTop = pArea.bottom - iBarHeight;
			pGraph.drawRect(x, iBarTop, iBinWidth, iBarHeight);
		}
	    
		if(bDecorated)
		{
			Font pFontSmall = new Font("monospaced",0,10);
			int iIntervalMultiplier = 1;
			int iInterval = 8;
			
			/*
			 * Desenho da legenda dos bins (legenda de baixo) 
			 */
			
			pGraph.setColor(pMarksColor);
			
			pGraph.setFont(pFontSmall);			
			FontMetrics metrics = pGraph.getFontMetrics();
			
			int iHalfFontHeight = metrics.getHeight() / 2;
			
			for(int i = 0; i <= iNumBins; i++)
			{
				if((i % iInterval) == 0)
				{
					String sLabel = String.valueOf(iIntervalMultiplier * i);
					int iTextHeight = metrics.stringWidth(sLabel);
					
					pGraph.translate(pArea.left + i * iBinWidth + iHalfFontHeight, pArea.bottom + iTextHeight + 2);
					pGraph.rotate(-Math.PI / 2);
					pGraph.drawString(sLabel, 0, 0);
					pGraph.rotate(Math.PI / 2);
					pGraph.translate(-(pArea.left + i * iBinWidth + iHalfFontHeight), -(pArea.bottom + iTextHeight + 2));
				}
			}

			/*
			 * Desenho da legenda dos valores de contagem (legenda da esquerda) 
			 */
			
			int iVerticalTicks = 10;
			int iStep = (int) (dMaxCount / iVerticalTicks);
			for(int i = 0; i <= iVerticalTicks; i++)
			{
				String sLabel;
				if(i == iVerticalTicks)
					sLabel = String.valueOf(dMaxCount);				
				else
					sLabel = String.valueOf(i * iStep);
				
				int iTextWidth = metrics.stringWidth(sLabel);
				
				pGraph.drawString(sLabel, pArea.left - 2 - iTextWidth, pArea.bottom - i * (iAreaHeight / iVerticalTicks));      
			}

			/*
			 * Desenho do título
			 */
			
		    if(sTitle != null)
		    {
		    	Font pFontLarge = new Font("default", Font.ITALIC, 20);
		    	pGraph.setFont(pFontLarge);
		    	pGraph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			    metrics = pGraph.getFontMetrics();
			    
		    	int iTextWidth = metrics.stringWidth(sTitle);
		    	pGraph.drawString(sTitle, (iWidth - iTextWidth) / 2,28);
		    }
		}
		
		/*
		 * Criação do objeto CImage para retorno 
		 */
		
		PlanarImage pTemp = PlanarImage.wrapRenderedImage(pBuff);
	    CImage pRet = new CImage(iWidth, iHeight, true);
	    pRet.setPlanarImage(pTemp);
		
		return pRet;
	}
}