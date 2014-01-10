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
 
package core.images;

/**
 * CPixel � a classe b�sica abstrata para a representa��o de um pixel no sistema Narciso.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 * 
 * @see CColorPixel
 * @see CGrayScalePixel
 * @see CImage
 * @see CImageObject
 *
 */

public abstract class CPixel
{
	/** Membro privado utilizado para armazenar o valor da matiz, segundo o formato HLS */
	protected double m_dHue;
	
	/** Membro privado utilizado para armazenar o valor da luminosidade, segundo o formato HLS */
	protected double m_dLightness;
	
	/** Membro privado utilizado para armazenar o valor da satura��o, segundo o formato HLS */
	protected double m_dSaturation;
	
	/**
	 * M�todo setter para configura��o simult�nea dos valores do formato HLS.
	 * 
	 * @param dHue Valor da matiz, no intervalo de 0.0 a (2.0 * Math.PI).
	 * @param dLightness Valor da luminosidade, no intervalo de 0.0 a 1.0.
	 * @param dSaturation Valor da satura��o, no intervalo de 0.0 a 1.0.
	 */
	protected void setHLS(final double dHue, final double dLightness, final double dSaturation)
	{
		m_dHue = dHue;
		m_dLightness = dLightness;
		m_dSaturation = dSaturation;
	}
	
	/**
	 * M�todo setter para a matiz do pixel.
	 * 
	 * @param dHue Valor da matiz, no intervalo de 0.0 a (2.0 * Math.PI).
	 */
	protected void setHue(double dHue)
	{
		if(dHue < 0.0)
			m_dHue = 0.0;
		else if(dHue > (2.0 * Math.PI))
			m_dHue = 2.0 * Math.PI;
		else
			m_dHue = dHue;
	}
	
	/**
	 * M�todo getter para obten��o do valor da matiz do pixel.
	 * 
	 * @return Valor atual da matiz do pixel, no intervalo de 0.0 a (2.0 * Math.PI).
	 */
	public double getHue()
	{
		return m_dHue;
	}

	/**
	 * M�todo setter para a luminosidade do pixel.
	 * 
	 * @param dHue Valor da luminosidade, no intervalo de 0.0 a 1.0.
	 */
	protected void setLightness(double dLightness)
	{
		if(dLightness < 0.0)
			m_dLightness = 0.0;
		else if(dLightness > 1.0)
			m_dLightness = 1.0;
		else
			m_dLightness = dLightness;
	}
	
	/**
	 * M�todo getter para obten��o do valor da luminosidade do pixel.
	 * 
	 * @return Valor atual da luminosidade do pixel, no intervalo de 0.0 a 1.0.
	 */
	public double getLightness()
	{
		return m_dLightness;
	}

	/**
	 * M�todo setter para a satura��o do pixel.
	 * 
	 * @param dHue Valor da satura��o, no intervalo de 0.0 a 1.0.
	 */
	protected void setSaturation(double dSaturation)
	{
		if(dSaturation < 0.0)
			m_dSaturation = 0.0;
		else if(dSaturation > 1.0)
			m_dSaturation = 1.0;
		else
			m_dSaturation = dSaturation;
	}
	
	/**
	 * M�todo getter para obten��o do valor da satura��o do pixel.
	 * 
	 * @return Valor atual da satura��o do pixel, no intervalo de 0.0 a 1.0.
	 */
	public double getSaturation()
	{
		return m_dSaturation;
	}
}