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
 
package GUI.actions;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import core.images.*;
import GUI.*;

/**
 * Implementa as configura��es de acessibilidade (nome em menus, �cones em bot�es de barra de ferramentas, atalhos no
 * teclado, etc) e a execu��o, para a a��o de exibi��o da janela de compara��o de imagens (menu Ferramentas-Comparar Imagens).
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

@SuppressWarnings("serial")
public class CToolCompareImagesAction extends AbstractAction
{
	/**
	 * Construtor da classe.
	 */
	public CToolCompareImagesAction()
	{
		super("Comparar Imagens");
		putValue(Action.SHORT_DESCRIPTION, "Exibe uma janela para compara��o das duas imagens selecionadas");
	}
	
	/**
	 * Captura o evento de execu��o da a��o (clique em menu ou bot�o, pressionamento de tecla de atalho, etc).
	 * @param e Objeto ActionEvent com o evento ocorrido.
	 */
	public void actionPerformed(ActionEvent e)
	{		
		CThumbnailWindow pWindow = CWindowManager.getThumbnailWindow();

		Vector<CThumbnail> vThumbs = pWindow.getMarkedThumbs();
		if(vThumbs.size() != 2)
		{
			JOptionPane.showMessageDialog(null, "A compara��o s� pode ser efetuada entre duas imagens", "Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		CImage pLImage = vThumbs.get(0).getImage();
		CImage pRImage = vThumbs.get(1).getImage();
		
		CWindowManager.getMainWindow().newCompareWindow(pLImage, pRImage);
	}
}
