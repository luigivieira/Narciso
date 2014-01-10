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
 * A classe CImageObject será utilizada para a segmentação de imagens em objetos de interesse para a Visão Computacional.
 * A versão atual do sistema implementa alguns métodos de segmentação (Thresholding, Prewitt, etc), mas ainda não implementa
 * métodos de rotulação, e por isso essa classe ainda não foi totalmente desenvolvida.
 * 
 * A classe consta no código do sistema pois a modelagem foi realizada contenplando o requisito de extração de objetos a partir
 * de imagens. Assim, as próximas versões já contam com um direcionamento formal para a construção dos objetos de imagens.
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
	 * Método getter utilizado para obter a imagem CImage geradora do objeto CImageObject.
	 * 
	 * @return Imagem CImage.
	 */
	public CImage getParent()
	{
		return m_pParent;
	}
}