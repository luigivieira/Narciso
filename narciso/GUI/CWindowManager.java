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
 
package GUI;

import java.awt.Component;
import java.util.Vector;

/**
 * Classe utilizada para gerenciar a cria��o e acesso �s janelas da interface gr�fica do sistema Narciso.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

public class CWindowManager
{
	/** Membro privado utilizado para armazenar a refer�ncia � janela principal do sistema. */
	private CMainWindow m_pMainWindow = null;
	
	/** Membro privado utilizado para armazenar a refer�ncia � janela de miniaturas do sistema. */
	private CThumbnailWindow m_pThumbnailWindow  = null;
	
	/** Membro privado utilizado para armazenar a refer�ncia � inst�ncia �nica de CWindowManager (singleton). */
	private static CWindowManager m_pInstance = null;

	/**
	 * Construtor da classe.
	 */
	protected CWindowManager()
	{
	}

	/**
	 * M�todo protegido e est�tico para a obten��o da inst�ncia �nica da classe. � utilizado por todos
	 * os demais m�todos p�blicos.
	 * @return Inst�ncia �nica da classe CWindowManager.
	 */
	protected static CWindowManager getInstance()
	{
		if(m_pInstance == null)
			m_pInstance = new CWindowManager();
		return m_pInstance;
	}
	
	/**
	 * M�todo getter para obten��o da inst�ncia da janela principal do sistema.
	 * @return Objeto CMainWindow com a janela principal do sistema.
	 */
	public static CMainWindow getMainWindow()
	{
		return CWindowManager.getInstance().m_pMainWindow;
	}

	/**
	 * M�todo setter para configura��o da janela principal do sistema.
	 * @param pWindow Objeto CMainWindow com a refer�ncia � janela principal do sistema.
	 */
	public static void setMainWindow(CMainWindow pWindow)
	{
		CWindowManager.getInstance().m_pMainWindow = pWindow;
	}

	/**
	 * M�todo getter para obten��o da inst�ncia da janela de miniaturas do sistema.
	 * @return Objeto CThumbnailWindow com a janela de miniaturas do sistema.
	 */
	public static CThumbnailWindow getThumbnailWindow()
	{
		return CWindowManager.getInstance().m_pThumbnailWindow;
	}
	
	/**
	 * M�todo setter para configura��o da janela de miniaturas do sistema.
	 * @param pWindow Objeto CThumbnailWindow com a refer�ncia � janela de miniaturas do sistema.
	 */
	public static void setThumbnailWindow(CThumbnailWindow pWindow)
	{
		CWindowManager.getInstance().m_pThumbnailWindow = pWindow;
	}
	
	/**
	 * M�todo getter para obten��o da lista de janelas de exibi��o de imagem, filhas da janela principal.
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
	 * M�todo utilizado para exibir a janela de di�logo de execu��o de opera��es ou roteiros.
	 */
	public static void showOperationsDialog()
	{
		COperationDialog pDlg = new COperationDialog();
		pDlg.setVisible(true);
	}
	
	/**
	 * M�todo utilizado para exibir a janela de di�logo de exibi��o de propriedades de imagem.
	 */
	public static void showPropertiesDialog(CThumbnail pThumb)
	{
		CPropertiesDialog pDlg = new CPropertiesDialog(pThumb.getImage());
		pDlg.setVisible(true);
	}
	
	/**
	 * M�todo utilizado para exibir a janela de di�logo de informa��es sobre o sistema Narciso.
	 */
	public static void showAboutDialog()
	{
		CAboutWindow pDlg = new CAboutWindow();
		pDlg.setVisible(true);
	}
}