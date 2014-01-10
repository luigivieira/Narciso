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

import java.awt.*;
import java.awt.image.renderable.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.media.jai.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Classe utilizada para implementar a lupa sobre imagens nas janelas de exibi��o e compara��o do sistema.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public class CMagnifier extends JComponent
{
	/** Membro privado utilizado para armazenar o objeto de imagem do JAI. */
	private PlanarImage m_pImage;
	
	/** Membro privado utilizado para armazenar a indica��o do componente pai da lupa (aquele sobra o qual a lupa ser� utilizada). */
	private JComponent m_pParent = null;
	
	/** Membro privado utilizado para armazenar o valor do n�vel de magnifica��o (zoom) da lupa. */
	private float m_fMagnification = 2.0F;
	
	/** Membro privado utilizado para indicar quando h� a necessidade de atualiza��o de uma lupa par (peer), como no caso da janela de compara��o. */
	private CMagnifier m_pSyncPeer = null;

	/**
	 * Construtor da classe.
	 */
	public CMagnifier()
	{
		setOpaque(true);
    	setMagnification(2F);
    	setBounds(0, 0, 50, 50);
    	setPreferredSize(new Dimension(50, 50));
    	setBorder(new LineBorder(Color.RED, 1, false));
    	setVisible(true);		
	}

	/**
	 * M�todo setter para a configura��o do componente pai da lupa (aquele sobre o qual a lupa ser� utilizada).
	 * @param pParent Objeto JComponent com o objeto pai da lupa.
	 */
	public void setSource(JComponent pParent)
	{
		m_pParent = pParent;
		m_pParent.addMouseListener(new MouseClickHandler());
		m_pParent.addMouseMotionListener(new MouseMotionHandler());
		
		if(m_pParent instanceof ImageDisplay)
			m_pImage = (PlanarImage)((ImageDisplay) m_pParent).getImage();
	}
	
	/**
	 * M�todo getter para obten��o do componente pai da lupa (aquele sobre o qual a lupa ser� utilizada).
	 * @return Objeto JComponent com o objeto pai da lupa.
	 */
	public JComponent getSource()
	{
		return m_pParent;
	}
	
	/**
	 * M�todo setter para a configura��o do n�vel de magnifica��o (zoom) da lupa.
	 * @param fValue Valor em ponto flutuante com o n�vel de magnifica��o. Deve ser maior do que 0,005.
	 */
	public void setMagnification(float fValue)
	{
		if(fValue < 0.005)
			m_fMagnification = 0.005F;
		else
			m_fMagnification = fValue;
		
		repaint();
	}
	
	/**
	 * M�todo setter para a configura��o de uma lupa par (peer), como no caso da utiliza��o na janela de compara��o de imagens.
	 * Obs.: Esse m�todo deve ser chamado em ambas as lupas, passando como par�metro a lupa inversa.
	 * @param pMag Objeto CMagnifier com a lupa par a ser configurada.
	 */
	public void setSyncPeer(CMagnifier pMag)
	{
		m_pSyncPeer = pMag;	
	}
	
	/**
	 * M�todo getter para a obten��o da lupa par (peer) configurada. 
	 * @return Objeto CMagnifier com a lupa par configurada, ou null se n�o existe uma lupa configurada como par para esta.
	 */
	public CMagnifier getSyncPeer()
	{
		return m_pSyncPeer;
	}
	
	/**
	 * M�todo para a captura do evento de pintura do componente, utilizado para implementar a pintura da �rea
	 * da imagem sobre a qual a lupa se encontra, de forma magnificada (zoom).
	 * 
	 * @param g Objeto Graphics do Java utilizado para as opera��es de pintura.
	 */
	public synchronized void paintComponent(Graphics g)
	{
		Graphics2D g2D = null;
		g2D = (Graphics2D)g;
		
		g2D.setColor(getBackground());
		g2D.fillRect(0, 0, getWidth(), getHeight());
		
		if(m_pImage != null)
		{
			Dimension pDim = getSize();
			Point pPoint = getLocation();
			Insets pInsets = m_pParent.getInsets();
	
			int iWidth = (int)((float) pDim.width / m_fMagnification + .5F) + 1;
			int iHeight = (int)((float) pDim.height / m_fMagnification + .5F) + 1;
			
			int x = pPoint.x + (pDim.width - iWidth)/2 - pInsets.left;
			int y = pPoint.y + (pDim.height - iHeight)/2 - pInsets.top;
			
			if(x < 0)
				x = 0;
			
			if(y < 0)
				y = 0;
		
			if((x + iWidth) > m_pImage.getWidth())
				iWidth = m_pImage.getWidth() - x;
		
			if((y + iHeight) > m_pImage.getHeight())
				iHeight = m_pImage.getHeight() - y;
		
			ParameterBlock pPB = new ParameterBlock();
			
			pPB.addSource(m_pImage);
			pPB.add((float) x);
			pPB.add((float) y);
			pPB.add((float) iWidth);
			pPB.add((float) iHeight);
			RenderedOp pTmp = JAI.create("crop", pPB, null);

			pPB = new ParameterBlock();
			pPB.addSource(pTmp);
			pPB.add(m_fMagnification);
			pPB.add(m_fMagnification);
			pPB.add((float) -x * m_fMagnification);
			pPB.add((float) -y * m_fMagnification);
			pPB.add(new InterpolationNearest());
			
			PlanarImage pDst = JAI.create("scale", pPB, null);
		
			AffineTransform pTrf = AffineTransform.getTranslateInstance(0, 0);
			g2D.drawRenderedImage(pDst, pTrf);
		}
	}
	
	/**
	 * Inner-classe utilizada em CMagnifier para a captura dos eventos de clique de mouse.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 */
	class MouseClickHandler extends MouseAdapter
	{
		/**
		 * M�todo utilizado para a captura do evento de mouse pressionado sobre a lupa.
		 * 
		 * @param e Objeto MouseEvent com o evento ocorrido.
		 */
		public void mousePressed(MouseEvent e)
		{
			int iMods = e.getModifiers();
			Point pPoint = e.getPoint();
		
			if((iMods & InputEvent.BUTTON1_MASK) != 0)
				moveit(pPoint.x, pPoint.y, true);
		}
	
		/**
		 * M�todo de captura do evento de libera��o do bot�o do mouse. N�o utilizado: criado apenas 
		 * para satisfazer a interface implementada, requerida para a captura do mousePressed.
		 */
		public void mouseReleased(MouseEvent e)
		{
		}
	}
	
	/**
	 * Inner-classe utilizada em CMagnifier para a captura dos eventos de arraste de mouse.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 */
	class MouseMotionHandler extends MouseMotionAdapter
	{
		/**
		 * M�todo utilizado para a captura do evento de arraste do mouse.
		 * 
		 * @param e Objeto MouseEvent com o evento ocorrido.
		 */
		public void mouseDragged(MouseEvent e)
		{
			Point pPoint = e.getPoint();
			int iMods = e.getModifiers();
		
			if((iMods & InputEvent.BUTTON1_MASK) != 0)
				moveit(pPoint.x, pPoint.y, true);
		}
	}

	/**
	 * M�todo utilizado para mover a lupa para as coordenadas dadas. Utilizado nos m�todos de captura dos
	 * eventos para implementar o arraste da lupa sobre a imagem portadora.
	 * 
	 * @param px Coordenada X para posicionamento do centro da lupa.
	 * @param py Coordenada Y para posicionamento do centro da lupa.
	 * @param bCallPeer Vari�vel l�gica indicando se o m�todo deve chamar o peer. Se true, o mesmo m�todo ser� chamado automaticamente para o par configurado, com a indica��o false nesse par�metro.
	 */
	public final void moveit(int px, int py, boolean bCallPeer)
	{
		Insets pInset = m_pParent.getInsets();
		Dimension pDim = getSize();
		Dimension pDimParent = m_pParent.getSize();
		
		int pw = pDim.width / 2;
		int ph = pDim.height / 2;
		int x = px - pw;
		int y = py - ph;
		
		if(px < pInset.left)
			x = -pw + pInset.left;
		
		if(py < pInset.top)
			y = -ph + pInset.top;
		
		if(px >= (pDimParent.width - pInset.right))
			x = pDimParent.width - pw - pInset.right;
		
		if(py >= (pDimParent.height - pInset.bottom))
			y = pDimParent.height - ph - pInset.bottom;
		
		setLocation(x, y);
		
		if(bCallPeer && m_pSyncPeer != null)
		{
			m_pSyncPeer.moveit(px, py, false);
		}
	}
}