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

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import core.errors.*;

/**
 * Classe básica para a implementação de roteiros de operações no sistema Narciso. Essa classe segue o padrão
 * de design Command, e por isso herda de COperation.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see CMacroOperation
 * @see COperationFactory
 */

public class CMacroOperation extends COperation
{
	/** Membro privado utilizado para armazenar as operações contidas no roteiro. */
	private Map<String, COperation> m_aOperations;
	
	/** Membro privado utilizado para armazenar o número de iterações para cada operação contida no roteiro. */
	private Map<String, Integer> m_aIterations;

	/**
	 * Cosntrutor da classe.
	 * 
	 * @param sName Nome do roteiro.
	 * @param sDesc Descrição do roteiro.
	 */
	public CMacroOperation(String sName, String sDesc)
	{
		super(sName, sDesc);
		m_aOperations = new LinkedHashMap<String, COperation>();
		m_aIterations = new LinkedHashMap<String, Integer>();
	}

	/**
	 * Método utilizado para salvar a configuração do roteiro de operações em um arquivo XML para utilização posterior.
	 * 
	 * @param sFile Nome do arquivo XML para gravação do roteiro.
	 * 
	 * @return Retorna CErrors.SUCCESS se o arquivo foi gravado com sucesso, ou um codigo indicando o erro de gravação.
	 */
	public int saveToFile(String sFile)
	{
		DocumentBuilder pFactory;
		Document pDoc;
		
		try
		{
			pFactory = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			pDoc = pFactory.newDocument();
		}
		catch(ParserConfigurationException e)
		{
			return CErrors.ERROR_CREATING_XML_DOCUMENT;
		}
		
		try
		{
			Map.Entry pPair;
			COperation pCur;
			Element pElem;
			int iIter;
			
			Element pRoot = pDoc.createElement("macro_operation");
			pDoc.appendChild(pRoot);
			
			pRoot.setAttribute("name", getName());
			pRoot.setTextContent(getDescription());
				
			Iterator itOper = m_aOperations.entrySet().iterator();
			Iterator itIter = m_aIterations.entrySet().iterator();
			
			while(itOper.hasNext())
			{
				pPair = (Map.Entry) itOper.next();
				pCur = (COperation) pPair.getValue();
				pPair = (Map.Entry) itIter.next();
				
				if(pPair == null)
					return CErrors.ERROR_UNEXPECTED_ERROR;
				
				iIter = (Integer) pPair.getValue();
				
				pElem = (Element) pRoot.appendChild(pDoc.createElement("operation"));
				pElem.setAttribute("name", pCur.getName());
				pElem.setAttribute("iterations", String.valueOf(iIter));
			}
			
		    FileOutputStream fHandle = new FileOutputStream(sFile);
		    Transformer pTransf = TransformerFactory.newInstance().newTransformer();
		    DOMSource pDocSource = new DOMSource(pDoc);
		    StreamResult pStm = new StreamResult(fHandle);
		    pTransf.transform(pDocSource, pStm);
		    fHandle.close();
		}
		catch(IOException e)
		{
			return CErrors.ERROR_READING_FILE;
		}
		catch(Exception e)
		{
			return CErrors.ERROR_PARSING_XML_FILE;
		}
		
		return CErrors.SUCCESS;
	}

