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
 
package core.info;

import java.util.*;

/**
 * Classe básica para o armazenamento e manipulação de propriedades de imagens processadas pelo sistema Narciso.
 * A PropertyBag é como um pacote ou "sacola" que armazena todas as propriedades em uma árvore recursiva composta por itens
 * individuais (CPropertyItem) ou subconjuntos de propriedades (CPropertyComposite).
 * Seguindo o padrão de design chamado Composite, essa classe é a classe abstrata para seus dois tipos de filhos,
 * implementando assim alguns métodos básicos para estes. 
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see CPropertyItem
 * @see CPropertyComposite
 *
 */

public abstract class CPropertyBag
{
	/** Membro publico estático utilizado na enumeração dos tipos de dados armazenados como propriedades. */
	public static enum CPropertyTypeEnum { COMPOSITE, STRING, INTEGER, BOOLEAN, FLOAT, DOUBLE };

	/** Membro utilizado para armazenar o nome da propriedade. */
	protected String m_sName;
	
	/** Membro utilizado para armazenar o tipo da propriedade. */
	protected CPropertyTypeEnum m_eType;
	 
	/**
	 * Construtor da classe.
	 * 
	 * @param sName Nome da propriedade.
	 * @param eType Tipo da propriedade.
	 */
	public CPropertyBag(String sName, CPropertyTypeEnum eType)
	{
		setName(sName);
		m_eType = eType;
	}
	 
	/**
	 * Método setter utilizado para definir o nome da propriedade.
	 * 
	 * @param sName Novo nome para a propriedade.
	 */
	public void setName(String sName)
	{
		m_sName = sName;
	}
	 
	/**
	 * Método getter utilizado para obter o nome da propriedade.
	 * 
	 * @return Nome da propriedade.
	 */
	public String getName()
	{
		return m_sName;
	}
	 
	/**
	 * Método getter para obter o tipo da propriedade. 
	 * 
	 * @return Valor, conforme definição em CPropertyTypeEnum, com o tipo da propriedade. 
	 */
	public CPropertyTypeEnum getType()
	{
		return m_eType;
	}
	
	/**
	 * Método estático publico utilizado para converter os valores de propriedades armazenadas sob 
	 * o pacote dado em um mapa de strings.
	 * 
	 * @param pBag Pacote de Propriedades (CPropertyBag) a ser convertido.
	 * 
	 * @return Mapa de strings (Map<String, String>) com as propriedades convertidas.
	 */
	static public Map<String, String> toMap(CPropertyBag pBag)
	{
		Map<String, String> pRet = new LinkedHashMap<String, String>();
		
		if(pBag.getType() == CPropertyBag.CPropertyTypeEnum.COMPOSITE)
		{
			CPropertyComposite pComp = (CPropertyComposite) pBag;
			for(int i = 0; i < pComp.getPropertyCount(); i++)
			{
				CPropertyBag pCur = pComp.getPropertyByIndex(i);
				Map<String, String> pAux = new LinkedHashMap<String, String>();
				pAux = toMap(pCur);
				pRet.putAll(pAux);
			}
		}
		else
		{
			String sName = pBag.getName();
			String sValue = ((CPropertyItem) pBag).toString();
			pRet.put(sName, sValue);
		}
		
		return pRet;
	}
}