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

import java.awt.image.renderable.*;
import java.io.*;
import core.errors.*;
import com.sun.media.jai.codec.*;
import javax.media.jai.*;

/**
 * CBMPFormat � a classe que implementa a manipula��o de arquivos de imagens (carregamento e grava��o)
 * no formato BitMap.
 * 
 * � herdada da classe CFormat.
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

public class CBMPFormat extends CFormat
{
	/**
	 * M�todo sobrescrito da classe pai CFormat para implementar o carregamento do arquivo de imagem de nome dado e no formato Bitmap.
	 * Se o arquivo informado n�o for Bitmap, um erro ser� gerado e o retorno do m�todo ser� null.
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
			pImage = JAI.create("BMP", pBlock);
		} 
		catch(Exception e)
		{
			return null;
		}
		
		return pImage;
	}

	/**
	 * M�todo sobrescrito da classe pai CFormat para implementar a grava��o do arquivo de imagem com o nome dado, no formato Bitmap.
	 *
	 * @param pImage Objeto PlanarImage (JAI) contendo a imagem a ser gravada.
	 * @param sFile Nome do arquivo de imagem a ser gravado.
	 * @return Retorna CErrors.SUCCESS se a grava��o foi realizada com sucesso, ou um c�digo com o erro ocorrido.
	 */
	@Override
	public int save(PlanarImage pImage, String sFile)
	{
		ParameterBlock pPB = new ParameterBlock() ;
		pPB.addSource(pImage) ;
		pPB.add(sFile) ;
		pPB.add("bmp") ;
		pPB.add(new BMPEncodeParam());
		JAI.create("filestore",pPB);
		return CErrors.SUCCESS;
	}
}
