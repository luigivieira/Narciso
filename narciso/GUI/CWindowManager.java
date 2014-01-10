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
 
package GUI;

import java.awt.Component;
import java.util.Vector;

/**
 * Classe utilizada para gerenciar a criação e acesso às janelas da interface gráfica do sistema Narciso.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

public class CWindowManager
{
	/** Membro privado utilizado para armazenar a referência à janela principal do sistema. */
	private CMainWindow m_pMainWindow = null;
	
	/** Membro privado utilizado para armazenar a referência à janela de miniaturas do sistema. */
	private CThumbnailWindow m_pThumbnailWindow  = null;
	
	/** Membro privado utilizado para armazenar a referência à instância única de CWindowManager (singleton). */
	private static CWindowManager m_pInstance = null;

	/**
	 * Construtor da classe.
	 */
	protected CWindowManager()
	{
	}

	/**
	 * Método protegido e estático para a obtenção da instância única da classe. É utilizado por todos
	 * os demais métodos públicos.
	 * @return Instância única da classe CWindowManager.
	 */
	protected static CWindowManager getInstance()
	{
		if(m_pInstance == null)
			m_pInstance = new CWindowManager();
		return m_pInstance;
	}
	
	/**
	 * Método getter para obtenção da instância da janela principal do sistema.
	 * @return Objeto CMainWindow com a janela principal do sistema.
	 */
	public static CMainWindow getMainWindow()
	{
		return CWindowManager.getInstance().m_pMainWindow;
	}

	/**
	 * Método setter para configuração da janela principal do sistema.
	 * @param pWindow Objeto CMainWindow com a referência à janela principal do sistema.
	 */
	public static void setMainWindow(CMainWindow pWindow)
	{
		CWindowManager.getInstance().m_pMainWindow = pWindow;
	}

	/**
	 * Método getter para obtenção da instância da janela de miniaturas do sistema.
	 * @return Objeto CThumbnailWindow com a janela de miniaturas do sistema.
	 */
	public static CThumbnailWindow getThumbnailWindow()
	{
		return CWindowManager.getInstance().m_pThumbnailWindow;
	}
	
	/**
	 * Método setter para configuração da janela de miniaturas do sistema.
	 * @param pWindow Objeto CThumbnailWindow com a referência à janela de miniaturas do sistema.
	 */
	public static void setThumbnailWindow(CThumbnailWindow pWindow)
	{
		CWindowManager.getInstance().m_pThumbnailWindow = pWindow;
	}
	
	/**
	 * Método getter para obtenção da lista de janelas de exibição de imagem, filhas da janela principal.
	 * @return Vetor de objetos CImageWindow com todas as janelas de imagem abertas na janela principal.
	 */
	public static Vector<CImageWindow> getOpenedImageWindow()
	{
		CMainWindow pWindow = getMainWindow() ; 
		Component m_aAllInternalWindow[] = pWindow.getDesktopPane().getComponents();
		Vector<CImageWindow> pRet= new Vector<CImageWindow>();
		
		for (int i=0; i < m_aAllInternalWindow.length; i++)
		{
			if (m_aAllInternalWindow[i] instanceof CImageWindow)
			{
				pRet.add( (CImageWindow) m_aAllInternalWindow[i] ) ; 
			}
			
		}
		return pRet ;
	}

	/**
	 * Método utilizado para exibir a janela de diálogo de execução de operações ou roteiros.
	 */
	public static void showOperationsDialog()
	{
		COperationDialog pDlg = new COperationDialog();
		pDlg.setVisible(true);
	}
	
	/**
	 * Método utilizado para exibir a janela de diálogo de exibição de propriedades de imagem.
	 */
	public static void showPropertiesDialog(CThumbnail pThumb)
	{
		CPropertiesDialog pDlg = new CPropertiesDialog(pThumb.getImage());
		pDlg.setVisible(true);
	}
	
	/**
	 * Método utilizado para exibir a janela de diálogo de informações sobre o sistema Narciso.
	 */
	public static void showAboutDialog()
	{
		CAboutWindow pDlg = new CAboutWindow();
		pDlg.setVisible(true);
	}
}