	/**
	 * Método utilizado para ler a configuração de roteiro a partir de um arquivo XML existente no disco.
	 * 
	 * @param sFile Nome do arquivo XML para leitura.
	 * 
	 * @return Retorna CErrors.SUCCESS se o arquivo foi lido com sucesso, ou um codigo indicando o erro de leitura.
	 */
	public int loadFromFile(String sFile)
	{
		DocumentBuilder pFactory;
		Document pDoc;

		clearOperations();
		
		try
		{
			pFactory = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			pDoc = pFactory.parse(new File(sFile));
		}
		catch(ParserConfigurationException e)
		{
			return CErrors.ERROR_CREATING_XML_DOCUMENT;
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
		if(!pRoot.getNodeName().equals("macro_operation"))
			return CErrors.ERROR_PARSING_XML_FILE;
		
		String sDesc = pRoot.getTextContent();		
		setDescription(sDesc);
		
		NodeList pList = pRoot.getElementsByTagName("operation");
		if(pList == null || pList.getLength() <= 0)
			return CErrors.ERROR_PARSING_XML_FILE;
		
		for(int i = 0; i < pList.getLength(); i++)
		{
			Node pNode = pList.item(i);
			NamedNodeMap pAttrs = pNode.getAttributes();
			
			Node pName = pAttrs.getNamedItem("name");
			if(pName == null)
				return CErrors.ERROR_PARSING_XML_FILE;
			
			String sName = pName.getTextContent();
			
			Node pIter = pAttrs.getNamedItem("iterations");
			if(pIter == null)
				return CErrors.ERROR_PARSING_XML_FILE;
			
			int iIter = Integer.parseInt(pIter.getTextContent());
			
			COperation pOper = COperationFactory.getOperation(sName);
			if(pOper != null)
				addOperation(pOper, iIter);
			else
				return CErrors.ERROR_INVALID_OPERATION_CLASS_NAME;
		}
		
		return CErrors.SUCCESS;
	}
	 
	/**
	 * Método utilizado para adicionar uma operação ao roteiro.
	 * 
	 * @param pOperation Objeto COperation com a operação a ser adicionada.
	 * @param iIter Valor inteiro com o número de vezes que a operação deverá ser executada. Deve ser no mínimo 1.
	 */
	public void addOperation(COperation pOperation, int iIter)
	{
		String sName = pOperation.getName();
		
		if(m_aOperations.get(sName) != null)
		{
			m_aOperations.remove(sName);
			m_aIterations.remove(sName);
		}
		
		if(iIter < 1)
			iIter = 1;
		
		m_aOperations.put(sName, pOperation);
		m_aIterations.put(sName, iIter);
	}

	/**
	 * Método getter para a obtenção do número total de operações contidas no roteiro.
	 * @return Número de operações.
	 */
	public int getOperationCount()
	{
		return m_aOperations.size();
	}
	 
	/**
	 * Método getter para a obtenção de uma operação do roteiro baseado em seu índice numérico.
	 * 
	 * @param iIndex Índice da operação desejada.
	 * @return Retorna o objeto COperation com a operação obtida ou null se o índice for inválido.
	 */
	public COperation getOperationByIndex(int iIndex)
	{
		if(iIndex < 0 || iIndex >= m_aOperations.size())
			return null;
		
		Iterator it = m_aOperations.entrySet().iterator();		
		for(int i = 0; it.hasNext(); i++)
		{
			Map.Entry pPair = (Map.Entry) it.next();
			if(i == iIndex)
				return (COperation) pPair.getValue();
		}
		
		return null;
	}

	/**
	 * Método getter para a obtenção de uma operação do roteiro baseado em seu nome.
	 * 
	 * @param sName Nome da operação desejada.
	 * @return Retorna o objeto COperation com a operação obtida ou null se o nome for inválido.
	 */	
	public COperation getOperationByName(String sName)
	{
		return m_aOperations.get(sName);
	}
	 
	/**
	 * Método utilizado para remover a operação dada do roteiro.
	 * @param sName Nome da operação a ser removida. Se inválido, o método simplesmente não executa.
	 */
	public void removeOperationByName(String sName)
	{
		m_aOperations.remove(sName);
		m_aIterations.remove(sName);
	}
	
	/**
	 * Método utilizaod para remover a operação dada do roteiro, baseado em seu índice.
	 * @param iIndex Índice da operação a ser removida. Se inválido, o método simplesmente não executa.
	 */
	public void removeOperationByIndex(int iIndex)
	{
		if(iIndex < 0 || iIndex >= m_aOperations.size())
			return;
		
		Iterator it = m_aOperations.entrySet().iterator();		
		for(int i = 0; it.hasNext(); i++)
		{
			Map.Entry pPair = (Map.Entry) it.next();
			if(i == iIndex)
			{
				m_aOperations.remove(pPair.getKey());
				m_aIterations.remove(pPair.getKey());
				return;
			}
		}
		
		return;
	}
	 
	/**
	 * Remove todas as operações existentes no roteiro.
	 */
	public void clearOperations()
	{
		m_aOperations.clear();
		m_aIterations.clear();
	}

	/**
	 * Método getter para a obtenção do número de iterações de uma operação baseado em seu nome.
	 * @param sName Nome da operação para a obtenção do número de iterações.
	 * @return Número de iterações configurada para a operação no roteiro.
	 */
	public int getOperationIterations(String sName)
	{
		return m_aIterations.get(sName);
	}

	/**
	 * Método setter para a configuração do número de iterações de uma operação baseado em seu nome.
	 * @param sName Nome da operação para configuração do número de iterações.
	 * @param iIter Novo valor para as iterações da operação.
	 */
	public void setOperationIterations(String sName, int iIter)
	{
		m_aIterations.remove(sName);
		m_aIterations.put(sName, iIter);
	}
	
	/**
	 * Executa o roteiro, executando cada uma das operações nele contidas. Se uma das operações for também
	 * um Roteiro, este será executado recursivamente, segundo o padrão de design utilizado.
	 * 
	 * Os objetos-fonte do roteiro são passados à primeira operação, e o resultado desta é passado para a segunda,
	 * e assim por diante. Deste modo, o resultado deste método é o resultado dá última operação executada.
	 * Os parâmetros definidos em pParams são sempre passados para todas as operações sem alteração. Se um erro é encontrado,
	 * o roteiro termina, e o parâmetro de erro (definido pela chave "error") contido em pParams indicará o código de erro
	 * da última operação executada.
	 * 
	 * <b>Importante:</b> Como a saída de uma operação é a entrada para a seguinte, deve-se ter atenção aos parâmetros e número de objetos de entrada
	 * requeridos e gerados por cada operação.
	 * 
	 * @param pSource Vetor de objetos básicos do Java, a serem passados para o roteiro.
	 * @param pParams
	 * @return Retorna um vetor com os objetos gerados pela última operação executada, ou null se ocorreu um erro.
	 */
	@Override
	public Vector<Object> execute(Vector<Object> pSource, Properties pParams)
	{
		Map.Entry pPair;
		COperation pCur;
		int iIter, i;		
		Vector<Object> pObjects = pSource;
		Iterator itOper = m_aOperations.entrySet().iterator();
		Iterator itIter = m_aIterations.entrySet().iterator();
		
		while(itOper.hasNext())
		{
			pPair = (Map.Entry) itOper.next();
			pCur = (COperation) pPair.getValue();
			pPair = (Map.Entry) itIter.next();
			
			if(pPair == null || !pPair.getKey().equals(pCur.getName()))
			{
				pParams.put("error", String.valueOf(CErrors.ERROR_UNEXPECTED_ERROR));
				return null;
			}

			iIter = (Integer) pPair.getValue();
			
			for(i = 0; i < iIter; i++)
			{
				pObjects = pCur.execute(pObjects, pParams);
				if(pObjects == null)
					return null;
			}
		}
		return pObjects;
	}
}