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
 * CGrayScalePixel é a classe para representação de um pixel em uma imagem em tons de cinza.
 * Ela provê os métodos para acesso aos valores de brilho em uma escala de tons de cinza, e é utilizada
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

public class CGrayScalePixel extends CPixel
{
 
	/** Membro privado utilizado para armazenar o valor de brilho do pixel em uma escala de 0 a 255 (tons de cinza) */
	private int m_iBrightness;

	/**
	 * Construtor principal da classe CGrayScalePixel. Recebe como parâmetro um valor de brilho para o pixel.
	 * @param siBrightness Valor de brilho para o pixel, que deve ser informado no intervalo de 0 a 255.
	 */
	public CGrayScalePixel(final int iBrightness)
	{
		setBrightness(iBrightness);
	}
	
	/**
	 * Método setter da brilho para o pixel. Atualiza o valor do membro m_siBrightness com o valor dado. Se o valor 
	 * informado for maior do que 255, o brilho é atualizado com o resto da divisão do valor informado por 255.
	 * Atualiza os valores no formato HLS.
	 * @param iValue Novo valor (no intervalo de 0 a 255) para o brilho do pixel. 
	 */	
	public void setBrightness(int iValue)
	{
		if(iValue > 255)
			m_iBrightness = iValue % 255;
		else
			m_iBrightness = iValue;
		updateHLSFromBrightness();
	}

	/**
	 * Método getter do brilho para o pixel. Retorna o valor atual do membro m_siBrightness, indicando o valor corrente
	 * de brilho para o pixel.
	 * @return Valor corrente do brilho para o pixel.
	 */	
	public int getBrightness()
	{
		return m_iBrightness;
	}
	
	/**
	 * Método setter da luminosidade para o pixel. Atualiza o valor do brilho.
	 * @param dLightness Novo valor para a luminosidade, no intervalo de 0.0 a 1.0. 
	 */	
	public void setLightness(double dLightness)
	{
		super.setLightness(dLightness);
		updateBrightnessFromHLS();
	}

	/**
	 * Atualiza os valores no formato HLS quando o valor do brilho é alterado.
	 *
	 */
	private void updateHLSFromBrightness()
	{
		m_dLightness = m_iBrightness / 255.0;
		m_dHue = 0.0;
		m_dSaturation = 0.0;
	}

	/**
	 * Atualiza o valor do brilho quando os valores do formato HLS são alterados.
	 *
	 */
	private void updateBrightnessFromHLS()
	{
		m_iBrightness = (int) (m_dLightness * 255.0);
	}	
}