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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import core.images.*;

/**
 * Classe utilizada para implementar a janela de exibição de miniaturas de imagens do sistema.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public class CThumbnailWindow extends JInternalFrame implements KeyListener
{
	/** Membro privado utilizado para armazenar os objetos de miniaturas a serem exibidos. */
	private Vector<CThumbnail> m_vThumbnails;
	
	/** Membro privado utilizado para armazenar a área de exibição das miniaturas dentro da janela. */
	private JPanel m_pArea;
		
	/**
	 * Construtor da classe.
	 */
	public CThumbnailWindow()
	{
		setTitle("Imagens Carregadas");
		setClosable(true);
		setBorder(new TitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		setResizable(true);
		setName("ThumbnailWindow");
		
		addKeyListener(this) ;
		this.setFocusable(true) ;
	
		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		m_pArea = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		m_pArea.setLayout(flowLayout);
		scrollPane.setViewportView(m_pArea);
		
		setBounds(10, 10, 400, 155);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		m_vThumbnails = new Vector<CThumbnail>();
	}

	/**
	 * Método utilizado para adicionar uma miniatura da imagem dada.
	 * @param pImage Objeto CImage com a imagem a ser adicionada como miniatura.
	 */
	public void addImage(CImage pImage)
	{		
		CThumbnail pThumb = new CThumbnail(this, pImage);
		
		m_pArea.add(pThumb);
		pThumb.setVisible(true);
		
		m_vThumbnails.add(pThumb);
		setBounds(getBounds());
	}
	
	/**
	 * Método utilizado para remover da exibição todas as miniaturas selecionadas.
	 */
	public void removeMarkedThumbs()
	{
		Vector<CThumbnail> m_MarkedThumbs = this.getMarkedThumbs();
		for (int i=0; i < m_MarkedThumbs.size(); i++)
		{
			m_vThumbnails.remove(m_MarkedThumbs.get(i));
			m_pArea.remove(m_MarkedThumbs.get(i));
		}
		this.moveToFront();		
	}

	/**
	 * Método utilizado para obter a contagem do número de miniaturas selecionadas na janela de exibição.
	 * @return Número de miniaturas marcadas como selecionadas.
	 */
	public Vector<CThumbnail> getMarkedThumbs()
	{
		Vector<CThumbnail> vRet = new Vector<CThumbnail>();
		int i;
		for(i = 0; i < m_vThumbnails.size(); i++)
		{
			CThumbnail pThumb = m_vThumbnails.get(i);
			if(pThumb.getChecked())
				vRet.add(pThumb);
		}
		return vRet;
	}
	
	/**
	 * Método utilizado para capturar o evento de clique em uma miniatura, de modo a atualizar a exibição
	 * da contagem de imagens marcadas como selecionadas.
	 * @param pThumb Objeto CThumbnail com a miniatura sobre a qual o mouse foi clicado.
	 */
	public void onClickThumbnail(CThumbnail pThumb)
	{        	    
		CMainWindow pWindow = CWindowManager.getMainWindow();
		Vector<CThumbnail> vMarked = getMarkedThumbs();
		if(vMarked.size() > 0)
			pWindow.setLeftMessage("Imagens marcadas: " + vMarked.size());
		else
			pWindow.setLeftMessage("");
	}
	
	/**
	 * Metodo para capturar o evento de duplo clique em uma miniatura, de modo a abrir a imagem original
	 * em uma janela de exibição.  
	 * @param pThumb Objeto CThumbnail com a miniatura sobre a qual o mouse foi clicado.
	 */	
	public void onDblClickThumbnail(CThumbnail pThumb)
	{
		boolean m_bExistsImageWindow = false ;
		
		Vector<CImageWindow> m_aAllInternalWindow = CWindowManager.getOpenedImageWindow();

		for (int i=0; i < m_aAllInternalWindow.size(); i++)
		{
			String m_sWindowName = m_aAllInternalWindow.get(i).getName();
			String m_sImageString = pThumb.getImage().toString();		
	
			try
			{
				if ( m_sWindowName.compareToIgnoreCase(m_sImageString)==0 )
				{
					((JInternalFrame) m_aAllInternalWindow.get(i)).moveToFront();
					((JInternalFrame) m_aAllInternalWindow.get(i)).setSelected(true);	
					m_bExistsImageWindow = true ;								
				}

			}
			catch(Exception ex)
			{
				// Tratar a exception Null Pointer
			}			
		}		
		if (!m_bExistsImageWindow) 
			CWindowManager.getMainWindow().newImageWindow(pThumb.getImage());
		
	}
	
	/**
	 * Método de captura de eventos de pressionamento de teclas. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de teclado, requerida para a captura do evento keyTyped. 
	 */
	public void keyPressed(KeyEvent arg0) {	}

	/**
	 * Método de captura de eventos de liberação de teclas. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de teclado, requerida para a captura do evento keyTyped. 
	 */
	public void keyReleased(KeyEvent arg0) { }

	/**
	 * Método utilizado para capturar o evento de digitação da tecla DELETE, utilizada para
	 * remover a(s) miniatura(s) selecionada(s).
	 * @param Objeto KeyEvent com a tecla pressionada.
	 */
	public void keyTyped(KeyEvent pKeyTyped)
	{
		if (pKeyTyped.getKeyChar() == KeyEvent.VK_DELETE)
			this.removeMarkedThumbs() ;		
	}
	
}
