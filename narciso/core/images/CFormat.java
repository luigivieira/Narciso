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

import javax.media.jai.PlanarImage;

/**
 * CFormat é a classe básica abstrata para a manipulação de arquivos de imagens (carregamento e gravação) em
 * diversos formatos. Serve como classe pai para as classes dos formatos específicos, mas mantem a
 * manipulação do nome do último arquivo carregado/gravado para facilitar as operações em uma
 * interface gráfica, por exemplo. 
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
	 * Método abstrato com o propósito de implementar a gravação de uma imagem ou objeto de imagem em um formato específico.
	 * É definido na classe CFormat como abstract, e deve ser implementado por cada uma das classes herdadas.
	 * 
	 * @param pImage Objeto da classe PlanarImage com a imagem a ser gravada.
	 * @param sFile Nome do arquivo para gravação.
	 * @return O retorno deve ser zero (0) para indicar sucesso, ou outro valor maior que zero para indicar um código de erro.
	 */
	public abstract int save(PlanarImage pImage, String sFile);
	
	/**
	 * Método abstrato com o propósito de implementar o carregamento de uma imagem de um formato específico.
	 * É definido na classe CFormat como abstract, e deve ser implementado por cada uma das classes herdadas.
	 * 
	 * @param sFile Nome do arquivo para carregamento.
	 * @return Objeto PlanarImage com a imagem lida, ou nulo (null) se a imagem não pode ser carregada.
	 */
	public abstract PlanarImage load(String sFile);
}