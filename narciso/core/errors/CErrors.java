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
 
package core.errors;

/**
 * CErrors é a classe abstrata utilizada para definir os tipos de erros retornados pelos métodos
 * das classes componentes do sistema Narciso. Todos os métodos de classes que não retornarem um 
 * objeto ou um valor lógico (boolean), retornarão um inteiro representando um dos valores definidos nessa classe.
 * O valor zero (0) representa o sucesso, e valores maiores do que zero representam um código de erro.
 * 
 * A classe define uma série de membros estáticos e finais para representar cada um dos valores de erro,
 * incluindo o sucesso, de modo que não é necessário se trabalhar diretamente com os valores. A classe
 * provê também um método chamado getErrorDescription que retorna uma string com a mensagem de erro
 * (apenas em Português) para um código de erro informado. O objetivo é facilitar o desenvolvimento
 * de interfaces gráficas dependentes dos códigos de erro do Narciso.
 *   
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 * 
 */

public abstract class CErrors {

	/** Membro público e estático representando o valor de indicação de sucesso. */
	public static final int SUCCESS = 0;
	
	/** Membro público e estático representando o código de erro que indica que um tipo inválido foi informado a um método. */
	public static final int ERROR_INVALID_TYPE = 1;

	/** Membro público e estático representando o código de erro que indica erro na leitura de um arquivo. */
	public static final int ERROR_READING_FILE = 2;

	/** Membro público e estático representando o código de erro que indica erro de parser em arquivo XML. */
	public static final int ERROR_PARSING_XML_FILE = 3;

	/** Membro público e estático representando o código de erro que indica erro de parser em arquivo XML. */
	public static final int ERROR_CREATING_XML_DOCUMENT = 4;
	
	/** Membro público e estático representando o código de erro que indica um valor inválido de configuração para o sistema Narciso. */
	public static final int ERROR_INVALID_CONFIG_VALUE = 5;

	/** Membro público e estático representando o código de erro que indica um nome de classe inválido para uma operação do Narciso. */
	public static final int ERROR_INVALID_OPERATION_CLASS_NAME = 6;

	/** Membro público e estático representando o código de erro que indica que um parâmetro requerido não foi informado na execução de uma operação. */
	public static final int ERROR_MISSING_PARAMETER = 7;
	
	/** Membro público e estático representando o código de erro que indica que um parâmetro informado para execução de uma operação contém dados inválidos. */
	public static final int ERROR_INVALID_PARAMETER = 8;

	/** Membro público e estático representando o código de erro que indica que o número de fontes (imagens, objetos, propriedades) informadas para uma operação é diferente do requerido. */
	public static final int ERROR_WRONG_NUMBER_OF_SOURCES = 9;

	/** Membro público e estático representando o código de erro que indica que o tipo da fonte (imagens, objetos, propriedades) informada para uma operação é diferente do requerido. */
	public static final int ERROR_WRONG_SOURCE_TYPE = 10;

	/** Membro público e estático representando o código de erro que indica que um erro interno (inesperado) aconteceu. */
	public static final int ERROR_UNEXPECTED_ERROR = 11;

	/** Membro público e estático representando o código de erro que indica que um tipo de propriedade inválido foi utilizado. */
	public static final int ERROR_INVALID_PROPERTY_TYPE = 12;
	
	/**
	 * Retorna uma mensagem de texto (apenas no idioma Português) para o código de erro dado.
	 * @param iError Código de erro para obtenção da mensagem descritiva.
	 * @return Mensagem descritiva em português para o erro informado.
	 */
	public static String getErrorDescription(int iError)
	{
		switch(iError)
		{
			case SUCCESS:
				return "sucesso";
			case ERROR_INVALID_TYPE:
				return "um tipo inválido foi informado ao método chamado";
			case ERROR_READING_FILE:
				return "erro na leitura/gravação do arquivo";
			case ERROR_PARSING_XML_FILE:
				return "o arquivo XML não pode ser gravado/lido devido a um erro estrutural (parser)";
			case ERROR_CREATING_XML_DOCUMENT:
				return "não foi possível criar um documento xml para leitura/gravação do arquivo";
			case ERROR_INVALID_CONFIG_VALUE:
				return "um valor inválido para um item de configuração do sistema Narciso foi identificado";
			case ERROR_INVALID_OPERATION_CLASS_NAME:
				return "um nome inválido de classe de operação foi informado";
			case ERROR_MISSING_PARAMETER:
				return "um parâmetro requerido não foi informado na execução da operação";
			case ERROR_INVALID_PARAMETER:
				return "o parâmetro informado para a execução da operação contém dados inválidos";
			case ERROR_WRONG_NUMBER_OF_SOURCES:
				return "o número de fontes (imagem, objeto ou propriedade) informadas para a operação é diferente do requerido";
			case ERROR_WRONG_SOURCE_TYPE:
				return "o tipo da fonte (imagem, objeto ou propriedade) informada para a operação é diferente do requerido";
			case ERROR_UNEXPECTED_ERROR:
				return "um erro interno ao sistema (inesperado) aconteceu (por favor contacte os desenvolvedores)";
			case ERROR_INVALID_PROPERTY_TYPE:
				return "um tipo de propriedade inválido foi utilizado";
			default:
				return "erro desconhecido";
		}
	}
}