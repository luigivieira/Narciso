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

import javax.media.jai.PlanarImage;

/**
 * CFormat � a classe b�sica abstrata para a manipula��o de arquivos de imagens (carregamento e grava��o) em
 * diversos formatos. Serve como classe pai para as classes dos formatos espec�ficos, mas mantem a
 * manipula��o do nome do �ltimo arquivo carregado/gravado para facilitar as opera��es em uma
 * interface gr�fica, por exemplo. 
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 * 
 * @see CBMPFormat
 * @see CJPGFormat
 * @see CGIFFormat
 * @see CTIFFFormat
 * @see CPixelArray
 *
 */

public abstract class CFormat
{
	/**
	 * M�todo abstrato com o prop�sito de implementar a grava��o de uma imagem ou objeto de imagem em um formato espec�fico.
	 * � definido na classe CFormat como abstract, e deve ser implementado por cada uma das classes herdadas.
	 * 
	 * @param pImage Objeto da classe PlanarImage com a imagem a ser gravada.
	 * @param sFile Nome do arquivo para grava��o.
	 * @return O retorno deve ser zero (0) para indicar sucesso, ou outro valor maior que zero para indicar um c�digo de erro.
	 */
	public abstract int save(PlanarImage pImage, String sFile);
	
	/**
	 * M�todo abstrato com o prop�sito de implementar o carregamento de uma imagem de um formato espec�fico.
	 * � definido na classe CFormat como abstract, e deve ser implementado por cada uma das classes herdadas.
	 * 
	 * @param sFile Nome do arquivo para carregamento.
	 * @return Objeto PlanarImage com a imagem lida, ou nulo (null) se a imagem n�o pode ser carregada.
	 */
	public abstract PlanarImage load(String sFile);
}