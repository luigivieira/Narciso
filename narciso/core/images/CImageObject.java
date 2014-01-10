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
 * A classe CImageObject ser� utilizada para a segmenta��o de imagens em objetos de interesse para a Vis�o Computacional.
 * A vers�o atual do sistema implementa alguns m�todos de segmenta��o (Thresholding, Prewitt, etc), mas ainda n�o implementa
 * m�todos de rotula��o, e por isso essa classe ainda n�o foi totalmente desenvolvida.
 * 
 * A classe consta no c�digo do sistema pois a modelagem foi realizada contenplando o requisito de extra��o de objetos a partir
 * de imagens. Assim, as pr�ximas vers�es j� contam com um direcionamento formal para a constru��o dos objetos de imagens.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 * 
 * @see CPixel
 * @see CImage
 *
 */

public class CImageObject
{
	/** Membro privado utilizado para armazenar a imagem geradora do objeto */
	private CImage m_pParent;
	 
	/**
	 * Construtor da classe.
	 * 
	 * @param pParent Imagem CImage gerador do objeto CImageObject. 
	 */
	public CImageObject(CImage pParent)
	{
		m_pParent = pParent;
	}
	
	/**
	 * M�todo getter utilizado para obter a imagem CImage geradora do objeto CImageObject.
	 * 
	 * @return Imagem CImage.
	 */
	public CImage getParent()
	{
		return m_pParent;
	}
}