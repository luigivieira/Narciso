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

/**
 * Classe para o armazenamento de itens individuais de propriedades, herdada de CPropertyBag. 
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see CPropertyBag
 * @see CPropertyComposite
 *
 */
public class CPropertyItem extends CPropertyBag
{
	/** Membro privado utilizado para armazenar o valor inteiro da propriedade, caso esse seja seu tipo definido. */ 
	private int m_iIntValue;

	/** Membro privado utilizado para armazenar o valor textual da propriedade, caso esse seja seu tipo definido. */
	private String m_sStringValue;
	 
	/** Membro privado utilizado para armazenar o valor l�gico da propriedade, caso esse seja seu tipo definido. */
	private boolean m_bBoolValue;
	 
	/** Membro privado utilizado para armazenar o valor em ponto flutuante da propriedade, caso esse seja seu tipo definido. */
	private float m_fFloatValue;
	 
	/** Membro privado utilizado para armazenar o valor em ponto flutuante duplo da propriedade, caso esse seja seu tipo definido. */
	private double m_dDoubleValue;

	/**
	 * Construtor da classe.
	 *  
	 * @param sName Nome da propriedade.
	 * @param eType Tipo da propriedade, conforme defini��o em CPropertyTypeEnum. 
	 */
	public CPropertyItem(String sName, CPropertyTypeEnum eType)
	{
		super(sName, eType);
		m_iIntValue = -1;
		m_sStringValue = null;
		m_bBoolValue = false;
		m_fFloatValue = 0.0F;
		m_dDoubleValue = 0.0;
	}

	/**
	 * M�todo utilizado para converter o valor da propriedade, qualquer que seja seu tipo, para texto.
	 * 
	 * @return Texto com o valor da propriedade.
	 */
	public String toString()
	{
		if(m_eType == CPropertyTypeEnum.INTEGER)
			return String.valueOf(m_iIntValue);
		else if(m_eType == CPropertyTypeEnum.STRING)
			return m_sStringValue;
		else if(m_eType == CPropertyTypeEnum.BOOLEAN)
			return String.valueOf(m_bBoolValue);
		else if(m_eType == CPropertyTypeEnum.FLOAT)
			return String.valueOf(m_fFloatValue);
		else if(m_eType == CPropertyTypeEnum.DOUBLE)
			return String.valueOf(m_dDoubleValue);
		else
			return "";
	}
	
	/**
	 * M�todo setter do valor da propriedade, utilizado quando seu tipo � inteiro. Se esse n�o for o tipo
	 * definido, o m�todo simplesmente nada executa e o valor da propriedade n�o � alterado.
	 * 
	 * @param iValue Novo valor para a propriedade.
	 */
	public void setInt(int iValue)
	{
		if(m_eType == CPropertyTypeEnum.INTEGER)
			m_iIntValue = iValue;
	}

	/**
	 * M�todo getter que obtem o valor atual da propriedade, utilizado quando seu tipo � inteiro. Se esse n�o for o tipo
	 * definido, o retorno ser� o valor default (0).
	 * 
	 * @return Valor atual da propriedade.
	 */
	public int getInt()
	{
		return m_iIntValue;
	}
	 
	/**
	 * M�todo setter do valor da propriedade, utilizado quando seu tipo � texto. Se esse n�o for o tipo
	 * definido, o m�todo simplesmente nada executa e o valor da propriedade n�o � alterado.
	 * 
	 * @param sValue Novo valor para a propriedade.
	 */
	public void setString(String sValue)
	{
		if(m_eType == CPropertyTypeEnum.STRING)
			m_sStringValue = sValue;
	}
	 
	/**
	 * M�todo getter que obtem o valor atual da propriedade, utilizado quando seu tipo � texto. Se esse n�o for o tipo
	 * definido, o retorno ser� o valor default (null).
	 * 
	 * @return Valor atual da propriedade.
	 */
	public String getString()
	{
		return m_sStringValue;
	}
	 
	/**
	 * M�todo setter do valor da propriedade, utilizado quando seu tipo � l�gico. Se esse n�o for o tipo
	 * definido, o m�todo simplesmente nada executa e o valor da propriedade n�o � alterado.
	 * 
	 * @param bValue Novo valor para a propriedade.
	 */
	public void setBool(boolean bValue)
	{
		if(m_eType == CPropertyTypeEnum.BOOLEAN)
			m_bBoolValue = bValue;
	}
	 
	/**
	 * M�todo getter que obtem o valor atual da propriedade, utilizado quando seu tipo � l�gico. Se esse n�o for o tipo
	 * definido, o retorno ser� o valor default (false).
	 * 
	 * @return Valor atual da propriedade.
	 */
	public boolean getBool()
	{
		return m_bBoolValue;
	}
	 
	/**
	 * M�todo setter do valor da propriedade, utilizado quando seu tipo � ponto flutuante. Se esse n�o for o tipo
	 * definido, o m�todo simplesmente nada executa e o valor da propriedade n�o � alterado.
	 * 
	 * @param fValue Novo valor para a propriedade.
	 */
	public void setFloat(float fValue)
	{
		if(m_eType == CPropertyTypeEnum.FLOAT)
			m_fFloatValue = fValue;
	}
	 
	/**
	 * M�todo getter que obtem o valor atual da propriedade, utilizado quando seu tipo � ponto flutuante. Se esse n�o for o tipo
	 * definido, o retorno ser� o valor default (0.0).
	 * 
	 * @return Valor atual da propriedade.
	 */
	public float getFloat()
	{
		return m_fFloatValue;
	}
	 
	/**
	 * M�todo setter do valor da propriedade, utilizado quando seu tipo � ponto flutuante duplo. Se esse n�o for o tipo
	 * definido, o m�todo simplesmente nada executa e o valor da propriedade n�o � alterado.
	 * 
	 * @param iValue Novo valor para a propriedade.
	 */
	public void setDouble(double dValue)
	{
		if(m_eType == CPropertyTypeEnum.DOUBLE)
			m_dDoubleValue = dValue;
	}
	 
	/**
	 * M�todo getter que obtem o valor atual da propriedade, utilizado quando seu tipo � ponto flutuante duplo. Se esse n�o for o tipo
	 * definido, o retorno ser� o valor default (0.0).
	 * 
	 * @return Valor atual da propriedade.
	 */
	public double getDouble()
	{
		return m_dDoubleValue;
	}
}