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
 
package core.exporting;

import core.images.*;

/**
 * Essa interface define os m�todos a serem implementados por todas as classes de exporta��o de propriedades implementadas  
 * pelo sistema Narciso.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

public interface IExporter
{
	/**
	 * Esse m�todo deve ser sempre publicado nas classes que implementarem essa interface, para prover
	 * a exporta��o das propriedades.
	 *  
	 * @param pImage Objeto CImage com a imagem de onde as propriedades ser�o extra�das.
	 * @param sFile Nome do arquivo a ser gerado.
	 * 
	 * @return Deve retornar CErrors.SUCCESS se a exporta��o foi realizada com sucesso, ou um c�digo com o erro ocorrido.
	 */
	public abstract int exportInfo(CImage pImage, String sFile);
}
 
