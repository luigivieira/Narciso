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
 
package core.config;

import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import org.w3c.dom.*;
import org.xml.sax.*;
import core.errors.*;

/**
 * Essa classe implementa o controle de configura��o do sistema Narciso, armazenando e permitindo a consulta
 * �s vari�veis do sistema e controlando a persist�ncia (grava��o) em disco.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

public class CConfiguration
{
	/**
	 * Membro privado est�tico utilizado para definir o modo de distribui��o dos pixels no sistema.
	 * S�o duas as op��es definidas: SQUARED, que descreve um formato quadrado de distribui��o, e 
	 * HEXAGONAL, que descreve um formato hexagonal de distribui��o.
	 */
	public static enum CDistributionModeEnum { SQUARED, HEXAGONAL };
	
	/**
	 * Membro privado est�tico utilizado para definir o modo de agrega��o de pixeis em regi�es.
	 * S�o duas as op��es definidas: REGION_OF_2x2, que descreve uma regi�o de vizinhan�a formada 
	 * por 4 pixels (duas linhas e duas colunas) e REGION_OF_3x3, que descreve uma regi�o de vizinhan�a
	 * formada por 9 pixels (tr�s linhas e tr�s colunas) 
	 */
	public static enum CNeighboringModeEnum { REGION_OF_2x2, REGION_OF_3x3 };

	/**
	 * Membro privado que armazena o valor configurado para a regi�o de vizinhan�a utilizada pelo Narciso.
	 * Na vers�o atual essa configura��o ainda n�o � utilizada, existindo apenas para aplica��es futuras. 
	 */	
	private CNeighboringModeEnum m_eNeighboringMode;
	 
	/**
	 * Membro privado que armazena o valor configurado para o modo de distribui��o utilizado pelo Narciso.
	 * Na vers�o atual essa configura��o ainda n�o � utilizada, existindo apenas para aplica��es futuras. 
	 */	
	private CDistributionModeEnum m_eDistributionMode;
	
	/**
	 * Membro privado que armazena o valor do �ltimo diret�rio de carregamento de arquivos utilizado pelo Narciso. 
	 */	
	private String m_sLastLoadPath;
	
	/**
	 * Membro privado que armazena o valor de indica��o de abertura de uma imagem ao ser carregada.
	 * Na vers�o atual essa configura��o ainda n�o � utilizada, existindo apenas para aplica��es futuras. 
	 */	
	private boolean m_bOpenImageOnLoad;

	/**
	 * Membro privado est�tico utilizado para conter a inst�ncia �nica (singleton) da classe CConfiguration. 
	 */		
	private static CConfiguration m_pInstance;

	/**
	 * Construtor da classe CConfiguration.
	 *  
	 */		
	protected CConfiguration()
	{
		m_pInstance = null;
		m_eNeighboringMode = CNeighboringModeEnum.REGION_OF_2x2;
		m_eDistributionMode = CDistributionModeEnum.SQUARED;
		m_sLastLoadPath = ".";
		m_bOpenImageOnLoad = false;
	}
	 
	/**
	 * M�todo est�tico e protegido, utilizado na cria��o da �nica inst�ncia (singleton) da classe.
	 * Todos os demais m�todos utiliz�veis da classe s�o definidos como est�ticos, e utilizam esse
	 * m�todo para ter acesso � inst�ncia �nica.
	 * 
	 * @return Retorna a inst�ncia do objeto CConfiguration.
	 */	
	protected static CConfiguration gettInstance()
	{
		if(m_pInstance == null)
			m_pInstance = new CConfiguration();
		return m_pInstance;
	}
	 
	/**
	 *  Salva os valores de configura��o armazenados na classe no arquivo dado, em formato XML.
	 * 
	 * @param sFile Nome do arquivo XML a ser gravado.
	 * @return Retorna CErrors.SUCCESS se o arquivo foi gravado com sucesso, ou um c�digo indicando o erro ocorrido.  
	 */	
	public static int save(String sFile)
	{
		CConfiguration pConfig = CConfiguration.gettInstance();
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
			Element pRoot = pDoc.createElement("narciso");
			pDoc.appendChild(pRoot);
		
			Node pDM = pRoot.appendChild(pDoc.createElement("distribution_mode"));
			if(pConfig.m_eDistributionMode == CDistributionModeEnum.SQUARED)
				pDM.appendChild(pDoc.createTextNode("SQUARED"));
			else if(pConfig.m_eDistributionMode == CDistributionModeEnum.HEXAGONAL)
				pDM.appendChild(pDoc.createTextNode("HEXAGONAL"));
			else
				return CErrors.ERROR_INVALID_CONFIG_VALUE;
			
			Node pNM = pRoot.appendChild(pDoc.createElement("neighboring_mode"));
			if(pConfig.m_eNeighboringMode == CNeighboringModeEnum.REGION_OF_2x2) 
				pNM.appendChild(pDoc.createTextNode("2x2"));
			else if(pConfig.m_eNeighboringMode == CNeighboringModeEnum.REGION_OF_3x3)
				pNM.appendChild(pDoc.createTextNode("3x3"));
			else
				return CErrors.ERROR_INVALID_CONFIG_VALUE;
		
			Node pPath = pRoot.appendChild(pDoc.createElement("last_load_path"));			
			pPath.appendChild(pDoc.createTextNode(pConfig.m_sLastLoadPath));
			
			Node pOpen = pRoot.appendChild(pDoc.createElement("open_image_on_load"));
			if(pConfig.m_bOpenImageOnLoad)
				pOpen.appendChild(pDoc.createTextNode("true"));
			else
				pOpen.appendChild(pDoc.createTextNode("false"));
			
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
	 *  L� os valores de configura��o a partir do arquivo dado, em formato XML.
	 * 
	 * @param sFile Nome do arquivo XML a ser lido.
	 * @return Retorna CErrors.SUCCESS se o arquivo foi lido com sucesso, ou um c�digo indicando o erro ocorrido.  
	 */	
	public static int load(String sFile)
	{
		CConfiguration pConfig = CConfiguration.gettInstance();
		DocumentBuilder pFactory;
		Document pDoc;
		
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
		if(!pRoot.getNodeName().equals("narciso"))
			return CErrors.ERROR_INVALID_CONFIG_VALUE;
		
		NodeList pDM = pRoot.getElementsByTagName("distribution_mode");
		if(pDM == null || pDM.getLength() != 1)
			return CErrors.ERROR_INVALID_CONFIG_VALUE;
		
		String sDM = pDM.item(0).getTextContent();
		if(sDM.equals("SQUARED"))
			pConfig.m_eDistributionMode = CDistributionModeEnum.SQUARED;
		else if(sDM.equals("HEXAGONAL"))
			pConfig.m_eDistributionMode = CDistributionModeEnum.HEXAGONAL;
		else
			return CErrors.ERROR_INVALID_CONFIG_VALUE;

		NodeList pNM = pRoot.getElementsByTagName("neighboring_mode");
		if(pNM == null || pNM.getLength() != 1)
			return CErrors.ERROR_INVALID_CONFIG_VALUE;
		
		String sNM = pNM.item(0).getTextContent();
		if(sNM.equals("2x2"))
			pConfig.m_eNeighboringMode = CNeighboringModeEnum.REGION_OF_2x2;
		else if(sNM.equals("3x3"))
			pConfig.m_eNeighboringMode = CNeighboringModeEnum.REGION_OF_3x3;
		else
			return CErrors.ERROR_INVALID_CONFIG_VALUE;
		
		NodeList pPath = pRoot.getElementsByTagName("last_load_path");
		if(pPath == null || pPath.getLength() != 1)
			return CErrors.ERROR_INVALID_CONFIG_VALUE;
		
		pConfig.m_sLastLoadPath = pPath.item(0).getTextContent();

		NodeList pOpen = pRoot.getElementsByTagName("open_image_on_load");
		if(pOpen == null || pOpen.getLength() != 1)
			return CErrors.ERROR_INVALID_CONFIG_VALUE;
		
		String sOpen = pOpen.item(0).getTextContent();
		if(sOpen.equals("true"))
			pConfig.m_bOpenImageOnLoad = true;
		else if(sOpen.equals("false"))
			pConfig.m_bOpenImageOnLoad = false;
		else
			return CErrors.ERROR_INVALID_CONFIG_VALUE;
		
		return CErrors.SUCCESS;
	}
	 
	/**
	 * M�todo setter da configura��o de regi�o de vizinhan�a utilizada pelo Narciso. Apesar de dispon�vel,
	 * essa configura��o ainda n�o � utilizada nessa vers�o do sistema. 
	 * 
	 * @param eValue Valor da regi�o de vizinhan�a, conforme defini��o em CNeighboringModeEnum.  
	 */	
	public static void setNeighboringMode(CNeighboringModeEnum eValue)
	{
		CConfiguration pConfig = CConfiguration.gettInstance();
		pConfig.m_eNeighboringMode = eValue;
	}

	/**
	 * M�todo getter da configura��o de regi�o de vizinhan�a utilizada pelo Narciso. Apesar de dispon�vel,
	 * essa configura��o ainda n�o � utilizada nessa vers�o do sistema. 
	 * 
	 * @return Valor corrente de configura��o da regi�o de vizinhan�a, conforme defini��o em CNeighboringModeEnum.  
	 */	
	public static CNeighboringModeEnum getNeighboringMode()
	{
		CConfiguration pConfig = CConfiguration.gettInstance();
		return pConfig.m_eNeighboringMode;		
	}
	 
	/**
	 * M�todo setter da configura��o de distribui��o utilizada pelo Narciso. Apesar de dispon�vel,
	 * essa configura��o ainda n�o � utilizada nessa vers�o do sistema. 
	 * 
	 * @param eValue Valor da distribui��o, conforme defini��o em CDistributionModeEnum.  
	 */	
	public static void setDistributionMode(CDistributionModeEnum eValue)
	{
		CConfiguration pConfig = CConfiguration.gettInstance();
		pConfig.m_eDistributionMode = eValue;
	}
	 
	/**
	 * M�todo getter da configura��o de distribui��o utilizada pelo Narciso. Apesar de dispon�vel,
	 * essa configura��o ainda n�o � utilizada nessa vers�o do sistema. 
	 * 
	 * @return Valor corrente de configura��o de distribui��o, conforme defini��o em CDistributionModeEnum.  
	 */	
	public static CDistributionModeEnum getDistributionMode()
	{
		CConfiguration pConfig = CConfiguration.gettInstance();
		return pConfig.m_eDistributionMode;
	}

	/**
	 * M�todo setter do valor do �ltimo diret�rio utilizado pelo Narciso no carregamento de imagens. Essa configura��o
	 * � de utiliza��o interna do sistema de forma transparente para o usu�rio.
	 * 
	 * @param sValue String com o diret�rio completo do �ltimo carregamento de imagens.
	 */
	public static void setLastLoadPath(String sValue)
	{
		if(sValue != "")
		{
			CConfiguration pConfig = CConfiguration.gettInstance();
			pConfig.m_sLastLoadPath = sValue;
		}
	}
	 
	/**
	 * M�todo getter do valor do �ltimo diret�rio utilizado pelo Narciso no carregamento de imagens. Essa configura��o
	 * � de utiliza��o interna do sistema de forma transparente para o usu�rio.
	 * 
	 * @return String com o diret�rio completo do �ltimo carregamento de imagens.
	 */
	public static String getLastLoadPath()
	{
		CConfiguration pConfig = CConfiguration.gettInstance();
		return pConfig.m_sLastLoadPath;
	}
	
	/**
	 * M�todo setter da configura��o de indica��o de abertura de imagens no momento do carregamento. Apesar de dispon�vel,
	 * essa configura��o ainda n�o � utilizada nessa vers�o do sistema. 
	 * 
	 * @param bValue Valor l�gico (true ou false) indicando o novo valor para a configura��o.
	 */
	public static void setOpenImageOnLoad(boolean bValue)
	{
		CConfiguration pConfig = CConfiguration.gettInstance();
		pConfig.m_bOpenImageOnLoad = bValue;
	}

	/**
	 * M�todo getter da configura��o de indica��o de abertura de imagens no momento do carregamento. Apesar de dispon�vel,
	 * essa configura��o ainda n�o � utilizada nessa vers�o do sistema. 
	 * 
	 * @return Valor l�gico (true ou false) indicando o valor corrente da configura��o.
	 */
	public static boolean getOpenImageOnLoad()
	{
		CConfiguration pConfig = CConfiguration.gettInstance();
		return pConfig.m_bOpenImageOnLoad;
	}
}