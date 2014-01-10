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
 
package GUI.filechoosers;

import java.io.File;
import javax.swing.ImageIcon;

import core.images.CFormatFactory;

/**
 * Classe utilizara para prover m�todos de utiliza��o geral.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

public class CUtils
{
	/** Membro p�blico est�tico utilizado para definir o nome da extens�o padr�o para arquivos de imagem no formato JPEG. */
	public final static String JPEG = "jpeg";
	
	/** Membro p�blico est�tico utilizado para definir o nome da extens�o padr�o para arquivos de imagem no formato JPEG. */
    public final static String JPG = "jpg";
    
    /** Membro p�blico est�tico utilizado para definir o nome da extens�o padr�o para arquivos de imagem no formato GIF. */
    public final static String GIF = "gif";
    
    /** Membro p�blico est�tico utilizado para definir o nome da extens�o padr�o para arquivos de imagem no formato TIFF. */
    public final static String TIFF = "tiff";
    
    /** Membro p�blico est�tico utilizado para definir o nome da extens�o padr�o para arquivos de imagem no formato TIFF. */
    public final static String TIF = "tif";
    
    /** Membro p�blico est�tico utilizado para definir o nome da extens�o padr�o para arquivos de imagem no formato PNG. */
    public final static String PNG = "png";
    
    /** Membro p�blico est�tico utilizado para definir o nome da extens�o padr�o para arquivos de imagem no formato BMP. */
    public final static String BMP = "bmp";
    
    /** Membro p�blico est�tico utilizado para definir o nome da extens�o padr�o para arquivos XML. */
    public final static String XML = "xml";

    /** Membro p�blico est�tico utilizado para definir o nome da extens�o padr�o para arquivos Excel. */
    public final static String XLS = "xls";
    
    /** Membro p�blico est�tico utilizado para definir o nome da extens�o padr�o para arquivos no formato CSV. */
    public final static String CSV = "csv";

    /**
     * M�todo getter utilizado para obter a extens�o de um arquivo dado.
     * 
     * @param fFile Objeto File com o arquivo de onde a extens�o deve ser extra�da.
     * @return Texto da extens�o do arquivo.
     */
    public static String getExtension(File fFile)
    {
        String sExt = null;
        String sName = fFile.getName();
        int iPos = sName.lastIndexOf('.');

        if (iPos > 0 &&  iPos < sName.length() - 1)
            sExt = sName.substring(iPos+1).toLowerCase();
        
        return sExt;
    }
   
    /**
     * M�todo utilizado para obter o formato de um arquivo de imagem de acordo com sua extens�o.
     * @param fFile Objeto File com o arquivo a ser verificado.
     * @return Formato (conforme defini��o em CFormatFactory.CFormatEnum) do arquivo.
     */
    public static CFormatFactory.CFormatEnum getFormatFactoryEnum(File fFile)
    {
    	String sExt = getExtension(fFile);
    	if(sExt.equals(BMP))
    		return CFormatFactory.CFormatEnum.BITMAP;
    	else if(sExt.equals(GIF))
    		return CFormatFactory.CFormatEnum.GIF;
    	else if(sExt.equals(JPEG) || sExt.equals(JPG))
    		return CFormatFactory.CFormatEnum.JPEG;
    	else if(sExt.equals(TIFF) || sExt.equals(TIF))
    		return CFormatFactory.CFormatEnum.TIFF;
    	else if(sExt.equals(PNG))
    		return CFormatFactory.CFormatEnum.PNG;
    	else
    		return null;
    }
    
    /**
     * M�todo utilizado para obter o objeto ImageIcon para o arquivo especificado.
     * 
     * @param sFile Nome do arquivo da imagem para obten��o do objeto ImageIcon.
     * @return Retorna o objeto imagem icon se o caminho for v�lido, ou null se inv�lido.
     */
    protected static ImageIcon createImageIcon(String sFile)
    {
    	ImageIcon pRet;
		try
		{
			pRet = new ImageIcon(Class.forName("GUI.Narciso").getResource(sFile));
		}
		catch (ClassNotFoundException e)
		{
			return null;
		}
        if(pRet != null)
            return pRet;
        else
        {
            System.err.println("O arquivo " + sFile + " n�o existe!");
            return null;
        }
    }
}