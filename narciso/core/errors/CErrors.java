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
 
package core.errors;

/**
 * CErrors � a classe abstrata utilizada para definir os tipos de erros retornados pelos m�todos
 * das classes componentes do sistema Narciso. Todos os m�todos de classes que n�o retornarem um 
 * objeto ou um valor l�gico (boolean), retornar�o um inteiro representando um dos valores definidos nessa classe.
 * O valor zero (0) representa o sucesso, e valores maiores do que zero representam um c�digo de erro.
 * 
 * A classe define uma s�rie de membros est�ticos e finais para representar cada um dos valores de erro,
 * incluindo o sucesso, de modo que n�o � necess�rio se trabalhar diretamente com os valores. A classe
 * prov� tamb�m um m�todo chamado getErrorDescription que retorna uma string com a mensagem de erro
 * (apenas em Portugu�s) para um c�digo de erro informado. O objetivo � facilitar o desenvolvimento
 * de interfaces gr�ficas dependentes dos c�digos de erro do Narciso.
 *   
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 * 
 */

public abstract class CErrors {

	/** Membro p�blico e est�tico representando o valor de indica��o de sucesso. */
	public static final int SUCCESS = 0;
	
	/** Membro p�blico e est�tico representando o c�digo de erro que indica que um tipo inv�lido foi informado a um m�todo. */
	public static final int ERROR_INVALID_TYPE = 1;

	/** Membro p�blico e est�tico representando o c�digo de erro que indica erro na leitura de um arquivo. */
	public static final int ERROR_READING_FILE = 2;

	/** Membro p�blico e est�tico representando o c�digo de erro que indica erro de parser em arquivo XML. */
	public static final int ERROR_PARSING_XML_FILE = 3;

	/** Membro p�blico e est�tico representando o c�digo de erro que indica erro de parser em arquivo XML. */
	public static final int ERROR_CREATING_XML_DOCUMENT = 4;
	
	/** Membro p�blico e est�tico representando o c�digo de erro que indica um valor inv�lido de configura��o para o sistema Narciso. */
	public static final int ERROR_INVALID_CONFIG_VALUE = 5;

	/** Membro p�blico e est�tico representando o c�digo de erro que indica um nome de classe inv�lido para uma opera��o do Narciso. */
	public static final int ERROR_INVALID_OPERATION_CLASS_NAME = 6;

	/** Membro p�blico e est�tico representando o c�digo de erro que indica que um par�metro requerido n�o foi informado na execu��o de uma opera��o. */
	public static final int ERROR_MISSING_PARAMETER = 7;
	
	/** Membro p�blico e est�tico representando o c�digo de erro que indica que um par�metro informado para execu��o de uma opera��o cont�m dados inv�lidos. */
	public static final int ERROR_INVALID_PARAMETER = 8;

	/** Membro p�blico e est�tico representando o c�digo de erro que indica que o n�mero de fontes (imagens, objetos, propriedades) informadas para uma opera��o � diferente do requerido. */
	public static final int ERROR_WRONG_NUMBER_OF_SOURCES = 9;

	/** Membro p�blico e est�tico representando o c�digo de erro que indica que o tipo da fonte (imagens, objetos, propriedades) informada para uma opera��o � diferente do requerido. */
	public static final int ERROR_WRONG_SOURCE_TYPE = 10;

	/** Membro p�blico e est�tico representando o c�digo de erro que indica que um erro interno (inesperado) aconteceu. */
	public static final int ERROR_UNEXPECTED_ERROR = 11;

	/** Membro p�blico e est�tico representando o c�digo de erro que indica que um tipo de propriedade inv�lido foi utilizado. */
	public static final int ERROR_INVALID_PROPERTY_TYPE = 12;
	
	/**
	 * Retorna uma mensagem de texto (apenas no idioma Portugu�s) para o c�digo de erro dado.
	 * @param iError C�digo de erro para obten��o da mensagem descritiva.
	 * @return Mensagem descritiva em portugu�s para o erro informado.
	 */
	public static String getErrorDescription(int iError)
	{
		switch(iError)
		{
			case SUCCESS:
				return "sucesso";
			case ERROR_INVALID_TYPE:
				return "um tipo inv�lido foi informado ao m�todo chamado";
			case ERROR_READING_FILE:
				return "erro na leitura/grava��o do arquivo";
			case ERROR_PARSING_XML_FILE:
				return "o arquivo XML n�o pode ser gravado/lido devido a um erro estrutural (parser)";
			case ERROR_CREATING_XML_DOCUMENT:
				return "n�o foi poss�vel criar um documento xml para leitura/grava��o do arquivo";
			case ERROR_INVALID_CONFIG_VALUE:
				return "um valor inv�lido para um item de configura��o do sistema Narciso foi identificado";
			case ERROR_INVALID_OPERATION_CLASS_NAME:
				return "um nome inv�lido de classe de opera��o foi informado";
			case ERROR_MISSING_PARAMETER:
				return "um par�metro requerido n�o foi informado na execu��o da opera��o";
			case ERROR_INVALID_PARAMETER:
				return "o par�metro informado para a execu��o da opera��o cont�m dados inv�lidos";
			case ERROR_WRONG_NUMBER_OF_SOURCES:
				return "o n�mero de fontes (imagem, objeto ou propriedade) informadas para a opera��o � diferente do requerido";
			case ERROR_WRONG_SOURCE_TYPE:
				return "o tipo da fonte (imagem, objeto ou propriedade) informada para a opera��o � diferente do requerido";
			case ERROR_UNEXPECTED_ERROR:
				return "um erro interno ao sistema (inesperado) aconteceu (por favor contacte os desenvolvedores)";
			case ERROR_INVALID_PROPERTY_TYPE:
				return "um tipo de propriedade inv�lido foi utilizado";
			default:
				return "erro desconhecido";
		}
	}
}