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
 
package core.images;

/**
 * CImage é a classe para representação de uma imagem como um conjunto pixels dispostos em uma matriz bidimensional.
 * Os pixels são representados pela classe CPixel e são posicionados de acordo com um plano cartesiano imaginário, 
 * de modo que seu acesso e manipulação se dá através de duas coordenadas inteiras X e Y. O número de pixels em cada
 * "linha" do eixo Y é o mesmo, assim como o número de pixels em cada "coluna" do eixo X.
 *   
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 * 
 * @see CPixel
 * @see CImageObject
 *
 */

import java.awt.Color;
import java.awt.image.*;
import javax.media.jai.*;
import core.errors.*;

public class CImage
{
	/**
	 * Membro privado utilizado para armazenar o valor da altura do Pixel Array
	 * em pixels.
	 */
	private int m_iHeight;

	/**
	 * Membro privado utilizado para armazenar o valor da largura do Pixel Array
	 * em pixels.
	 */
	private int m_iWidth;
	
	/**
	 * Membro privado utilizado para armazenar a indicação se o Pixel Array representa uma imagem
	 * colorida ou em escala de cinza.
	 */
	private boolean m_bColored;

	/** Membro protegido utilizado para armazenar o nome do último arquivo manipulado (carregado/gravado). */
	protected String m_sCurrentFile;	
	
	/** Membro privado utilizado para armazenar o buffer de pixels formadores da imagem. */
	private BufferedImage m_pBuffer;

	/**
	 * Construtor da classe CPixelArray. Inicializa a área de armazenamento dos pixels componentes.
	 * 
	 * @param iWidth Largura em pixels da imagem
	 * @param iHeight Altura em pixels da imagem
	 * @param bColored Valor lógico (true ou false) indicando se a imagem é ou não colorida.
	 * A definição dessa valor indicará se o objeto CImage conterá uma matrix de pixeis coloridos
	 * (CColorPixel) ou em escala de cinza (CGrayScalePixel).
	 */
	public CImage(int iWidth, int iHeight, boolean bColored)
	{
		m_iWidth = iWidth;
		m_iHeight = iHeight;
		
		m_pBuffer = null;
	
		m_bColored = bColored;
		
		if(m_bColored)
			m_pBuffer = new BufferedImage(m_iWidth, m_iHeight, BufferedImage.TYPE_INT_RGB);
		else
			m_pBuffer = new BufferedImage(m_iWidth, m_iHeight, BufferedImage.TYPE_BYTE_GRAY);
		
		m_sCurrentFile = toString();
	}

	/**
	 * Método getter que obtém a altura em pixels da imagem.
	 * 
	 * @return Altura em pixels da imagem.
	 */
	public int getHeight()
	{
		return m_iHeight;
	}

	/**
	 * Método getter que obtém a largura em pixels da imagem.
	 * 
	 * @return Largura em pixels da imagem.
	 */
	public int getWidth()
	{
		return m_iWidth;
	}

	/**
	 * Retorna a indicação se o objeto representa uma imagem colorida (true) ou em escala de cinza (false).
	 * 
	 * @return True se os pixels componentes da imagem forem coloridos, false caso contrário.
	 */
	public boolean IsColored()
	{
		return m_bColored;
	}
	
	/**
	 * Método setter do nome do arquivo representante da imagem. 
	 * 
	 * @param sName Nome do arquivo representante. 
	 */	
	protected void setCurrentFileName(String sName)
	{
		m_sCurrentFile = sName;
	}
	
	/**
	 * Método getter do nome arquivo representante da imagem.
	 * 
	 * @return Nome do arquivo. 
	 */	
	public String getCurrentFileName()
	{
		return m_sCurrentFile;
	}
	
