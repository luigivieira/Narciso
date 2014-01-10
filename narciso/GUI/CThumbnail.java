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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;
import javax.swing.*;

import core.images.*;

/**
 * Classe utilizada para implementar o objeto de exibição de miniaturas de imagens na janela de miniaturas do sistema.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public class CThumbnail extends JPanel implements MouseListener
{
	/** Membro privado utilizado para armazenar a referência à janela de miniaturas. */	
	private CThumbnailWindow m_pThumbnailWindow;
	
	/** Membro privado utilizado para armazenar a imagem a ser miniaturizada. */
	private CImage m_pImage;
	
	/** Membro privado utilizado para armazenar a miniatura da imagem. */
	private ImageIcon m_pThumbnail;
	
	/** Membro privado utilizado para armazenar o estado indicadivo de miniatura selecionada ou não. */
	private boolean m_bChecked;
	
	/**
	 * Construtor da classe.
	 * @param pThumbnailWindow Objeto da janela de miniaturas onde a miniatura será exibida.
	 * @param pImage Objeto CImage com a imagem a ser miniaturizada.
	 */
	public CThumbnail(CThumbnailWindow pThumbnailWindow, CImage pImage)
	{
		m_pThumbnailWindow = pThumbnailWindow;
		m_bChecked = false;
		
		addMouseListener(this);
		
		notifyImageChange(pImage);
		setPreferredSize(new Dimension(100, 100));
		
		setToolTipText("Clique para marcar/desmarcar e duplo clique para abrir");
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	
	/**
	 * Método getter para obtenção da imagem original da miniatura.
	 * @return Objeto CImage com a imagem original.
	 */
	public CImage getImage()
	{
		return m_pImage;
	}
	
	/**
	 * Método público utilizado para notificar a miniatura quando ocorrerem mudanças na imagem original,
	 * de modo que uma nova miniatura possa ser preparada.
	 * @param pImage Novo objeto CImage com a imagem a ser miniaturizada.
	 */
	public void notifyImageChange(CImage pImage)
	{
		try
		{
			m_pImage = pImage;
			BufferedImage pBuffer = m_pImage.getPlanarImage().getAsBufferedImage();
			ImageIcon pIcon = new ImageIcon(pBuffer);
			m_pThumbnail = new ImageIcon(pIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}
	}
	
	/**
	 * Método setter para configurar o estado indicativo de miniatura selecionada ou não. 
	 * @param bValue Valor lógico indicando se a miniatura está selecionada (true) ou não (false).
	 */
	public void setChecked(boolean bValue)
	{
		m_bChecked = bValue;
		repaint();
	}

	/**
	 * Método getter para obtenção do estado indicadivo da seleção da miniatura.
	 * @return Valor lógico indicando se a miniatura está selecionada (true) ou não (false).
	 */
	public boolean getChecked()
	{
		return m_bChecked;
	}
	
	/**
	 * Método utilizado para alternar a seleção da miniatura.
	 */
	public void toggleChecked()
	{
		setChecked(!getChecked());
	}

	/**
	 * Método utilizado para capturar o evento de pintura do objeto e exibir a miniatura da 
	 * imagem original. Em caso de indicação de seleção definida como verdadeiro (true), uma 
	 * borda na cor vermelha também é exibida.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		try
		{
			super.paintComponent(g);
			g.drawImage(m_pThumbnail.getImage(), 0, 0, null);
			if(m_bChecked)
			{
				Graphics2D g2 = (Graphics2D) g;
				Stroke pStroke = new BasicStroke(4);
				g2.setStroke(pStroke);
				g2.setColor(Color.red);
				g2.drawRect(0, 0, getWidth(), getHeight());
			}
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}
	}

	/**
	 * Método utilizado para capturar o evento de clique do mouse, utilizado para alternar a indicação
	 * de seleção da miniatura.
	 * @param Objeto MouseEvent com o evento ocorrido.
	 */
	public void mouseClicked(MouseEvent e)
	{
		try
		{
			switch(e.getClickCount())
			{
				case 1:
					toggleChecked();
					m_pThumbnailWindow.onClickThumbnail(this);
					break;
				case 2:
					m_pThumbnailWindow.onDblClickThumbnail(this);
					break;
			}
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}
	}

	/**
	 * Método de captura de eventos de entrada do mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mouseEntered(MouseEvent e) {}

	/**
	 * Método de captura de eventos de saída do mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mouseExited(MouseEvent e) {}

	/**
	 * Método de captura de eventos de pressionamento do botão do mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mousePressed(MouseEvent e) {}

	/**
	 * Método de captura de eventos de liberação do botão do mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mouseReleased(MouseEvent e)	{}		
	
	/**
	 * Método de captura de eventos de movimentação do mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mouseMoved(MouseEvent e)	{}
	
	/**
	 * Método de captura de eventos de arraste do mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mouseDraged(MouseEvent e)	{}

	/**
	 * Método de captura de eventos de término de arraste do mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mouseDragged(MouseEvent arg0) {}
}