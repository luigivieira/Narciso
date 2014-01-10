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
import java.awt.event.*;

import javax.media.jai.PlanarImage;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import core.images.CImage;
import core.info.CHistogram;

/**
 * Classe utilizada para implementar a janela de comparação de duas imagens.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public class CCompareWindow extends JInternalFrame implements AdjustmentListener, MouseListener, ChangeListener
{
	/** Membro privado utilizado para armazenar o objeto da imagem à esquerda. */
	private CImage m_pLImage;
	
	/** Membro privado utilizado para armazenar o objeto de exibição da imagem à esquerda. */
	private ImageDisplay m_pLDisplay;
	
	/** Membro privado utilizado para armazenar as imagens de gráfico de histograma para todas as bandas da imagem à esquerda. */
	private ImageDisplay m_aLDispHistogram[];

	/** Membro privado utilizado para armazenar o objeto da imagem à direita. */
	private CImage m_pRImage;

	/** Membro privado utilizado para armazenar o objeto de exibição da imagem à direita. */
	private ImageDisplay m_pRDisplay;

	/** Membro privado utilizado para armazenar as imagens de gráfico de histograma para todas as bandas da imagem à direita. */
	private ImageDisplay m_aRDispHistogram[];

	/** Membro privado utilizado para armazenar o objeto de odômetro (marcador de posição dos pixels) sobre a imagem à esquerda. */
	private JLabel m_lblLOdometer;

	/** Membro privado utilizado para armazenar o objeto de odômetro (marcador de posição dos pixels) sobre a imagem à direita. */
	private JLabel m_lblROdometer;

	/** Membro privado utilizado para armazenar o objeto de controle de rolagem da imagem à esquerda. */
	private JScrollPane m_pLeftScroll;

	/** Membro privado utilizado para armazenar o objeto de controle de rolagem da imagem à direita. */
	private JScrollPane m_pRightScroll;

	/** Membro privado utilizado para armazenar indicação de execução durante evento, evitando re-entrada em um mesmo código. */
	private boolean m_bInEvent = false;

	/** Membro privado utilizado para armazenar o objeto da lupa sobre a imagem à esquerda. */
	private CMagnifier m_pLMagnifier;
	
	/** Membro privado utilizado para armazenar o objeto da lupa sobre a imagem à direita. */
	private CMagnifier m_pRMagnifier;
	
	/** Membro privado utilizado para armazenar o objeto texto com o tamanho das lupas. */
	private JLabel m_pTamLabel;
	
	/** Membro privado utilizado para armazenar o objeto texto com o zoom das lupas. */
	private JLabel m_pZoomLabel;
	
	/** Membro privado utilizado para armazenar o objeto de alteração do tamanho das lupas. */
	private JSlider m_pSizeSlider;
	
	/** Membro privado utilizado para armazenar o objeto de alteração do zoom das lupas. */
	private JSlider m_pZoomSlider;
	
	/**
	 * Construtor da classe.
	 * 
	 * @param pLImage Objeto CImage com a imagem a ser exibida na esquerda.
	 * @param pRImage Objeto CImage com a imagem a ser exibida na direita.
	 */
	public CCompareWindow(CImage pLImage, CImage pRImage)
	{
		super();
		getContentPane().setLayout(new BorderLayout());
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle("Comparação de Imagens");
		setBounds(100, 100, 500, 375);

    	m_pLImage = pLImage;
		
    	m_pRImage = pRImage;

		final JPanel pStatusBar = new JPanel();
		pStatusBar.setLayout(new BorderLayout());
		pStatusBar.setPreferredSize(new Dimension(0, 18));
		getContentPane().add(pStatusBar, BorderLayout.SOUTH);

		final JPanel pLeftPanel = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(2);
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.LEFT);
		pLeftPanel.setLayout(flowLayout);
		pLeftPanel.setPreferredSize(new Dimension(240, 0));
		pStatusBar.add(pLeftPanel, BorderLayout.WEST);

		final JToggleButton btHist = new JToggleButton();
		btHist.setPreferredSize(new Dimension(16, 16));
		btHist.setIcon(new ImageIcon(getClass().getResource("/GUI/images/Histogram16.gif")));
		btHist.setName("hist");
		btHist.addMouseListener(this);
		pLeftPanel.add(btHist);

		final JToggleButton btZoom = new JToggleButton();
		btZoom.setPreferredSize(new Dimension(16, 16));
		btZoom.setIcon(new ImageIcon(getClass().getResource("/GUI/images/Zoom16.gif")));
		btZoom.setName("toggle");
		btZoom.addMouseListener(this);
		pLeftPanel.add(btZoom);

		m_pTamLabel = new JLabel();
		pLeftPanel.add(m_pTamLabel);
		m_pTamLabel.setText("Tam.:");
		m_pTamLabel.setEnabled(false);

		m_pSizeSlider = new JSlider();
		m_pSizeSlider.setPreferredSize(new Dimension(60, 10));
		pLeftPanel.add(m_pSizeSlider);
		m_pSizeSlider.setName("size");
		m_pSizeSlider.setMinimum(50);
		m_pSizeSlider.setMaximum(200);
		m_pSizeSlider.addChangeListener(this);
		m_pSizeSlider.setEnabled(false);

		m_pZoomLabel = new JLabel();
		m_pZoomLabel.setText("Zoom:");
		pLeftPanel.add(m_pZoomLabel);
		m_pZoomLabel.setEnabled(false);		

		m_pZoomSlider = new JSlider();
		m_pZoomSlider.setPreferredSize(new Dimension(60, 10));
		pLeftPanel.add(m_pZoomSlider);
		m_pZoomSlider.setValue(2);
		m_pZoomSlider.setName("zoom");
		m_pZoomSlider.setMinimum(2);
		m_pZoomSlider.setMaximum(20);
		m_pZoomSlider.addChangeListener(this);
		m_pZoomSlider.setEnabled(false);

		final JPanel pRightPanel = new JPanel();
		final FlowLayout flowLayout_1 = new FlowLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		flowLayout_1.setVgap(0);
		pRightPanel.setLayout(flowLayout_1);
		pStatusBar.add(pRightPanel);

		final JPanel pArea = new JPanel();
		pArea.setLayout(new BoxLayout(pArea, BoxLayout.X_AXIS));
		getContentPane().add(pArea);

		m_pLeftScroll = new JScrollPane();
		pArea.add(m_pLeftScroll);
		m_pLeftScroll.setName("left");
		m_pLeftScroll.getHorizontalScrollBar().addAdjustmentListener(this);
		m_pLeftScroll.getVerticalScrollBar().addAdjustmentListener(this);

		final JPanel pLeftArea = new JPanel();
		pLeftArea.setLayout(new BorderLayout());
		m_pLeftScroll.setViewportView(pLeftArea);
		m_pLDisplay = new ImageDisplay(pLImage.getPlanarImage());
		pLeftArea.add(m_pLDisplay, BorderLayout.CENTER);

		CHistogram pHist = new CHistogram(256, pLImage);
		m_aLDispHistogram = new ImageDisplay[pHist.getNumBands()];
		for(int iBand = 0; iBand < pHist.getNumBands(); iBand++)
		{
			PlanarImage pTemp = pHist.createHistogramImage(iBand, 89, 30, null, false).getPlanarImage();
			m_aLDispHistogram[iBand] = new ImageDisplay(pTemp);
			m_pLDisplay.add(m_aLDispHistogram[iBand]);
			m_aLDispHistogram[iBand].setBounds(5, 5 + (iBand * pTemp.getHeight() + 5), pTemp.getWidth(), pTemp.getHeight());
			m_aLDispHistogram[iBand].setVisible(false);
		}
		
		m_pRightScroll = new JScrollPane();
		pArea.add(m_pRightScroll);
		m_pRightScroll.setName("right");
		m_pRightScroll.getHorizontalScrollBar().addAdjustmentListener(this);
		m_pRightScroll.getVerticalScrollBar().addAdjustmentListener(this);		

		final JPanel pRightArea = new JPanel();
		pRightArea.setLayout(new BorderLayout());
		m_pRightScroll.setViewportView(pRightArea);
		m_pRDisplay = new ImageDisplay(pRImage.getPlanarImage());
		pRightArea.add(m_pRDisplay, BorderLayout.CENTER);

		pHist = new CHistogram(256, pRImage);
		m_aRDispHistogram = new ImageDisplay[pHist.getNumBands()];
		for(int iBand = 0; iBand < pHist.getNumBands(); iBand++)
		{
			PlanarImage pTemp = pHist.createHistogramImage(iBand, 89, 30, null, false).getPlanarImage();
			m_aRDispHistogram[iBand] = new ImageDisplay(pTemp);
			m_pRDisplay.add(m_aRDispHistogram[iBand]);
			m_aRDispHistogram[iBand].setBounds(5, 5 + (iBand * pTemp.getHeight() + 5), pTemp.getWidth(), pTemp.getHeight());
			m_aRDispHistogram[iBand].setVisible(false);
		}
		
		m_lblLOdometer = m_pLDisplay.getOdometer();
		m_lblROdometer = m_pRDisplay.getOdometer();
		pRightPanel.add(m_lblLOdometer);
		pRightPanel.add(m_lblROdometer);
		//
	}

	/**
	 * Método utilizado para capturar os eventos de rolagem das imagens para sincronização de ambos os lados.
	 * 
	 * @param e Objeto AdjustmentEvent do Java com o evento ocorrido.
	 */
	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		if(m_bInEvent)
			return;
		
		m_bInEvent = true;
		Adjustable pSource = e.getAdjustable();
        int iOrientation = pSource.getOrientation();
		JScrollBar pBar = (JScrollBar) e.getSource();
		JScrollPane pPane = (JScrollPane) pBar.getParent();

		if(pPane.getName().equals("left"))
		{
			if(iOrientation == Adjustable.HORIZONTAL)
				m_pRightScroll.getHorizontalScrollBar().setValue(pBar.getValue());
			else
				m_pRightScroll.getVerticalScrollBar().setValue(pBar.getValue());
		}
		else if(pPane.getName().equals("right"))
		{
			if(iOrientation == Adjustable.HORIZONTAL)
				m_pLeftScroll.getHorizontalScrollBar().setValue(pBar.getValue());
			else
				m_pLeftScroll.getVerticalScrollBar().setValue(pBar.getValue());	
		}
		m_bInEvent = false;
	}
	
	/**
	 * Método getter para obtenção da imagem exibida à direita.
	 * @return Objeto CImage com a imagem exibida à direita.
	 */
	public CImage getRightImage()
	{
		return m_pRImage;
	}

	/**
	 * Método getter para obtenção da imagem exibida à esquerda.
	 * @return Objeto CImage com a imagem exibida à esquerda.
	 */
	public CImage getLeftImage()
	{
		return m_pLImage;
	}

	/**
	 * Método utilizado para capturar os eventos de clique do mouse na janela.
	 * @param e Objeto MouseEvent do Java com o evento ocorrido.
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
			    	m_pLMagnifier = new CMagnifier();
			    	m_pRMagnifier = new CMagnifier();
			    				    	
			    	m_pLMagnifier.setSource(m_pLDisplay);
					m_pLDisplay.add(m_pLMagnifier);
			    	m_pLMagnifier.setBounds(m_pLMagnifier.getX(), m_pLMagnifier.getY(), m_pSizeSlider.getValue(), m_pSizeSlider.getValue());

			    	m_pRMagnifier.setSource(m_pRDisplay);
					m_pRDisplay.add(m_pRMagnifier);
			    	m_pRMagnifier.setBounds(m_pRMagnifier.getX(), m_pRMagnifier.getY(), m_pSizeSlider.getValue(), m_pSizeSlider.getValue());
			    	
			    	m_pLMagnifier.setMagnification((float) m_pZoomSlider.getValue());
			    	m_pRMagnifier.setMagnification((float) m_pZoomSlider.getValue());
			    	
					m_pTamLabel.setEnabled(true);
					m_pZoomLabel.setEnabled(true);
					
					m_pSizeSlider.setEnabled(true);
					m_pZoomSlider.setEnabled(true);
					
					m_pLMagnifier.setSyncPeer(m_pRMagnifier);
					m_pRMagnifier.setSyncPeer(m_pLMagnifier);
				}
				else
				{
					m_pLDisplay.remove(m_pLMagnifier);
					m_pLMagnifier = null;

					m_pRDisplay.remove(m_pRMagnifier);
					m_pRMagnifier = null;
					
					m_pTamLabel.setEnabled(false);
					m_pZoomLabel.setEnabled(false);
					
					m_pSizeSlider.setEnabled(false);
					m_pZoomSlider.setEnabled(false);
				}
				repaint();
			}
			else if(pBtn.getName().equals("hist"))
			{
				for(int iBand = 0; iBand < m_aLDispHistogram.length; iBand++)
					m_aLDispHistogram[iBand].setVisible(pBtn.isSelected());
				for(int iBand = 0; iBand < m_aLDispHistogram.length; iBand++)
					m_aRDispHistogram[iBand].setVisible(pBtn.isSelected());
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
	 * Método utilizado para a captura dos eventos de mudança do tamanho e do zoom das lupas.
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
				m_pLMagnifier.setBounds(m_pLMagnifier.getX(), m_pLMagnifier.getY(), pSl.getValue(), pSl.getValue());
				m_pRMagnifier.setBounds(m_pRMagnifier.getX(), m_pRMagnifier.getY(), pSl.getValue(), pSl.getValue());
			}
			else if(pSl.getName().equals("zoom"))
			{
				m_pLMagnifier.setMagnification((float) pSl.getValue());
				m_pRMagnifier.setMagnification((float) pSl.getValue());
			}
		}		
	}
}