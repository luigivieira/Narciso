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
 * CColorPixel é a classe para representação de um pixel colorido em uma imagem.
 * Ela provê os métodos para acesso às cores individuais no padrão RGB, e é utilizada
 * pelas classes CImage e CImageObject.
 * 
 * É herdada da classe CPixel.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 * 
 * @see CPixel
 * @see CImage
 * @see CImageObject
 *
 */

public class CColorPixel extends CPixel
{
	/** Membro privado utilizado para armazenar o valor da cor vermelha no formato RGB */
	private int m_iRed;
	
	/** Membro privado utilizado para armazenar o valor da cor verde no formato RGB */
	private int m_iGreen;
	
	/** Membro privado utilizado para armazenar o valor da cor azul no formato RGB */
	private int m_iBlue;
	
	/** 
	 * Construtor principal da classe CColorPixel. Recebe como parâmetros os valores para as cores formadoras do pixel,
	 * no formato RGB.
	 * 
	 * @param iRed Valor para a cor vermelha, que deve ser informado no intervalo de 0 a 255.
	 * @param iGreen Valor para a cor verde, que deve ser informado no intervalo de 0 a 255.
	 * @param iBlue Valor para a cor azul, que deve ser informado no intervalor de 0 a 255.
	 */
	public CColorPixel(final int iRed, final int iGreen, final int iBlue)
	{
		setRGB(iRed, iGreen, iBlue);
	}
	
	/** 
	 * Construtor alternativo da classe CColorPixel. Recebe como parâmetros os valores para as cores formadoras do pixel,
	 * no formato HLS.
	 * 
	 * @param dHue Valor para a matiz, que deve ser informado no intervalo de 0.0 a (2.0 * Math.PI).
	 * @param dLightness Valor para a luminosidade, que deve ser informado no intervalo de 0.0 a 1.0.
	 * @param dSaturation Valor para a saturação, que deve ser informado no intervalor de 0.0 a 1.0.
	 */
	public CColorPixel(final double dHue, final double dLightness, final double dSaturation)
	{
		setHLS(dHue, dLightness, dSaturation);
	}
	
	/**
	 * Método setter da classe que permite alterar simultâneamente os valores dos três componentes do formato
	 * RGB: vermelho, verde e azul. 
	 * 
	 * @param iRed Valor para a cor vermelha, que deve ser informado no intervalo de 0 a 255.
	 * @param iGreen Valor para a cor verde, que deve ser informado no intervalo de 0 a 255.
	 * @param iBlue Valor para a cor azul, que deve ser informado no intervalo de 0 a 255.
	 */
	public void setRGB(final int iRed, final int iGreen, final int iBlue)
	{
		if(iRed > 255)
			m_iRed = iRed % 255;
		else
			m_iRed = iRed;

		if(iGreen > 255)
			m_iGreen = iGreen % 255;
		else
			m_iGreen = iGreen;

		if(iBlue > 255)
			m_iBlue = iBlue % 255;
		else
			m_iBlue = iBlue;
		
		updateHLSFromRGB();
	}
	
	/**
	 * Método setter da classe que permite alterar simultâneamente os valores dos três componentes do formato
	 * HLS: matiz, luminosidade e saturação.
	 * 
	 * @param dHue Valor para a matiz, que deve ser informado no intervalo de 0.0 a (2.0 * Math.PI).
	 * @param dLightness Valor para a luminosidade, que deve ser informado no intervalo de 0.0 a 1.0.
	 * @param dSaturation Valor para a saturação, que deve ser informado no intervalor de 0.0 a 1.0.
	 */
	public void setHLS(final double dHue, final double dLightness, final double dSaturation)
	{
		super.setHLS(dHue, dLightness, dSaturation);
		updateRGBFromHLS();
	}
	
