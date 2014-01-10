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
 
package core.images;

import java.awt.image.renderable.ParameterBlock;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JOptionPane;

import com.sun.media.jai.codec.SeekableStream;

import core.errors.CErrors;

/**
 * CGIFFormat é a classe que implementa a manipulação de arquivos de imagens (carregamento e gravação)
 * no formato GIF.
 * 
 * É herdada da classe CFormat.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 * @see CFormat
 * @see CBMPFormat
 * @see CJPEGFormat
 * @see CTIFFFormat
 * @see CImage
 *
 */

public class CGIFFormat extends CFormat
{
	/**
	 * Método sobrescrito da classe pai CFormat para implementar o carregamento do arquivo de imagem de nome dado e no formato GIF.
	 * Se o arquivo informado não for GIF, um erro será gerado e o retorno do método será null.
	 * 
	 * @param sFile Nome do arquivo de imagem a ser carregado.
	 * @return Retorna um objeto PlanarImage (JAI) se a imagem for carregada com sucesso, ou null se ocorrer um erro no carregamento.
	 */
	@Override
	public PlanarImage load(String sFile)
	{
		PlanarImage pImage;
		try
		{
			InputStream pInput = new FileInputStream(sFile);
			SeekableStream pStream = SeekableStream.wrapInputStream(pInput, false);
			ParameterBlock pBlock = new ParameterBlock();
			pBlock.add(pStream);
			pImage = JAI.create("GIF", pBlock);
		} 
		catch(Exception e)
		{
			return null;
		}
		
		return pImage;
	}

	/**
	 * Método sobrescrito da classe pai CFormat para implementar a gravação do arquivo de imagem com o nome dado, no formato GIF.
	 * 
	 * <b>ATENÇÃO: Devido ao fato de que o formato GIF não é um formato aberto, e sim protegido por direitos de utilização,
	 * o sistema Narciso não implementa a gravação de imagens nesse formato. Mesmo assim o método foi implementado seguindo
	 * a interface padrão definida em CFormat de modo a manter a padronização. Se executado, esse método exibe uma janela de 
	 * diálogo informado que o formato GIF não é suportado para gravação.</b>
	 *
	 * @param pImage Objeto PlanarImage (JAI) contendo a imagem a ser gravada.
	 * @param sFile Nome do arquivo de imagem a ser gravado.
	 * @return Sempre retorna CErrors.ERROR_INVALID_TYPE.
	 */
	@Override
	public int save(PlanarImage pImage, String sFile)
	{
		JOptionPane.showMessageDialog(null,"Estão versão do Narciso ainda suporta salvar imagens GIF.\nDesculpe o transtorno.");
		return CErrors.ERROR_INVALID_TYPE;
	}

}
