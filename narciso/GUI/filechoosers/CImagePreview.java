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
 
package GUI.filechoosers;

import javax.swing.*;
import java.beans.*;
import java.awt.*;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderableImage;
import java.io.File;
import javax.media.jai.*;

/**
 * Classe utilizada para a construção da janela de seleção de arquivos do sistema Narciso. Esta classe
 * provê o preview da imagem cujo arquivo for selecionado na janela de seleção de arquivos.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 */

@SuppressWarnings("serial")
public class CImagePreview extends JComponent implements PropertyChangeListener
{
	/** Membro privado utilizado para armazenar a imagem da miniatura (preview) do arquivo selecionado. */
	private ImageIcon m_pThumbnail = null;
	
	/** Membro privado utilizado para armazenar o objeto File para manipulação do arquivo selecioando. */
    private File m_fFile = null;

    /**
     * Construtor da classe.
     * @param pFC Objeto JFileChooser com a janela de seleção de arquivos onde o preview deverá ser desenhado.
     */
    public CImagePreview(JFileChooser pFC)
    {
        setPreferredSize(new Dimension(200, 50));
        pFC.addPropertyChangeListener(this);
    }

    /**
     * Método utilizado para carregar o arquivo de imagem selecionado, para a exibição do preview.
     */
    public void loadImage()
    {
        if (m_fFile == null)
        {
        	m_pThumbnail = null;
            return;
        }

        PlanarImage pTemp = JAI.create("fileload", m_fFile.getPath());
        
        ParameterBlock pPB = new ParameterBlock();
        pPB.addSource(pTemp);
        pPB.add(null);
        pPB.add(null);
        pPB.add(null);
        pPB.add(null);
        pPB.add(null);
        RenderableImage pRend = JAI.createRenderable("renderable", pPB);
        
        pPB = new ParameterBlock();
        pPB.addSource(pRend);
                
        int iFixed = 150;
        double dPercent;
        int iWidth = pTemp.getWidth();
        int iHeight = pTemp.getHeight();
        
        if(iWidth > iHeight) // Se é mais alta do que larga
        {
        	dPercent = (iFixed * 100.0 / iHeight);
        	iHeight = iFixed;
        	iWidth = (int) (iWidth * (dPercent / 100.0));
        }
        else // Se é mais larga do que alta
        {
        	dPercent = (iFixed * 100.0 / iWidth);
        	iWidth = iFixed;
        	iHeight = (int) (iHeight * (dPercent / 100.0));        	
        }
        
        PlanarImage pTarget = (PlanarImage) pRend.createScaledRendering(iWidth, iHeight, null);
        
        m_pThumbnail = new ImageIcon(pTarget.getAsBufferedImage());
    }

    /**
     * Método utilizado para capturar o evento de mudança na seleção de arquivos.
     * @param e Objeto PropertyChangeEvent com o evento ocorrido.
     */
    public void propertyChange(PropertyChangeEvent e)
    {
        boolean bUpdate = false;
        
        String sProp = e.getPropertyName();

        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(sProp))
        {
        	m_fFile = null;
            bUpdate = true;
        }
        else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(sProp))
        {
        	m_fFile = (File) e.getNewValue();
            bUpdate = true;
        }

        if (bUpdate)
        {
        	m_pThumbnail = null;
            if(isShowing())
            {
                loadImage();
                repaint();
            }
        }
    }

    /**
     * Método utilizado para capturar o evento de pintura do componente, para que a miniatura
     * (preview) possa ser apresentada.
     * @param g Objeto Graphics para acesso às operações de pintura.
     */
    protected void paintComponent(Graphics g)
    {
        if (m_pThumbnail == null)
            loadImage();

        if (m_pThumbnail != null)
        {
            int x = getWidth()/2 - m_pThumbnail.getIconWidth()/2;
            int y = getHeight()/2 - m_pThumbnail.getIconHeight()/2;

            if (y < 0)
                y = 0;

            if (x < 5)
            	x = 5;

            m_pThumbnail.paintIcon(this, g, x, y);
        }
    }
}