	/**
	 * Método getter que obtém um pixel da imagem dadas suas coordenadas X e Y. O pixel da imagem
	 * obtido será retornado na instância concreta da classe CColorPixel ou CGrayScalePixel, mas 
	 * "formatado" como uma classe CPixel, segundo o polimorfismo da Orientação a Objetos e de modo
	 * a tornar esse método suficientemente abstrato.
	 * 
	 * @param X Valor da coordenada X do pixel.
	 * @param Y Valor da coordenada Y do pixel.
	 * @return Objeto CPixel com o pixel obtido da coordenada X e Y, ou nulo (null) se
	 * não existe um pixel nas coordenadas dadas. Por exemplo, os valores informados podem
	 * extrapolar o tamanho atual de um CImage.
	 */
	public CPixel getPixel(int X, int Y)
	{
		CPixel pPixel = null;
		try
		{
			if(m_bColored)
			{
				Color pColor = new Color(m_pBuffer.getRGB(X, Y));
				pPixel = new CColorPixel(pColor.getRed(), pColor.getGreen(), pColor.getBlue());
			}
			else
			{
				Color pColor = new Color(m_pBuffer.getRGB(X, Y));
				CColorPixel pColorPixel = new CColorPixel(pColor.getRed(), pColor.getGreen(), pColor.getBlue());
				pPixel = pColorPixel.toGrayScale();
			}
		}
		catch(Exception e)
		{
			return null;
		}
		
		return pPixel;
	}

	/**
	 * Método setter para a atualização de um pixel da imagem dadas suas coordenadas e um
	 * objeto CPixel para atualização. Se as coordenadas do pixel extrapolarem os limites da
	 * imagem, o método simplesmente nada executa.
	 * 
	 * O valor do objeto em CPixel deve ser uma instância concreta de uma de suas classes filhas,
	 * e de acordo com a indicação de colorida ou não da classe. Por exemplo, se for efetuada uma
	 * tentativa de atualizar uma imagem colorida com uma instância de um CGrayScalePixel, uma
	 * exceção será gerada. Para efetuar conversões, verifique os métodos disponíveis nas classes
	 * CColorPixel e CGrayScalePixel.
	 * 
	 * @param X Valor da coordenada X do pixel.
	 * @param Y Valor da coordenada Y do pixel.
	 * @param pPixel Objeto CPixel para ser atualizado nas coordenadas dadas, contendo uma instância
	 * contreta de uma de suas classes filhas.
	 */
	public void setPixel(int X, int Y, CPixel pPixel)
	{
		if(X < 0 || X >= this.m_iWidth)
			return;
		if(Y < 0 || Y >= this.m_iHeight)
			return;
		
		WritableRaster pRaster = m_pBuffer.getRaster();
		
		if(m_bColored)
		{
			int aRGB[] = new int[3];
			aRGB[0] = ((CColorPixel) pPixel).getRed();
			aRGB[1] = ((CColorPixel) pPixel).getGreen();
			aRGB[2] = ((CColorPixel) pPixel).getBlue();
			pRaster.setPixel(X, Y, aRGB);
		}
		else
		{
			int iBrightness = ((CGrayScalePixel) pPixel).getBrightness();
			int aRGB[] = new int[3];
			aRGB[0] = iBrightness;
			aRGB[1] = iBrightness;
			aRGB[2] = iBrightness;
			pRaster.setPixel(X, Y, aRGB);
		}
	}

	/**
	 * Utiliza as classes CFormatFactory e CFormat para salvar a imagem
	 * em um arquivo segundo o formato indicado.
	 * 
	 * @param sFile Nome do arquivo (incluindo diretório completo) para gravação da imagem.
	 * @param eFormat Formato do arquivo a ser gravado, conforme definido em CFormatFactory.CFormatEnum.
	 * @return Código de erro indicando o resultado da operação, conforme definido em CErrors.
	 * 
	 * @see CFormat#save(CPixelArray, String)
	 * @see CFormatFactory#CFormatEnum
	 * @see errors.CErrors
	 */
	public int save(String sFile, CFormatFactory.CFormatEnum eFormat) {
		CFormatFactory pFactory = CFormatFactory.getInstance();
		CFormat pFormat = pFactory.getFormat(eFormat);
		
		if(pFormat == null)
			return CErrors.ERROR_INVALID_TYPE;
		
		setCurrentFileName(sFile) ;	
		return pFormat.save(PlanarImage.wrapRenderedImage(m_pBuffer), sFile);
	}