	/**
	 * Método setter da cor vermelha para o pixel. Atualiza o valor do membro m_siRed com o valor dado. Se o valor 
	 * informado for maior do que 255, a cor vermelha é atualizada com o resto da divisão do valor informado por 255.
	 * Atualiza os valores no formato HLS.
	 * 
	 * @param iValue Novo valor (no intervalo de 0 a 255) para a cor vermelha, segundo o padrão RGB. 
	 */
	public void setRed(final int iValue)
	{
		if(iValue > 255)
			m_iRed = iValue % 255;
		else
			m_iRed = iValue;
		
		updateHLSFromRGB();
	}
	 
	/**
	 * Método getter da cor vermelha para o pixel. Retorna o valor atual do membro m_siRed, indicando o valor corrente
	 * de vermelho para o pixel.
	 * @return Valor corrente do vermelho para o pixel.
	 */
	public int getRed()
	{
		return m_iRed;
	}

	/**
	 * Método setter da cor verde para o pixel. Atualiza o valor do membro m_siGreen com o valor dado. Se o valor 
	 * informado for maior do que 255, a cor verde é atualizada com o resto da divisão do valor informado por 255.
	 * Atualiza os valores no formato HLS.
	 * 
	 * @param iValue Novo valor (no intervalo de 0 a 255) para a cor verde, segundo o padrão RGB. 
	 */	
	public void setGreen(final int iValue)
	{
		if(iValue > 255)
			m_iGreen = iValue % 255;
		else
			m_iGreen = iValue;
		
		updateHLSFromRGB();
	}
	 
	/**
	 * Método getter da cor verde para o pixel. Retorna o valor atual do membro m_siGreen, indicando o valor corrente
	 * de verde para o pixel.
	 * @return Valor corrente do verde para o pixel.
	 */
	public int getGreen()
	{
		return m_iGreen;
	}
	 
	/**
	 * Método setter da cor azul para o pixel. Atualiza o valor do membro m_siBlue com o valor dado. Se o valor 
	 * informado for maior do que 255, a cor azul é atualizada com o resto da divisão do valor informado por 255.
	 * Atualiza os valores no formato HLS.
	 * 
	 * @param iValue Novo valor (no intervalo de 0 a 255) para a cor azul, segundo o padrão RGB. 
	 */
	public void setBlue(int iValue)
	{
		if(iValue > 255)
			m_iBlue = iValue % 255;
		else
			m_iBlue = iValue;
		
		updateHLSFromRGB();
	}
	 
	/**
	 * Método getter da cor azul para o pixel. Retorna o valor atual do membro m_siBlue, indicando o valor corrente
	 * de azul para o pixel.
	 * @return Valor corrente do azul para o pixel.
	 */
	public int getBlue()
	{
		return m_iBlue;
	}
	
	/**
	 * Método setter sobrecarregado da matiz para o pixel, simplesmente executando o método da classe pai CPixel.
	 * Atualiza os valores no formato RGB.
	 * 
	 * @param dHue Novo valor para a matiz, no intervalo de 0.0 a (2.0 * Math.PI).
	 */
	public void setHue(double dHue)
	{
		super.setHue(dHue);
		updateRGBFromHLS();
	}
	
	/**
	 * Método setter sobrecarregado da luminosidade para o pixel, simplesmente executando o método da classe pai CPixel.
	 * Atualiza os valores no formato RGB.
	 * 
	 * @param dLightness Novo valor para a luminosidade, no intervalo de 0.0 a 1.0.
	 */
	public void setLightness(double dLightness)
	{
		super.setLightness(dLightness);
		updateRGBFromHLS();
	}
	
	/**
	 * Método setter sobrecarregado da saturação para o pixel, simplesmente executando o método da classe pai CPixel.
	 * Atualiza os valores no formato RGB.
	 * 
	 * @param dSaturation Novo valor para a saturação, no intervalo de 0.0 a 1.0.
	 */
	public void setSaturation(double dSaturation)
	{
		super.setSaturation(dSaturation);
		updateRGBFromHLS();
	}

