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
 
package core.images;

import java.util.*;

/**
 * Esta classe implementa um Factory (construtor) de formatos de leitura e grava��o de imagens para o sistema Narciso.
 * Segue o padr�o de design Abstract Factory, e � utilizada pelo sistema quando precisa ler ou gravar um arquivo de imagem,
 * abstraindo a necessidade do sistema de conhecer como os formatos s�o lidos ou gravados.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see CFormat
 * @see CJPEGFormat
 * @see CGIFFormat
 * @see CTIFFFormat
 * @see CPNGFormat
 * @see CImage
 *
 */

public class CFormatFactory {
	
	/** Membro publico est�tico e final utilizado para enumerar os tipos de imagens pass�veis de manipula��o em arquivo pela classe. */
	public static enum CFormatEnum { BITMAP, JPEG, GIF, TIFF, PNG };

	/** Membro privado utilizado para armazenar as inst�ncias espec�ficas criadas pelo factory para cada formato suportado pelo sistema. */
	private Map<CFormatEnum, CFormat> m_aFormats; 

	/** Membro privado est�tico utilizado para armazenar uma inst�ncia �nica (singleton) da classe. */
	private static CFormatFactory m_pInstance;

	/**
	 * Construtor da classe CFormatFactory.
	 */
	protected CFormatFactory()
	{
		m_aFormats = new HashMap<CFormatEnum, CFormat>();
	}

	/**
	 * M�todo est�tico utilizado para obter a inst�ncia �nica (singleton) da classe).
	 * 
	 * @return Objeto da classe CFormatFactory com a inst�ncia �nica.
	 */
	public static CFormatFactory getInstance()
	{
		if(m_pInstance == null)
			m_pInstance = new CFormatFactory();
		return m_pInstance;
	}
	
	/**
	 * M�todo b�sico do padr�o Abstract Factory, utilizado para retornar a classe de formato espec�fica para
	 * o formato indicado. Utiliza o polimorfismo da Orienta��o a Objetos, retornando o objeto concreto criado
	 * como a classe b�sica CFormat.
	 * 
	 * @param eFormat Enumera��o indicativa do formato a ser obtido.
	 * @return Inst�ncia da classe de formato concreta, abstraida pela classe CFormat.
	 */
	public CFormat getFormat(CFormatEnum eFormat)
	{
		CFormat pRet = m_aFormats.get(eFormat);
		
		if(pRet == null)
		{
			if(eFormat == CFormatEnum.BITMAP)
				pRet = new CBMPFormat();
			else if(eFormat == CFormatEnum.JPEG)
				pRet = new CJPEGFormat();
			else if(eFormat == CFormatEnum.GIF)
				pRet = new CGIFFormat();
			else if(eFormat == CFormatEnum.TIFF)
				pRet = new CTIFFFormat();
			else if(eFormat == CFormatEnum.PNG)
				pRet = new CPNGFormat();
			else
				return null;
			
			m_aFormats.put(eFormat, pRet);
		}
		
		return pRet;
	}
}