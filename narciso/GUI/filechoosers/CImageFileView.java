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
 
package GUI.filechoosers;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/**
 * Classe utilizada para a construção da janela de seleção de arquivos do sistema Narciso. Esta classe
 * provê informações sobre os arquivos durante sua exibição na janela de seleção.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

public class CImageFileView extends FileView
{
	/** Membro provado utilizado para armazenar o ícone representativo de um arquivo de imagem no formato JPEG. */
    ImageIcon pJpgIcon = CUtils.createImageIcon("/GUI/images/jpgIcon.gif");
    
    /** Membro provado utilizado para armazenar o ícone representativo de um arquivo de imagem no formato GIF. */
    ImageIcon pGifIcon = CUtils.createImageIcon("/GUI/images/gifIcon.gif");
    
    /** Membro provado utilizado para armazenar o ícone representativo de um arquivo de imagem no formato TIFF. */    
    ImageIcon pTiffIcon = CUtils.createImageIcon("/GUI/images/tiffIcon.gif");
    
    /** Membro provado utilizado para armazenar o ícone representativo de um arquivo de imagem no formato PNG. */
    ImageIcon pPngIcon = CUtils.createImageIcon("/GUI/images/pngIcon.gif");
    
    /** Membro provado utilizado para armazenar o ícone representativo de um arquivo de imagem no formato BMP. */
    ImageIcon pBmpIcon = CUtils.createImageIcon("/GUI/images/bmpIcon.gif");

    /**
     * Método getter utilizado para obter o nome do arquivo.
     * @param fFile Objeto fFile com o arquivo a ser verificado.
     * @return Texto com o nome do arquivo ou null para manter o nome original.
     */
    public String getName(File fFile)
    {
        return null;
    }

    /**
     * Método getter utilizado para obter a descrição do arquivo.
     * @param fFile Objeto fFile com o arquivo a ser verificado.
     * @return Texto com a descrição do arquivo ou null para não conter descrção.
     */
    public String getDescription(File fFile)
    {
        return null;
    }

    /**
     * Método getter utilizado para obter a indicação se o arquivo é um link para outro arquivo.
     * @param fFile Objeto fFile com o arquivo a ser verificado.
     * @return Indicação lógica (true ou false) se o arquivo é um link ou não, ou null para não conter indicação.
     */
    public Boolean isTraversable(File fFile)
    {
        return null;
    }

    /**
     * Método getter utilizado para obter a descrição do tipo do arquivo.
     * @param fFile Objeto fFile com o arquivo a ser verificado.
     * @return Texto com a descrição do tipo do arquivo.
     */
    public String getTypeDescription(File fFile)
    {
        String sExt = CUtils.getExtension(fFile);
        String sType = null;

        if(sExt != null)
        {
            if(sExt.equals(CUtils.JPEG) || sExt.equals(CUtils.JPG))
                sType = "Imagem JPEG";
            else if(sExt.equals(CUtils.GIF))
                sType = "Imagem GIF";
            else if(sExt.equals(CUtils.TIFF) || sExt.equals(CUtils.TIF))
                sType = "Imagem TIFF";
            else if(sExt.equals(CUtils.PNG))
                sType = "Imagem PNG";
            else if(sExt.equals(CUtils.BMP))
            	sType = "Imagem Bitmap";
        }
        return sType;
    }

    /**
     * Método getter utilizado para obter o ícone do arquivo.
     * @param fFile Objeto fFile com o arquivo a ser verificado.
     * @return Objeto Icon com o ícone do arquivo a ser apresentado na janela de seleção.
     */
    public Icon getIcon(File fFile)
    {
        String sExt = CUtils.getExtension(fFile);
        Icon pIcon = null;

        if (sExt != null)
        {
            if(sExt.equals(CUtils.JPEG) || sExt.equals(CUtils.JPG))
                pIcon = pJpgIcon;
            else if(sExt.equals(CUtils.GIF))
            	pIcon = pGifIcon;
            else if(sExt.equals(CUtils.TIFF) || sExt.equals(CUtils.TIF))
            	pIcon = pTiffIcon;
            else if(sExt.equals(CUtils.PNG))
            	pIcon = pPngIcon;
            else if(sExt.equals(CUtils.BMP))
            	pIcon = pBmpIcon;
        }
        return pIcon;
    }
}