	/**
	 * Gera uma instância da classe CGrayScalePixel contendo o pixel atual convertido para escala de cinza.
	 * A conversão é baseada diretamente no valor de luminosidade do pixel atual.
	 * 
	 * @return Objeto da classe CGrayScalePixel.
	 */
	public CGrayScalePixel toGrayScale()
	{
		return new CGrayScalePixel((int) (m_dLightness * 255.0));
	}

	/**
	 * Método privado de utilização interna da classe. É utilizado para atualizar os valores  do formato HLS quando
	 * o valor de qualquer componente do formato RGB é atualizado.
	 */
	private void updateHLSFromRGB()
	{
		double dR = m_iRed / 255.0;
		double dG = m_iGreen / 255.0;
		double dB = m_iBlue / 255.0;

		double dMin = Math.min(Math.min(dR, dG), dB);
		double dMax = Math.max(Math.max(dR, dG), dB);
		double dDelta = dMax - dMin;

		m_dLightness = (dMax + dMin) / 2.0;

		if(m_dLightness <= 0)
			m_dSaturation = 0.0;
		else if(m_dLightness <= 0.5)
			m_dSaturation = dDelta / (m_dLightness * 2.0);
		else
			m_dSaturation = dDelta / (2.0 - (m_dLightness * 2.0));
				
		if(dDelta == 0)
			m_dHue = 0.0;
		else if(dMax == dR)
		{
			if(dG >= dB)
				m_dHue = (Math.PI / 3.0) * ((dG - dB) / dDelta); 
			else
				m_dHue = (Math.PI / 3.0) * ((dG - dB) / dDelta) + (2.0 * Math.PI);
		}
		else if(dMax == dG)
			m_dHue = (Math.PI / 3.0) * ((dB - dR) / dDelta) + ((2.0 / 3.0) * Math.PI);
		else // dMax == dB
			m_dHue = (Math.PI / 3.0) * ((dR - dG) / dDelta) + ((4.0 / 3.0) * Math.PI);
	}
	
	/**
	 * Método privado de utilização interna da classe. É utilizado para atualizar os valores  do formato RGB quando
	 * o valor de qualquer componente do formato HLS é atualizado.
	 */
	private void updateRGBFromHLS()
	{
		double dTemp2;
		
		if(m_dLightness < 0.5)
			dTemp2 = m_dLightness * (1.0 + m_dSaturation);
		else
			dTemp2 = m_dLightness + m_dSaturation - (m_dLightness * m_dSaturation);
		
		double dTemp1 = 2.0 * m_dLightness - dTemp2;
		
		double dHue = m_dHue / (2.0 * Math.PI);
		
		double aTemp3[] = new double[3];
		double aRGB[] = new double[3];
		
		aTemp3[0] = dHue + (1.0 / 3.0);
		aTemp3[1] = dHue;
		aTemp3[2] = dHue - (1.0 / 3.0);
		
		for(int i = 0; i < 3; i++)
		{
			if(aTemp3[i] < 0)
				aTemp3[i] += 1.0;
			else if(aTemp3[i] > 1)
				aTemp3[i] -= 1.0;
			
			if(aTemp3[i] < (1.0 / 6.0))
				aRGB[i] = dTemp1 + ((dTemp2 - dTemp1) * 6.0 * aTemp3[i]);
			else if(aTemp3[i] < (1.0 / 2.0))
				aRGB[i] = dTemp2;
			else if(aTemp3[i] < (2.0 / 3.0))
				aRGB[i] = dTemp1 + ((dTemp2 - dTemp1) * ((2.0 / 3.0) - aTemp3[i]) * 6.0);
			else
				aRGB[i] = aTemp3[i];
		}
		
		m_iRed = (int) (aRGB[0] * 255.0);
		m_iGreen = (int) (aRGB[1] * 255.0);
		m_iBlue = (int) (aRGB[2] * 255.0);
	}	
}