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
import java.net.URL;

/**
 * Classe utilizada para implementar o Splash do sistema Narciso (exibi��o de imagem de entrada, durante o carregamento do sistema).
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public final class CSplash extends Frame
{
	/** Membro privado utilizado para armazenar o nome do arquivo da imagem de splash. */
	private String m_sImage;
	
	/** Membro privado utilizado para armazenar a imagem de splash. */
	private Image m_pImage;
	
	/** Membro privado utilizado para armazenar o objeto MediaTracker para controle das atualiza��es do Java. */
	private MediaTracker m_pMediaTracker;

	/**
	 * Construtor da classe.
	 * @param sImage Nome do arquivo de Splash a ser utilizado.
	 */
	public CSplash(String sImage)
	{
		if(sImage == null || sImage.trim().length() == 0)
			throw new IllegalArgumentException("Nenhuma imagem foi indicada para a tela de splash.");
		m_sImage = sImage;
	}
   
	/**
	 * M�todo utilizado para exibir a imagem de splash. A exibi��o da imagem � efetuada de forma a 
	 * durar todo o processo de carregamento do sistema, mas sem interromper seu processamento de inicializa��o.
	 * O objeto MediaTracker do Java � utilizado para esse controle, e quando o sistema est� totalmente carregado,
	 * a imagem � removida da tela.
	 */
	public void splash()
	{
		initImageAndTracker();
		setSize(m_pImage.getWidth(null), m_pImage.getHeight(null));
		center();
    
		m_pMediaTracker.addImage(m_pImage, 0);
		try
		{
			m_pMediaTracker.waitForID(0);
		}
		catch(InterruptedException ie)
		{
			System.out.println("Erro na espera (tracking) pelo ID da imagen informada para a tela de splash.");
		}

		new CSplashWindow(this, m_pImage);
	}

	/**
	 * M�todo de utiliza��o interna (private) para inicializa��o do MediaTracker do Java.
	 */
	private void initImageAndTracker()
	{
		m_pMediaTracker = new MediaTracker(this);
		URL imageURL = CSplash.class.getResource(m_sImage);
		m_pImage = Toolkit.getDefaultToolkit().getImage(imageURL);
	}

	/**
	 * M�todo de utiliza��o interna (private) utilizado para centralizar a janela com a imagem, segunndo
	 * as configura��es do desktop onde o sistema est� em execu��o.
	 *
	 */
	private void center()
	{
		Dimension pScreen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle pFrame = getBounds();
		setLocation((pScreen.width - pFrame.width)/2, (pScreen.height - pFrame.height)/2);
	}
 
	/**
	 * Inner-classe utilizada em CSplash para implementar a janela onde o Splash � exibido.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */
	private class CSplashWindow extends Window
	{
		/** Membro privado utilizado para armazenar a imagem do splash. */
		private Image m_pImage;
		
		/**
		 * Construtor da classe.
		 * @param pParent Objeto Frame com a janela pai do Splash.
		 * @param pImage Objeto Image do Java com a imagem para exibi��o.
		 */
		CSplashWindow(Frame pParent, Image pImage)
		{
			super(pParent);
			m_pImage = pImage;
			setSize(m_pImage.getWidth(null), m_pImage.getHeight(null));
			Dimension pScreen = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle pWindow = getBounds();
			setLocation((pScreen.width - pWindow.width) / 2, (pScreen.height - pWindow.height) / 2);
			setVisible(true);
		}

		/** M�todo utilizado para a captura do evento de pintura da janela, para realizar a pintura
		 *  da imagem de splash
		 *  @param graphics Objeto Graphics do Java para acesso aos m�todos de pintura.
		 */
		public void paint(Graphics graphics)
		{
			if (m_pImage != null)
				graphics.drawImage(m_pImage,0,0,this);
		}
	}
}