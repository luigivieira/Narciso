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
 
package core.operations;

import java.util.Properties;
import java.util.Vector;

/**
 * Classe b�sica para a implementa��o de opera��es sobre imagens no sistema Narciso.
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
	/** Membro privado utilizado para armazenar o nome da opera��o. */
	private String m_sName;
	
	/** Membro privado utilizado para armazenar a descri��o da opera��o. */
	private String m_sDescription;
	 
	/**
	 * Construtor da classe.
	 * 
	 * @param sName Nome da opera��o.
	 * @param sDescription Descri��o da opera��o.
	 */
	public COperation(String sName, String sDescription)
	{
		setName(sName);
		setDescription(sDescription);
	}
	 
	/**
	 * M�todo setter do nome da opera��o.
	 * 
	 * @param sName Novo nome para a opera��o.
	 */
	public void setName(String sName)
	{
		m_sName = sName;
	}
	 
	/**
	 * M�todo getter do nome da opera��o.
	 * @return Novo da opera��o.
	 */
	public String getName()
	{
		return m_sName;
	}

	/**
	 * M�todo setter da descri��o da opera��o.
	 * 
	 * @param sDesc Nova descri��o para a opera��o.
	 */
	public void setDescription(String sDesc)
	{
		m_sDescription = sDesc;
	}
	 
	/**
	 * M�todo getter da descri��o da opera��o.
	 * @return Descri��o da opera��o.
	 */
	public String getDescription()
	{
		return m_sDescription;
	}
	
	/**
	 * M�todo abstrato que deve ser implementado em todas as opera��es implementadas no sistema Narciso,
	 * respons�vel pela execu��o da opera��o.
	 * 
	 * @param pSource Vetor de objetos b�sicos do Java, com os objetos sobre os quais a opera��o ser� executada.
	 * Utiliza-se objetos b�sicos do Java pois a interface das opera��es deve ser suficientemente gen�rica para ser
	 * executada sobre qualquer classe, prevendo a implementa��o de opera��es sobre imagens, objetos, pixels, etc.
	 * A documenta��o de cada opera��o deve descrever que tipo de objeto � esperado nesse vetor. 
	 * 
	 * @param pParams Objeto Properties do Java contendo par�metros para a execu��o da opera��o. Os par�metros devem
	 * ser definidos pelo nome e valor, separados pelo sinal de igual (=). Por exemplo, um par�metro pode ser definido
	 * como o texto "largura=10", e adicionado a um objeto Properties, antes desse m�todo ser executado. A documenta��o
	 * de cada opera��o deve descrever os par�metros esperados.
	 * Esse par�metro tamb�m deve ser utilizado pelas opera��es implementadas para definir o c�digo de erro, em caso 
	 * um ocorra. O par�metro de nome "error" deve ent�o ser definido em pParams, para utiliza��o externa na rotina
	 * chamadora da opera��o.
	 * 
	 * @return Vetor de objetos b�sicos do Java, contendo os objetos resultantes da execu��o da opera��o, ou null se 
	 * um erro ocorreu. No caso de erro ocorrido, o par�metro "error" deve ter sido disponibilizado no par�metro pParams.
	 */
	public abstract Vector<Object> execute(Vector<Object> pSource, Properties pParams);
}
 
