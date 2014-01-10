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

import java.io.*;

import javax.swing.*;

import core.config.CConfiguration;

/**
 * Classe utilizada para a construção da janela de seleção de arquivos do sistema Narciso.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

public class CLoadImageFileChooser
{
	/** Membro privado para armazenar a janela original (Java) de seleção de arquivos */
    private JFileChooser pFC;

    /**
     * Construtor da classe.
     */
    public CLoadImageFileChooser()
    {
    	File fLastLoadPath = new File(CConfiguration.getLastLoadPath());
        pFC = new JFileChooser(fLastLoadPath);
        pFC.addChoosableFileFilter(new CImageFilter());
        pFC.setAcceptAllFileFilterUsed(false);
        pFC.setFileView(new CImageFileView());
        pFC.setAccessory(new CImagePreview(pFC));
   }

    /**
     * Método utilizado para exibir a janela de seleção de arquivos. 
     */
    public int showDialog()
    {
    	int iRet = pFC.showDialog(null, "Carregar");
    	if(iRet == JFileChooser.APPROVE_OPTION)
    	{
    		File fFile = getSelectedFile();
    		CConfiguration.setLastLoadPath(fFile.getPath());
    	}
        return iRet;
    }
    
    /**
     * Método getter utilizado para obter o arquivo selecionado pelo usuário.
     * @return Objeto File com o arquivo selecionado pelo usuário.
     */
    public File getSelectedFile()
    {
    	return pFC.getSelectedFile();
    }
}