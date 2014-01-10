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
import javax.swing.filechooser.*;

/**
 * Classe utilizada para a construção da janela de seleção de arquivos do sistema Narciso. Esta classe
 * provê o filtro dos tipos de arquivo permitidos na seleção do nome para carregamento e/ou gravação
 * de imagens.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

public class CImageFilter extends FileFilter
{
	/**
	 * Método utilizado para verificar se um arquivo pode ser considerado válido se passar no filtro.
	 * Apenas os arquivos que passarem no filtro serão exibidos na janela de seleção.
	 * @param fFile Objeto File com o arquivo a ser verificado.
	 * @return Retorna verdadeiro (true) se o arquivo passar no filtro, ou falso (false) se não.
	 */
    public boolean accept(File fFile)
    {
        if (fFile.isDirectory())
            return true;

        String sExt = CUtils.getExtension(fFile);
        if (sExt != null) {
            if (sExt.equals(CUtils.TIFF) ||
            	sExt.equals(CUtils.TIF)  ||
            	sExt.equals(CUtils.GIF)  ||
            	sExt.equals(CUtils.JPEG) ||
            	sExt.equals(CUtils.JPG)  ||
            	sExt.equals(CUtils.BMP)  ||
            	sExt.equals(CUtils.PNG))
                    return true;
            else
                return false;
        }

        return false;
    }

    /**
     * Método utilizado para obter a descrição do filtro, a ser exibido na janela de seleção de arquivos
     * na lista correspondente.
     */
    public String getDescription()
    {
        return "Arquivos de Imagens (BMP, JPEG, GIF, TIFF, PNG)";
    }
}