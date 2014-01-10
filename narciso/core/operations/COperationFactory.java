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

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import core.errors.*;

/**
 * Classe construtora para a cria��o das opera��es. Esta classe � baseada nos padr�es de design Abstract
 * Factory e Singleton.
 * Seu objetivo � criar as inst�ncias das opera��es e mant�-las em mem�ria para reutiliza��o dentro de uma mesma
 * sess�o do sistema. Tamb�m � respons�vel por ler as opera��es pr�-definidas de um arquivo XML durante
 * o in�cio do sistema. Segundo a modelagem do sistema, ao se criar novas opera��es basta atualizar esse arquivo
 * que a classe COperationFactory gerenciar� sua cria��o e disponibiliza��o para execu��o.  
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see CMacroOperation
 * @see COperation
 */

public class COperationFactory
{
	/** Membro privado est�tico utilizado para armazenar a inst�ncia �nica da classe (singleton). */
	private static COperationFactory m_pInstance = null;
	
	/** Membro privado utilizado para armazenar as opera��es criadas. */
	private Vector<COperation> m_vOperations;
	
	/** Membro provadi utilizado para armazenar exemplos de par�metros para as opera��es criadas. */
	private Map<String, String> m_vParamEx;
	
	/**
	 * Cosntrutor da classe.
	 */
	protected COperationFactory()
	{
		m_vOperations = new Vector<COperation>();
		m_vParamEx = new HashMap<String, String>();
	}

	/**
	 * M�todo getter protegido e est�tico para obten��o da inst�ncia default (singleton) da classe.
	 * � utilizado pelos outros m�todos est�ticos p�blicos para ter acesso � inst�ncia �nica.
	 * @return Retorna a inst�ncia �nica da classe COperationFactory. 
	 */
	protected static COperationFactory getInstance()
	{
		if(m_pInstance == null)
			m_pInstance = new COperationFactory();
		return m_pInstance;
	}
	
	/**
	 * M�todo de inicializa��o, utilizado para registrar e publicar no sistema as opera��es definidas
	 * em um arquivo XML. 
	 * @param sFile Nome do arquivo XML.
	 * @return Retorna CErrors.SUCCESS se o arquivo foi lido com sucesso e as opera��es registradas. Caso contr�rio
	 * retorna o c�digo do erro ocorrido.
	 */
	public static int registerOperationsFromFile(String sFile)
	{
		DocumentBuilder pFactory;
		Document pDoc;
		
		try
		{
			pFactory = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			pDoc = pFactory.parse(new File(sFile));
		}
		catch(ParserConfigurationException e)
		{
			return CErrors.ERROR_PARSING_XML_FILE;
		}
		catch(SAXException e)
		{
			return CErrors.ERROR_PARSING_XML_FILE;
		}
		catch(IOException e)
		{
			return CErrors.ERROR_READING_FILE;
		}
		
		Element pRoot = pDoc.getDocumentElement();
		
		NodeList pList = pRoot.getElementsByTagName("operation");
		if(pList == null)
			return CErrors.ERROR_PARSING_XML_FILE;
		
		for(int i = 0; i < pList.getLength(); i++)
		{
			Node pNode = pList.item(i);
			String sDesc = pNode.getTextContent().trim();
			sDesc = sDesc.replace("\n", " ").replace("\t", "");
			
			NamedNodeMap pAttrs = pNode.getAttributes();
			
			Node pClass = pAttrs.getNamedItem("class");
			if(pClass == null)
				return CErrors.ERROR_PARSING_XML_FILE;

			String sClass = pClass.getTextContent();

			Node pName = pAttrs.getNamedItem("name");
			if(pName == null)
				return CErrors.ERROR_PARSING_XML_FILE;

			String sName = pName.getTextContent();

			Node pParamEx = pAttrs.getNamedItem("paramexample");
			if(pParamEx == null)
				return CErrors.ERROR_PARSING_XML_FILE;

			String sParamEx = pParamEx.getTextContent();
			
			int iRet = COperationFactory.registerOperation(sClass, sName, sDesc, sParamEx);
			if(iRet != CErrors.SUCCESS)
				return iRet;
		}
		
		return CErrors.SUCCESS;
	}
	
	/**
	 * M�todo utilizado para registrar e publicar uma opera��o. Esse m�todo utiliza o Refletion do Java
	 * para criar e publicar a classe para utiliza��o no sistema sem a necessidade de conhec�-la previamente.
	 * 
	 * @param sClassName Nome da classe para cria��o. 
	 * @param sOperName Nome da opera��o para registro.
	 * @param sOperDesc Descri��o da opera��o pare registro.
	 * @param sParamEx Exemplo de par�metros (nome=valor) separados por ponto-e-v�rgula.
	 * @return Retorna CErrors.SUCCESS se a opera��o foi registrada com sucesso, ou um c�digo identificando o erro ocorrido.
	 */
	public static int registerOperation(String sClassName, String sOperName, String sOperDesc, String sParamEx)
	{
		COperationFactory pFactory = COperationFactory.getInstance();
		
		try
		{
			Class pClass = Class.forName(sClassName);
			String sSuper = pClass.getSuperclass().getName();
			if(!sSuper.equals("core.operations.COperation"))
				return CErrors.ERROR_INVALID_OPERATION_CLASS_NAME;
			
			Class aTypes[] = new Class[2];
			aTypes[0] = String.class;
			aTypes[1] = String.class;
			Constructor pConstructor = pClass.getConstructor(aTypes);
			
			Object aParams[] = new Object[2];
			aParams[0] = sOperName;
			aParams[1] = sOperDesc;
			
			COperation pOper = (COperation) pConstructor.newInstance(aParams);
			
			if(pOper == null)
				return CErrors.ERROR_INVALID_OPERATION_CLASS_NAME;
	
			pFactory.m_vParamEx.put(sOperName, sParamEx);
			pFactory.m_vOperations.add(pOper);
		}
		catch(Exception e)
		{
			return CErrors.ERROR_INVALID_OPERATION_CLASS_NAME;
		}
		
		return CErrors.SUCCESS;
	}

	/**
	 * M�todo getter para a obten��o de todas as opera��es registradas no Factory.
	 * @return Vetor com as opera��es registradas.
	 */
	public static Vector<COperation> getRegisteredOperations()
	{
		COperationFactory pFactory = COperationFactory.getInstance();
		return pFactory.m_vOperations;
	}
	
	/**
	 * M�todo getter para a obten��o de uma opera��o registrada, baseado em seu nome.
	 * @param sName Nome da opera��o para obten��o.
	 * @return Inst�ncia da classe COperation com a opera��o obtida ou null se o nome for inv�lido.
	 */
	public static COperation getOperation(String sName)
	{
		COperationFactory pFactory = COperationFactory.getInstance();
		COperation pRet = null;
		
		for(int i = 0; i < pFactory.m_vOperations.size(); i++)
		{
			COperation pOper = pFactory.m_vOperations.get(i);			
			if(pOper.getName().equals(sName))
			{
				pRet = pOper;
				break;
			}
		}
		return pRet;
	}
	
	/**
	 * M�todo getter para a obten��o do exemplo de par�metros de uma opera��o registrada, baseado em seu nome.
	 * @param sName Nome da opera��o para obten��o do exemplo de par�metros.
	 * @return Texto com o exemplo de par�metros separados por ponto-e-v�rgula (;).
	 */
	public static String getParamExample(String sName)
	{
		COperationFactory pFactory = COperationFactory.getInstance();
		return pFactory.m_vParamEx.get(sName);
	}
}