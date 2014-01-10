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
 
package core.operations;

import java.util.Properties;
import java.util.Vector;

/**
 * Classe básica para a implementação de operações sobre imagens no sistema Narciso.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see CMacroOperation
 * @see COperation
 */

public abstract class COperation
{
	/** Membro privado utilizado para armazenar o nome da operação. */
	private String m_sName;
	
	/** Membro privado utilizado para armazenar a descrição da operação. */
	private String m_sDescription;
	 
	/**
	 * Construtor da classe.
	 * 
	 * @param sName Nome da operação.
	 * @param sDescription Descrição da operação.
	 */
	public COperation(String sName, String sDescription)
	{
		setName(sName);
		setDescription(sDescription);
	}
	 
	/**
	 * Método setter do nome da operação.
	 * 
	 * @param sName Novo nome para a operação.
	 */
	public void setName(String sName)
	{
		m_sName = sName;
	}
	 
	/**
	 * Método getter do nome da operação.
	 * @return Novo da operação.
	 */
	public String getName()
	{
		return m_sName;
	}

	/**
	 * Método setter da descrição da operação.
	 * 
	 * @param sDesc Nova descrição para a operação.
	 */
	public void setDescription(String sDesc)
	{
		m_sDescription = sDesc;
	}
	 
	/**
	 * Método getter da descrição da operação.
	 * @return Descrição da operação.
	 */
	public String getDescription()
	{
		return m_sDescription;
	}
	
	/**
	 * Método abstrato que deve ser implementado em todas as operações implementadas no sistema Narciso,
	 * responsável pela execução da operação.
	 * 
	 * @param pSource Vetor de objetos básicos do Java, com os objetos sobre os quais a operação será executada.
	 * Utiliza-se objetos básicos do Java pois a interface das operações deve ser suficientemente genérica para ser
	 * executada sobre qualquer classe, prevendo a implementação de operações sobre imagens, objetos, pixels, etc.
	 * A documentação de cada operação deve descrever que tipo de objeto é esperado nesse vetor. 
	 * 
	 * @param pParams Objeto Properties do Java contendo parâmetros para a execução da operação. Os parâmetros devem
	 * ser definidos pelo nome e valor, separados pelo sinal de igual (=). Por exemplo, um parâmetro pode ser definido
	 * como o texto "largura=10", e adicionado a um objeto Properties, antes desse método ser executado. A documentação
	 * de cada operação deve descrever os parâmetros esperados.
	 * Esse parâmetro também deve ser utilizado pelas operações implementadas para definir o código de erro, em caso 
	 * um ocorra. O parâmetro de nome "error" deve então ser definido em pParams, para utilização externa na rotina
	 * chamadora da operação.
	 * 
	 * @return Vetor de objetos básicos do Java, contendo os objetos resultantes da execução da operação, ou null se 
	 * um erro ocorreu. No caso de erro ocorrido, o parâmetro "error" deve ter sido disponibilizado no parâmetro pParams.
	 */
	public abstract Vector<Object> execute(Vector<Object> pSource, Properties pParams);
}
 
