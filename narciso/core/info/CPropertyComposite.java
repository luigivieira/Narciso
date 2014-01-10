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
 
package core.info;

import java.util.*;

/**
 * Classe para o armazenamento de subconjuntos de propriedades, herdada de CPropertyBag. 
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see CPropertyBag
 * @see CPropertyItem
 *
 */

public class CPropertyComposite extends CPropertyBag
{
	/** Membro privado utilizado para armazenar o vetor com as propriedades do subconjunto */
	private Vector<CPropertyBag> m_aProperties;
	 
	/**
	 * Construtor da classe.
	 * 
	 * @param sName Nome do subconjunto.
	 */
	public CPropertyComposite(String sName)
	{
		super(sName, CPropertyTypeEnum.COMPOSITE);
		m_aProperties = new Vector<CPropertyBag>();
	}
	 
	/**
	 * M�todo utilizado para adicionar uma propriedade (utilizando polimorfismo em uma classe CPropertyBag)
	 * ao subconjunto. O polimorfismo garante que possam ser utilizados v�rios n�veis de subconjuntos, como uma 
	 * �rvore recursiva.
	 * 
	 * @param pBag Objeto CPropertyBag com a propriedade ou subconjunto a ser adicionado.
	 * 
	 * @return Retorna a pr�pria inst�ncia da propriedade adicionada.
	 */
	public CPropertyBag addProperty(CPropertyBag pBag)
	{
		String sName = pBag.getName();
		CPropertyBag pTemp = getPropertyByName(sName);
		if(pTemp != null)
			return pTemp;
	
		m_aProperties.add(pBag);
		return pBag;
	}
	 
	/**
	 * M�todo utilizado para remover uma propriedade (utilizando polimorfismo em uma classe CPropertyBag)
	 * do subconjunto, baseando-se em seu nome.
	 * 
	 * @param sName Nome da propriedade a ser removida.
	 */
	public void removePropertyByName(String sName)
	{
		CPropertyBag pBag = getPropertyByName(sName);
		if(pBag != null)
			m_aProperties.remove(pBag);
	}
	 
	/**
	 * M�todo utilizado para remover uma propriedade (utilizando polimorfismo em uma classe CPropertyBag)
	 * do subconjunto, baseando-se em seu �ndice.
	 * 
	 * @param iIndex �ndice da propriedade a ser removida.
	 */
	public void removePropertyByIndex(int iIndex)
	{
		if(iIndex < 0 || iIndex >= m_aProperties.size())
			return;
		else
			m_aProperties.remove(iIndex);
	}
	 
	/**
	 * M�todo getter utilizado para obter o n�mero de propriedades contidas no subconjunto.
	 * @return
	 */
	public int getPropertyCount()
	{
		return m_aProperties.size();
	}
	 
	/**
	 * M�todo getter utilizado para obter uma propriedade (utilizando polimorfismo em uma classe CPropertyBag),
	 * baseando-se em seu nome.
	 * 
	 * @param sName Nome da propriedade a ser obtida.
	 * 
	 * @return Objeto CPropertyBag com a propriedade obtida ou null se o nome n�o foi encontrado.
	 */
	public CPropertyBag getPropertyByName(String sName)
	{
		Iterator it = m_aProperties.iterator();
		while(it.hasNext())
		{
			CPropertyBag pBag = (CPropertyBag) it.next();
			String sBagName = pBag.getName();
			if(sBagName.equals(sName))
				return pBag;
		}
		return null;
	}
	 
	/**
	 * M�todo getter utilizado para obter uma propriedade (utilizando polimorfismo em uma classe CPropertyBag),
	 * baseando-se em seu �ndice.
	 * 
	 * @param iIndex �ndice da propriedade a ser obtida.
	 * 
	 * @return Objeto CPropertyBag com a propriedade obtida ou null se o �ndice n�o foi encontrado.
	 */
	public CPropertyBag getPropertyByIndex(int iIndex)
	{
		if(iIndex < 0 || iIndex >= m_aProperties.size())
			return null;
		else
			return m_aProperties.get(iIndex);
	}
	 
	/**
	 * Remove todas as propriedades configuradas no subconjunto.
	 */
	public void clearProperties()
	{
		m_aProperties.clear();
	}
}