	/**
	 * Utiliza as classes CFormatFactory e CFormat para carregar um arquivo
	 * de imagem existente segundo o formato indicado.
	 * 
	 * @param sFile Nome do arquivo (incluindo diretório completo) para carregamento da imagem.
	 * @param eFormat Formato do arquivo a ser carregado, conforme definido em CFormatFactory.CFormatEnum.
	 * @return Código de erro indicando o resultado da operação, conforme definido em CErrors.
	 * 
	 * @see CFormat#load(String, CPixelArray)
	 * @see CFormatFactory#CFormatEnum
	 * @see errors.CErrors
	 */
	public int load(String sFile, CFormatFactory.CFormatEnum eFormat)
	{
		CFormatFactory pFactory = CFormatFactory.getInstance();
		CFormat pFormat = pFactory.getFormat(eFormat);
		
		if(pFormat == null)
			return CErrors.ERROR_INVALID_TYPE;
		
		PlanarImage pImage = pFormat.load(sFile);
		if(pImage != null)
		{
			setCurrentFileName(sFile);
			setPlanarImage(pImage);
			return CErrors.SUCCESS;
		}
		else
			return CErrors.ERROR_READING_FILE;
	}
	
	/**
	 * Método setter que altera o objeto CImage do Narciso a partir de um objeto PlanarImage do JAI.
	 * 
	 * @param pImagePlan Objeto PlanarImage (JAI) para configuração do CImage.
	 */
	public void setPlanarImage(PlanarImage pImagePlan)
	{
		m_iWidth = pImagePlan.getWidth();		
		m_iHeight = pImagePlan.getHeight();
		
		m_pBuffer = pImagePlan.getAsBufferedImage();
		ColorModel pModel = m_pBuffer.getColorModel();
		int iColorType = pModel.getColorSpace().getType();
		switch(iColorType)
		{
			case BufferedImage.TYPE_BYTE_BINARY:
			case BufferedImage.TYPE_BYTE_GRAY:
			case BufferedImage.TYPE_BYTE_INDEXED:
			case BufferedImage.TYPE_USHORT_GRAY:
				m_bColored = false;
				break;
			default:
				m_bColored = true;
				break;
		}
	}
	
	/**
	 * Método getter que obtem um objeto PlanarImage do JAI a partir do objeto CImage do Narciso.
	 * 
	 * @return Objeto PlanarImage do JAI obtido a partir do objeto CImage do Narciso
	 */
	public PlanarImage getPlanarImage()
	{
		return PlanarImage.wrapRenderedImage(m_pBuffer);
	}
	
	/**
	 * Método definido para adicionar objetos segmentados a partir da imagem. Ainda não está implementado
	 * na versão atual do sistema.
	 * 
	 * @return Sempre null.
	 */
	public CImageObject addObject()
	{
		return null;
	}
	 
	/**
	 * Método definido para remover objetos segmentados a partir da imagem. Ainda não está implementado
	 * na versão atual do sistema.
	 */
	public void removeObject(CImageObject pObject)
	{
	}
	 
	/**
	 * Método definido para remover todos os objetos segmentados a partir da imagem. Ainda não está implementado
	 * na versão atual do sistema.
	 * 
	 * @return Sempre 0.
	 */
	public int clearObjects()
	{
		return 0;
	}
	 
	/**
	 * Método definido para obter o total de objetos segmentados a partir da imagem. Ainda não está implementado
	 * na versão atual do sistema.
	 * 
	 * @return Sempre 0.
	 */
	public int getObjectCount()
	{
		return 0;
	}
	 
	/**
	 * Método definido para obter um objeto segmentado a partir da imagem, baseado em uma coordenada X e Y. Ainda não está implementado
	 * na versão atual do sistema.
	 * 
	 * @return Sempre null.
	 */
	public CImageObject getObjectByCoord(int X, int Y)
	{
		return null;
	}
	 
	/**
	 * Método definido para obter um objeto segmentado a partir da imagem, baseado em um índice numérico. Ainda não está implementado
	 * na versão atual do sistema.
	 * 
	 * @return Sempre null.
	 */
	public CImageObject getObjectByIndex(int iIndex)
	{
		return null;
	}
}