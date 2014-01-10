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
import core.errors.*;
import core.images.*;
import java.io.*;
import GUI.*;
import GUI.filechoosers.*;

/**
 * Implementa as configurações de acessibilidade (nome em menus, ícones em botões de barra de ferramentas, atalhos no
 * teclado, etc) e a execução, para a ação de carregamento de imagem (menu Arquivo-Carregar).
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

@SuppressWarnings("serial")
public class CFileLoadAction extends AbstractAction
{
	/**
	 * Construtor da classe.
	 */
	public CFileLoadAction()
	{
		super("Carregar...");
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/Open16.gif")));
		putValue(Action.SHORT_DESCRIPTION, "Carrega um arquivo de imagem existente");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
	}
	
	/**
	 * Captura o evento de execução da ação (clique em menu ou botão, pressionamento de tecla de atalho, etc).
	 * @param e Objeto ActionEvent com o evento ocorrido.
	 */
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			CLoadImageFileChooser pDlg = new CLoadImageFileChooser();
			int iRet = pDlg.showDialog();
	        if(iRet == JFileChooser.APPROVE_OPTION)
	        {
	        	File fFile = pDlg.getSelectedFile();
	        	CFormatFactory.CFormatEnum eFormat = CUtils.getFormatFactoryEnum(fFile);
	        	if(eFormat == null)
	        	{
	        		JOptionPane.showMessageDialog(null, "O formato do arquivo " + fFile.getPath() + " não é reconhecido pelo Narciso.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
	        		return;
	        	}
	        	
	        	CImage pImage = new CImage(10, 10, true);
	        	iRet = pImage.load(fFile.getPath(), eFormat);
	        	if(iRet != CErrors.SUCCESS)
	        	{
	        		JOptionPane.showMessageDialog(null, "Ocorreu um erro no carregamento do arquivo " + fFile.getPath() + ".\nMensagem de erro: " + CErrors.getErrorDescription(iRet), "Erro no Carregamento do Arquivo", JOptionPane.ERROR_MESSAGE);
	        		return;
	        	}

	        	CThumbnailWindow pWindow = CWindowManager.getThumbnailWindow();
	        	pWindow.addImage(pImage);
	        }
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}
	}
}