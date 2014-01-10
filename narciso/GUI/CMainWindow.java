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

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.border.*;

import core.config.*;
import core.images.*;
import core.operations.*;
import GUI.actions.*;

/**
 * CMainWindows é a classe que define a janela principal do sistema Narciso.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

public class CMainWindow
{
	/** Membro privado que armazena a action para a execução do carregamento de arquivos de imagens. */
	private Action acFileOpen = new CFileLoadAction();

	/** Membro privado que armazena a action para a execução da gravação de arquivos de imagens com um nome específico. */
	private Action acFileSaveAs = new CFileSaveAsAction();
	
	/** Membro privado que armazena a action para a Exportação de propriedades para arquivos especificos. */
	private Action acFileExport = new CFileExportAction();

	/** Membro privado que armazena a action para a execução do encerramento do sistema. */
	private Action acFileExit = new CFileExitAction();
	
	/** Membro privado que armazena a action para a execução do fechamento de uma imagem **/ 
	private Action acImageClose = new CImageCloseAction();
	
	/** Membro privado que armazena a action para a exibição da janela de imagens carregadas. */
	private Action acShowThumbs = new CShowThumbnailsAction();

	/** Membro privado que armazena a action para a exibição da janela de propriedades da imagem selecionada na janela de thumbnails. */
	private Action acShowProps = new CShowPropertiesAction();
	
	/** Membro privado que armazena a action para a execução do diálogo de operações. */
	private Action acRunOperations = new CRunOperationsAction();
	
	/** Membro privado que armazena a action para a exibição de janela de comparação de imagens. */
	private Action acToolCompareImages = new CToolCompareImagesAction();
	
	/** Membro privado que armazena a action para a exibição da janela de informações do programa. */
	private Action acHelpAboutWindow = new CHelpAboutAction();

	/** Membro privado que armazena o texto à direita da barra de status da janela. Sua utilização
	 * é planejada para versões futuras.
	 */
	private JLabel m_lbRightMessage;
	
	/** Membro privado que armazena o texto à esquerda da barra de status da janela. Sua utilização
	 * é planejada para versões futuras.
	 */
	private JLabel m_lbLeftMessage;
	
	/** Membro privado que armazena o frame da janela principal do sistema, onde todos os demais
	 *  objetos são posicionados.
	 */
	private JFrame pFrame;

	/** Membro privado que armazena a janela de miniaturas do sistema (CThumbnailWindow). */
	private CThumbnailWindow m_pThumbnailWindow;
	
	/** Membro privado que armazena o desktop panel da janela principal do sistema, onde todos as janelas
	 *  filhas são mantidas.
	 */
	private JDesktopPane m_pDesktopPane;
	
	/**
	 * Construtor da classe.
	 */
	public CMainWindow()
	{
		initialize();

		CConfiguration.load("configuration.xml");
		COperationFactory.registerOperationsFromFile("operations.xml");
		
		m_pThumbnailWindow = new CThumbnailWindow();
		CWindowManager.setThumbnailWindow(m_pThumbnailWindow);
		m_pDesktopPane.add(m_pThumbnailWindow);
		showThumbnailWindow();
	}

	/**
	 * Método utilizado para exibir ou trazer para a frente a janela de miniaturas do sistema.
	 */
	public void showThumbnailWindow()
	{
		if(m_pThumbnailWindow.isVisible())
		{
			m_pThumbnailWindow.moveToFront() ;
			
			try
			{
				m_pThumbnailWindow.setSelected(true) ;
			}
			catch(Exception e)
			{
				// TODO Auto-generated catch block
			}
		}		
		else
			m_pThumbnailWindow.setVisible(true);			
	}

	/**
	 * Método getter utilizado para obter a área de contenção das janelas filhas do sistema.
	 * 
	 * @return Objeto JDesktopPanel com o desktop panel de contenção das janelas filhas.
	 */
	public JDesktopPane getDesktopPane()
	{
		return m_pDesktopPane;
	}
	
	/**
	 * Método utilizado para iniciar a janela durante o carregamento do sistema.
	 *
	 */
	public void start()
	{
		pFrame.setVisible(true);
	}
	
	/**
	 * Método utilizado para encerrar a janela principal, e consequentemente o sistema.
	 */
	public void close()
	{
		pFrame.dispose();
	}

	/**
	 * Método utilizado para criar uma nova janela filha para a apresentação de imagens.
	 * 
	 * @param pImage Objeto CImage com a imagem a ser apresentada.
	 */
	public void newImageWindow(CImage pImage)
	{
    	CImageWindow pChild = new CImageWindow(pImage);
    	m_pDesktopPane.add(pChild);
    	pChild.setVisible(true);
    	pChild.setName(pImage.toString());
	}
	
	/**
	 * Método utilizado para criar uma nova janela filha para a comparação de imagens.
	 * @param pLImage Objeto CImage com a imagem a ser comparada à esquerda.
	 * @param pRImage Objeto CImage com a imagem a ser comparada à direita.
	 */
	public void newCompareWindow(CImage pLImage, CImage pRImage)
	{
		CCompareWindow pChild = new CCompareWindow(pLImage, pRImage);
		m_pDesktopPane.add(pChild);
		pChild.setVisible(true);
	}
	
	/** Método setter utilizado para alterar o texto à esquerda da barra de status da janela. Sua utilização
	 * é planejada para versões futuras.
	 * @param sMessage Texto com a nova mensagem.
	 */	
	public void setLeftMessage(String sMessage)
	{
		m_lbLeftMessage.setText(sMessage);
	}

	/** Método setter utilizado para alterar o texto à direita da barra de status da janela. Sua utilização
	 * é planejada para versões futuras.
	 * @param sMessage Texto com a nova mensagem.
	 */	
	public void setRightMessage(String sMessage)
	{
		m_lbRightMessage.setText(sMessage);
	}
	
	/**
	 * Método de utilização interna (privado) para a inicialização dos componentes do conteúdo da janela principal.
	 */
	private void initialize()
	{
		pFrame = new JFrame();
		pFrame.setTitle("Narciso - Ambiente de Suporte ao Processamento de Imagens para Visão Computacional");
		pFrame.setName("fMain");
		pFrame.setBounds(100, 100, 595, 431);
		pFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		pFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	
		pFrame.addWindowListener(
	        new WindowAdapter()
	        {
	            public void windowClosing(WindowEvent e)
	            {
	            	acFileExit.actionPerformed(null);
	            }
	            
	            public void windowClosed (WindowEvent e)
	            {
	            }
	        }
		);
		
		final JMenuBar menuBar = new JMenuBar();
		menuBar.setName("mMenuBar");
		pFrame.setJMenuBar(menuBar);

		final JMenu arquivoMenu = new JMenu();
		arquivoMenu.setText("Arquivo");
		menuBar.add(arquivoMenu);

		final JMenuItem mnuOpen = new JMenuItem();
		mnuOpen.setAction(acFileOpen);
		arquivoMenu.add(mnuOpen);

		final JMenuItem mnuSaveAs = new JMenuItem();
		mnuSaveAs.setAction(acFileSaveAs);
		arquivoMenu.add(mnuSaveAs);

		arquivoMenu.addSeparator();
		
		final JMenuItem mnuExport = new JMenuItem();
		mnuExport.setAction(acFileExport);
		arquivoMenu.add(mnuExport);
		
		arquivoMenu.addSeparator();

		final JMenuItem menuItem = new JMenuItem();
		menuItem.setAction(acFileExit);
		arquivoMenu.add(menuItem);

		final JMenu exibirMenu = new JMenu();
		exibirMenu.setText("Exibir");
		menuBar.add(exibirMenu);

		final JMenuItem menuShowThumbs = new JMenuItem();
		menuShowThumbs.setAction(acShowThumbs);
		exibirMenu.add(menuShowThumbs);

		final JMenuItem menuShowProps = new JMenuItem();
		menuShowProps.setAction(acShowProps);
		exibirMenu.add(menuShowProps);
		
		final JMenu ferramentasMenu = new JMenu();
		ferramentasMenu.setText("Ferramentas");
		menuBar.add(ferramentasMenu);

		final JMenuItem compararMenuItem = new JMenuItem();
		compararMenuItem.setAction(acToolCompareImages);
		ferramentasMenu.add(compararMenuItem);

		ferramentasMenu.addSeparator();

		final JMenu operacoesMenu = new JMenu();
		operacoesMenu.setText("Operações");
		ferramentasMenu.add(operacoesMenu);

		final JMenuItem runMenu = new JMenuItem();
		runMenu.setAction(acRunOperations);
		operacoesMenu.add(runMenu);
		
		final JMenu ajudaMenu = new JMenu();
		ajudaMenu.setText("Ajuda");
		menuBar.add(ajudaMenu);
		
		final JMenuItem aboutMenu = new JMenuItem();
		aboutMenu.setAction(acHelpAboutWindow);
		ajudaMenu.add(aboutMenu);
		
		final JPanel pnStatusBar = new JPanel();
		pnStatusBar.setFocusable(false);
		pnStatusBar.setBorder(new LineBorder(Color.black, 1, false));
		pnStatusBar.setLayout(new BorderLayout());
		pnStatusBar.setPreferredSize(new Dimension(0, 20));
		pFrame.getContentPane().add(pnStatusBar, BorderLayout.SOUTH);

		m_lbRightMessage = new JLabel();
		m_lbRightMessage.setFocusable(false);
		m_lbRightMessage.setMinimumSize(new Dimension(200, 0));
		m_lbRightMessage.setPreferredSize(new Dimension(0, 0));
		pnStatusBar.add(m_lbRightMessage, BorderLayout.EAST);

		final JPanel pnProgressPanel = new JPanel();
		pnProgressPanel.setFocusable(false);
		pnProgressPanel.setPreferredSize(new Dimension(200, 0));
		pnProgressPanel.setLayout(new BorderLayout());
		pnStatusBar.add(pnProgressPanel, BorderLayout.WEST);
		pnProgressPanel.setVisible(false);

		final JButton button = new JButton();
		button.setPreferredSize(new Dimension(20, 0));
		pnProgressPanel.add(button, BorderLayout.EAST);

		final JProgressBar pbProgressBar = new JProgressBar();
		pbProgressBar.setFocusable(false);
		pbProgressBar.setValue(20);
		pnProgressPanel.add(pbProgressBar, BorderLayout.CENTER);

		m_lbLeftMessage = new JLabel();
		m_lbLeftMessage.setFocusable(false);
		m_lbLeftMessage.setMinimumSize(new Dimension(200, 0));
		pnStatusBar.add(m_lbLeftMessage);

		m_pDesktopPane = new JDesktopPane();
		m_pDesktopPane.setBackground(new Color(239, 239, 239));
		pFrame.getContentPane().add(m_pDesktopPane, BorderLayout.CENTER);

		final JPanel pToolbarArea = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		pToolbarArea.setLayout(flowLayout);
		pFrame.getContentPane().add(pToolbarArea, BorderLayout.NORTH);

		final JToolBar pFileToolbar = new JToolBar();
		pToolbarArea.add(pFileToolbar);
		pFileToolbar.setName("Arquivo");

		final JButton btnOpen = new JButton();
		btnOpen.setAction(acFileOpen);
		btnOpen.setText("");
		pFileToolbar.add(btnOpen);

		final JButton btnSaveAs = new JButton();
		btnSaveAs.setAction(acFileSaveAs);
		btnSaveAs.setText("");
		pFileToolbar.add(btnSaveAs);

		pFileToolbar.addSeparator();

		final JButton btnDelete = new JButton();
		btnDelete.setAction(acImageClose);
		btnDelete.setText("");
		pFileToolbar.add(btnDelete);

		final JToolBar pShowToolbar = new JToolBar();
		pToolbarArea.add(pShowToolbar);
		pShowToolbar.setName("Exibir");

		final JButton btnShowThumbs = new JButton();
		pShowToolbar.add(btnShowThumbs);
		btnShowThumbs.setAction(acShowThumbs);
		btnShowThumbs.setText("");

		final JButton btnShowProps = new JButton();
		btnShowProps.setAction(acShowProps);
		btnShowProps.setText("");
		pShowToolbar.add(btnShowProps);
		
		final JToolBar pHelpToolbar = new JToolBar();
		pToolbarArea.add(pHelpToolbar);
		pHelpToolbar.setName("Ajuda");

		final JButton btnHelpAbout = new JButton();
		pHelpToolbar.add(btnHelpAbout);
		btnHelpAbout.setAction(acHelpAboutWindow);
		btnHelpAbout.setText("");
	}

	/**
	 * Método utilizado para forçar uma reavaliação dos objetos da janela principal quando eventos são
	 * ocorridos. É utilizado, por exemplo, para habilitar ou desabilitar botões quando uma imagem é selecionada
	 * na janela de miniaturas.
	 */
	public void verifyEvents()
	{
		boolean bChildOpened, bChildGenerated, bCompareAllowed, bOperationAllowed, bProperties;
		
		JInternalFrame pChild = m_pDesktopPane.getSelectedFrame();
		bChildOpened = (pChild instanceof CImageWindow);

		if(bChildOpened)
		{
			CImageWindow pWnd = (CImageWindow) pChild;
			String sName = pWnd.getImage().getCurrentFileName();
			bChildGenerated = sName.indexOf("@") != -1;
		}
		else
			bChildGenerated = false;
		
		Vector<CThumbnail> vThumbs = m_pThumbnailWindow.getMarkedThumbs();
		bCompareAllowed = ((vThumbs != null) && (vThumbs.size() == 2));
		bOperationAllowed = ((vThumbs != null) && (vThumbs.size() > 0));
		bProperties = ((vThumbs != null) && (vThumbs.size() == 1));
		
		acFileSaveAs.setEnabled(bChildGenerated);

		
		acFileExport.setEnabled(bOperationAllowed);
		acToolCompareImages.setEnabled(bCompareAllowed);
		acRunOperations.setEnabled(bOperationAllowed);
		
		acShowProps.setEnabled(bProperties);
	}
}