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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.media.jai.PlanarImage;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.images.*;
import core.info.CHistogram;

/**
 * Classe utilizada para implementar a janela de exibição de uma imagem.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */


@SuppressWarnings("serial")
public class CImageWindow extends JInternalFrame implements MouseListener, ChangeListener
{
	/** Membro privado que armazena a imagem apresentada pela janela. */
	private CImage m_pImage;
	
	/** Membro privado que armazena o caminho e nome do arquivo de imagem. */
	private String m_sFullName;
	
	/** Membro privado que armazena apenas o nome do arquivo de imagem. */
	private String m_sShortName;
	
	/** Membro privado que armazena o objeto de exibição da imagem apresentada pela janela. */
	private ImageDisplay m_pDisplay;

	/** Membro privado que armazena os objetos de exibição das imagens de gráfico de histograma da imagem. */
	private ImageDisplay m_aDispHistogram[];
	
	/** Membro privado que armazena o texto do odômetro (coordenadas do pixel sobre mouse) para a imagem. */
	private JLabel m_lblOdometer;

	/** Membro privado que armazena a lupa. */
	private CMagnifier m_pMagnifier;
	
	/** Membro privado que armazena o texto do tamanho da lupa. */
	private JLabel m_pTamLabel;
	
	/** Membro privado que armazena o texto do zoom da lupa. */
	private JLabel m_pZoomLabel;
	
	/** Membro privado que armazena o objeto de alteração do tamanho da lupa. */
	private JSlider m_pSizeSlider;

	/** Membro privado que armazena o objeto de alteração do zoom da lupa. */
	private JSlider m_pZoomSlider;

	/**
	 * Construtor da classe.
	 * @param pImage Objeto CImage com a imagem a ser apresentada.
	 */
	public CImageWindow(CImage pImage)
	{
		super("", true, true, true, true);
		setAutoscrolls(true);
		setResizable(true);
		setLayer(0) ;
		m_pImage = pImage;
		setNames(m_pImage.getCurrentFileName());
		
		setFocusCycleRoot(true);
	
		setTitle(m_sShortName);
		setBounds(100, 100, 500, 375);

		JScrollPane pScroll = new JScrollPane();
		getContentPane().add(pScroll, BorderLayout.CENTER);

		final JPanel pImageArea = new JPanel();
		pImageArea.setLayout(new BorderLayout());
		pScroll.setViewportView(pImageArea);
		
    	m_pDisplay = new ImageDisplay(pImage.getPlanarImage());
    	pImageArea.add(m_pDisplay, BorderLayout.CENTER);

		CHistogram pHist = new CHistogram(256, m_pImage);		
		m_aDispHistogram = new ImageDisplay[pHist.getNumBands()];
		for(int iBand = 0; iBand < pHist.getNumBands(); iBand++)
		{
			PlanarImage pTemp = pHist.createHistogramImage(iBand, 89, 30, null, false).getPlanarImage();
			m_aDispHistogram[iBand] = new ImageDisplay(pTemp);
			m_pDisplay.add(m_aDispHistogram[iBand]);
			m_aDispHistogram[iBand].setBounds(5, 5 + (iBand * pTemp.getHeight() + 5), pTemp.getWidth(), pTemp.getHeight());
			m_aDispHistogram[iBand].setVisible(false);
		}
    	
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setBorder(new LineBorder(Color.black, 1, false));
		panel.setPreferredSize(new Dimension(0, 18));

		final JPanel pLeftPanel = new JPanel();
		pLeftPanel.setPreferredSize(new Dimension(240, 0));
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(2);
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		pLeftPanel.setLayout(flowLayout);
		panel.add(pLeftPanel, BorderLayout.WEST);

		final JToggleButton btHist = new JToggleButton();
		btHist.setPreferredSize(new Dimension(16, 16));
		pLeftPanel.add(btHist);
		btHist.setIcon(new ImageIcon(getClass().getResource("/GUI/images/Histogram16.gif")));
		btHist.setToolTipText("Exibir/Esconder Histograma");
		btHist.setName("hist");
		btHist.addMouseListener(this);
		
		final JToggleButton btZoom = new JToggleButton();
		pLeftPanel.add(btZoom);
		btZoom.setIcon(new ImageIcon(getClass().getResource("/GUI/images/Zoom16.gif")));
		btZoom.setToolTipText("Exibir/Esconder Lupa");
		btZoom.setName("toggle");
		btZoom.addMouseListener(this);
		btZoom.setPreferredSize(new Dimension(16, 16));

		m_pTamLabel = new JLabel();
		m_pTamLabel.setText("Tam.:");
		pLeftPanel.add(m_pTamLabel);
		m_pTamLabel.setEnabled(false);

		m_pSizeSlider = new JSlider();
		m_pSizeSlider.setName("size");
		m_pSizeSlider.setMaximum(200);
		m_pSizeSlider.setMinimum(50);
		m_pSizeSlider.setPreferredSize(new Dimension(60, 10));
		pLeftPanel.add(m_pSizeSlider);
		m_pSizeSlider.addChangeListener(this);
		m_pSizeSlider.setEnabled(false);

		m_pZoomLabel = new JLabel();
		m_pZoomLabel.setText("Zoom:");
		pLeftPanel.add(m_pZoomLabel);
		m_pZoomLabel.setEnabled(false);

		m_pZoomSlider = new JSlider();
		m_pZoomSlider.setMaximum(20);
		m_pZoomSlider.setValue(2);
		m_pZoomSlider.setMinimum(2);
		m_pZoomSlider.setName("zoom");
		m_pZoomSlider.setPreferredSize(new Dimension(60, 10));
		pLeftPanel.add(m_pZoomSlider);
		m_pZoomSlider.addChangeListener(this);
		m_pZoomSlider.setEnabled(false);
		
		final JPanel pRightPanel = new JPanel();
		final FlowLayout flowLayout_1 = new FlowLayout();
		flowLayout_1.setVgap(0);
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		pRightPanel.setLayout(flowLayout_1);
		panel.add(pRightPanel, BorderLayout.CENTER);
		
		m_lblOdometer = m_pDisplay.getOdometer();
		pRightPanel.add(m_lblOdometer);
		//
	}

