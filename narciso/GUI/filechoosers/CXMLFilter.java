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
import javax.swing.filechooser.*;

/**
 * Classe utilizada para a constru��o da janela de sele��o de arquivos do sistema Narciso. Esta classe
 * prov� o filtro do tipo de arquivo XML.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

public class CXMLFilter extends FileFilter
{
	/**
	 * M�todo utilizado para verificar se um arquivo pode ser considerado v�lido se passar no filtro.
	 * Apenas os arquivos que passarem no filtro ser�o exibidos na janela de sele��o.
	 * @param fFile Objeto File com o arquivo a ser verificado.
	 * @return Retorna verdadeiro (true) se o arquivo passar no filtro, ou falso (false) se n�o.
	 */
    public boolean accept(File fFile)
    {
        if (fFile.isDirectory())
            return true;

        String sExt = CUtils.getExtension(fFile);
        if(sExt != null)
        {
            if(sExt.equals(CUtils.XML))
                return true;
            else
                return false;
        }

        return false;
    }

    /**
     * M�todo utilizado para obter a descri��o do filtro, a ser exibido na janela de sele��o de arquivos
     * na lista correspondente.
     */
    public String getDescription()
    {
        return "Arquivos XML (*.xml)";
    }
}