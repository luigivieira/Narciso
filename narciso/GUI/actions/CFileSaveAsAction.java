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

import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import core.config.*;
import core.images.*;
import GUI.*;
import GUI.filechoosers.*;

/**
 * Implementa as configurações de acessibilidade (nome em menus, ícones em botões de barra de ferramentas, atalhos no
 * teclado, etc) e a execução, para a ação de gravação de imagem (menu Arquivo-Salvar Como).
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

@SuppressWarnings("serial")
public class CFileSaveAsAction extends AbstractAction
{
	/**
	 * Construtor da classe.
	 */
	public CFileSaveAsAction()
	{
		super("Salvar Como...");
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/SaveAs16.gif")));
		putValue(Action.SHORT_DESCRIPTION, "Salva a imagem atual em um arquivo com um outro nome e/ou formato");
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK));
		setEnabled(false);
	}
	
	/**
	 * Captura o evento de execução da ação (clique em menu ou botão, pressionamento de tecla de atalho, etc).
	 * @param e Objeto ActionEvent com o evento ocorrido.
	 */
	public void actionPerformed(ActionEvent e)
	{
		String sFileType,sFileName = new String();

		CImage Image = ((CImageWindow) CWindowManager.getMainWindow().getDesktopPane().getSelectedFrame()).getImage() ;
		
		sFileName = Image.getCurrentFileName() + ".jpg"; // Já sugere JPG para o formato
		
		try
		{			
			JFileChooser pDlg = new JFileChooser (new File(CConfiguration.getLastLoadPath()));
			pDlg.addChoosableFileFilter(new CImageFilter()) ;
			pDlg.setAcceptAllFileFilterUsed(false);
			pDlg.setFileView(new CImageFileView());			
			pDlg.setSelectedFile(new File(sFileName));
			
			int iRet = pDlg.showSaveDialog(null);
			if(iRet != JFileChooser.APPROVE_OPTION)
			{
				return;
			}
			else
			{
				File fFile = pDlg.getSelectedFile();
				sFileName = fFile.getAbsolutePath();
				sFileType = fFile.getName().substring(fFile.getName().indexOf(".")+1) ;		
				
				CFormatFactory.CFormatEnum eFormat = CUtils.getFormatFactoryEnum(fFile);
				
	        	if(eFormat == null)
	        	{
	        		JOptionPane.showMessageDialog(null, "O formato de arquivo " + sFileType.toUpperCase() + " não é reconhecido pelo Narciso.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
	        		return;
	        	}
	        	else 
				{
					Image.save(sFileName, eFormat) ;	        		
				}				
			}
			
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}

	}
}