	/**
	 * Método setter para configurar o caminho e nome do arquivo de imagem.
	 * @param sFullName
	 */
	public void setNames(String sFullName)
	{
        m_sFullName = sFullName;
        if(m_sFullName != null)
        {
	        int iPos = m_sFullName.lastIndexOf('/');
	        if (iPos > 0 &&  iPos < m_sFullName.length() - 1)
	            m_sShortName = m_sFullName.substring(iPos+1);
	        else
	        	m_sShortName = m_sFullName;
        }
	}

	/**
	 * Método utilizado para capturar o evento de clique do mouse na janela.
	 * @param e Objeto MouseEvent com o evento ocorrido.
	 */
	public void mouseClicked(MouseEvent e)
	{
		if(e.getSource() instanceof JToggleButton)
		{
			JToggleButton pBtn = (JToggleButton) e.getSource();
			if(pBtn.getName().equals("toggle"))
			{
				if(pBtn.isSelected())
				{
			    	m_pMagnifier = new CMagnifier();
			    				    	
			    	m_pMagnifier.setSource(m_pDisplay);
					m_pDisplay.add(m_pMagnifier);
			    	m_pMagnifier.setBounds(m_pMagnifier.getX(), m_pMagnifier.getY(), m_pSizeSlider.getValue(), m_pSizeSlider.getValue());
					
			    	m_pMagnifier.setMagnification((float) m_pZoomSlider.getValue());
			    	
					m_pTamLabel.setEnabled(true);
					m_pZoomLabel.setEnabled(true);
					
					m_pSizeSlider.setEnabled(true);
					m_pZoomSlider.setEnabled(true);
				}
				else
				{
					m_pDisplay.remove(m_pMagnifier);
					m_pMagnifier = null;

					m_pTamLabel.setEnabled(false);
					m_pZoomLabel.setEnabled(false);
					
					m_pSizeSlider.setEnabled(false);
					m_pZoomSlider.setEnabled(false);
				}
				repaint();
			}
			else if(pBtn.getName().equals("hist"))
			{
				for(int iBand = 0; iBand < m_aDispHistogram.length; iBand++)
					m_aDispHistogram[iBand].setVisible(pBtn.isSelected());
			}			
		}
	}

	/**
	 * Método de captura de eventos de entrada de mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mouseEntered(MouseEvent e)
	{
	}

	/**
	 * Método de captura de eventos de saída de mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mouseExited(MouseEvent e)
	{
	}

	/**
	 * Método de captura de eventos de pressionamento do botão do mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mousePressed(MouseEvent e)
	{
	}

	/**
	 * Método de captura de eventos de liberação do botão do mouse. Não utilizado: criado apenas devido à
	 * obrigação de implementação da interface de mouse, requerida para a captura do evento mouseClicked. 
	 */
	public void mouseReleased(MouseEvent e)
	{
	}

	/**
	 * Método utilizado para a captura dos eventos de mudança do tamanho e do zoom da lupa.
	 * 
	 * @param e Objeto ChangeEvent com o evento ocorrido.
	 */
	public void stateChanged(ChangeEvent e)
	{
		if(e.getSource() instanceof JSlider)
		{
			JSlider pSl = (JSlider) e.getSource();
			if(pSl.getName().equals("size"))
			{
				m_pMagnifier.setBounds(m_pMagnifier.getX(), m_pMagnifier.getY(), pSl.getValue(), pSl.getValue());
			}
			else if(pSl.getName().equals("zoom"))
			{
				m_pMagnifier.setMagnification((float) pSl.getValue());
			}
		}
	}
	
	/**
	 * Método getter para obtenção do objeto da imagem em exibição.
	 * @return Objeto CImage com a imagem em exibição.
	 */
	public CImage getImage() 
	{
		return m_pImage ;
	}
}