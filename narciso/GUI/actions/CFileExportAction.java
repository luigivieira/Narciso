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
import java.io.File;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import core.config.CConfiguration;
import core.exporting.CCSVExporter;
import core.exporting.CExcelExporter;

import GUI.CExceptionDialog;
import GUI.CThumbnail;
import GUI.CThumbnailWindow;
import GUI.CWindowManager;
import GUI.filechoosers.CExportFileFilter;

/**
 * Implementa as configurações de acessibilidade (nome em menus, ícones em botões de barra de ferramentas, atalhos no
 * teclado, etc) e a execução, para a ação de exportação de propriedades de imagem (menu Arquivo-Exportar).
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

@SuppressWarnings("serial")
public class CFileExportAction extends AbstractAction
{
	/**
	 * Construtor da classe.
	 */
	public CFileExportAction()
	{
		super("Exportar para...");
		putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/SaveAs16.gif")));
		putValue(Action.SHORT_DESCRIPTION, "Exporta as propridades da imagem em diversos formatos");
		setEnabled(false);
	}
	
	/**
	 * Captura o evento de execução da ação (clique em menu ou botão, pressionamento de tecla de atalho, etc).
	 * @param e Objeto ActionEvent com o evento ocorrido.
	 */
	public void actionPerformed(ActionEvent e)
	{
		String sFileType,sFileName = new String(""); 
				
		CThumbnailWindow pWindow = CWindowManager.getThumbnailWindow();
		Vector<CThumbnail> vThumbs = pWindow.getMarkedThumbs();
		
		if(vThumbs == null || vThumbs.size() != 1)
		{
			JOptionPane.showMessageDialog(null,"Apenas uma imagem pode ter as propriedades exportadas por vez.");
			return;
		}
		
		try
		{			
			JFileChooser pDlg = new JFileChooser (new File(CConfiguration.getLastLoadPath()));
			pDlg.addChoosableFileFilter(new CExportFileFilter());
			
			int iRet = pDlg.showSaveDialog(null);
			if(iRet != JFileChooser.APPROVE_OPTION)
				return;
			else
			{
				File fFile = pDlg.getSelectedFile();
				sFileName = fFile.getAbsolutePath();
				sFileType = fFile.getName().substring(fFile.getName().indexOf(".")+1) ;
			
				if (sFileType.equalsIgnoreCase("xls"))
				{
					CExcelExporter objExport = new CExcelExporter();
					objExport.exportInfo(vThumbs.get(0).getImage(), sFileName);					
				}	
				else if (sFileType.equalsIgnoreCase("csv"))
				{
					CCSVExporter objExport = new CCSVExporter();
					objExport.exportInfo(vThumbs.get(0).getImage(), sFileName);
				}
				else
				{
					JOptionPane.showMessageDialog(null,"Formato não suportado");
				}
			}
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}
	}
}
