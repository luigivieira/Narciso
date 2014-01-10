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
 
package GUI.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.*;
import GUI.*;

/**
 * Implementa as configurações de acessibilidade (nome em menus, ícones em botões de barra de ferramentas, atalhos no
 * teclado, etc) e a execução, para a ação de remoção de imagens da memória (botão deletar).
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

@SuppressWarnings("serial")
public class CImageCloseAction extends AbstractAction
{
	/**
	 * Construtor da classe.
	 */
	public CImageCloseAction()
	{
		super("Imagens Disponíveis");
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/Delete16.gif")));
		putValue(Action.SHORT_DESCRIPTION, "Elimina da memoria as imagens selecionadas na janela de miniaturas");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, InputEvent.CTRL_MASK));
	}
	
	/**
	 * Captura o evento de execução da ação (clique em menu ou botão, pressionamento de tecla de atalho, etc).
	 * @param e Objeto ActionEvent com o evento ocorrido.
	 */
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			CThumbnailWindow m_ThumbnailWindow = CWindowManager.getThumbnailWindow() ;
			m_ThumbnailWindow.removeMarkedThumbs();
			
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}
	}
}