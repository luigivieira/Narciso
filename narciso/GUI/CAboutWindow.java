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
import java.awt.event.*;
import java.awt.image.renderable.*;
import javax.media.jai.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Classe utilizada para implementar a janela de Sobre, com informa��es do sistema.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public class CAboutWindow extends JDialog implements ActionListener
{
	/**
	 * Construtor da classe.
	 */
	public CAboutWindow()
	{
		super();
		setResizable(false);
		setModal(true);
		setTitle("Sobre o Narciso");
		setBounds(100, 100, 710, 501);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation((screenSize.width -  getSize().width) / 2, (screenSize.height - getSize().height) / 2);

		final JPanel pButtonPanel = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		pButtonPanel.setLayout(flowLayout);
		pButtonPanel.setPreferredSize(new Dimension(0, 35));
		getContentPane().add(pButtonPanel, BorderLayout.SOUTH);

		final JButton okButton = new JButton();
		okButton.setPreferredSize(new Dimension(70, 26));
		okButton.setText("Ok");
		okButton.addActionListener(this);
		pButtonPanel.add(okButton);

		final JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.black, 2, false));
		panel.setLayout(new BorderLayout());

		ParameterBlock pPB = (new ParameterBlock()).add(getClass().getResource("/GUI/images/logo.JPG"));
		PlanarImage pImage = JAI.create("url", pPB);
		
		panel.setPreferredSize(new Dimension(230, 428));
		panel.setOpaque(false);
		
		getContentPane().add(panel, BorderLayout.WEST);
		
		ImageDisplay pDisp = new ImageDisplay(pImage);
		panel.add(pDisp);

		final JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane);

		final JTextPane edText = new JTextPane();
		scrollPane.setViewportView(edText);
		edText.setEditable(false);
		edText.setAutoscrolls(true);
		edText.setText("Narciso\n" +
					   "----------------------\n" +
					   "Ambiente de Suporte ao Processamento de Imagens para Vis�o Computacional\n" +
					   "Vers�o: 1.0\n\n" +
					   "Copyright (C) 2006\n" +
					   "----------------------\n" +
					   "Kiran Mantripragada (kiran@mantripragada.com.br)\n" +
					   "Luiz Carlos Vieira (luigivieira@uol.com.br)\n\n" +
					   "Projeto de conclus�o da disciplina de Vis�o Computacional, " +
					   "orientado pelo Professor Doutor Flavius Portella Ribas Martins.\n\n" +
					   "IPT - Instituto de Pesquisas Tecnol�gicas (http://www.ipt.br)\n\n" +
					   "Requerimentos\n" +
					   "----------------------\n" +
					   "Java Runtime Environment (JRE) Vers�o 1.5.0.08 ou posterior;\n" +
					   "Java Advanced Imaging API (JAI) vers�o 1.1.2.01 ou superior;\n\n" +
					   "Termo de Utiliza��o (License Agreement)\n" +
					   "----------------------\n" +
					   "Este programa � um software livre; voc� pode redistribu�-lo e/ou modific�-lo " +
					   "sob os termos da licen�a GNU (General Public License) conforme publica��o pela " +
					   "Free Software Foundation; tanto a vers�o 2 da Licen�a ou (� sua escolha) qualquer " +
					   "vers�o posterior. " +
					   "Este programa � distribu�do na esperan�a de que seja �til, mas SEM QUALQUER GARANTIA; " +
					   "tamb�m sem a garantia impl�cita de COMERCIABILIDADE ou ADAPTABILIDADE A QUALQUER PROP�SITO " +
					   "PARTICULAR. Veja a Licen�a GNU para maiores detalhes.\n\n" +
					   "This program is free software; you can redistribute it and/or " +
					   "modify it under the terms of the GNU General Public License " +
					   "as published by the Free Software Foundation; either version 2 " +
					   "of the License, or (at your option) any later version. " +
					   "This program is distributed in the hope that it will be useful, " +
					   "but WITHOUT ANY WARRANTY; without even the implied warranty of " +
					   "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the " +
					   "GNU General Public License for more details.");
		edText.setCaretPosition(0);
		
		//
	}

	/**
	 * M�todo utilizado para gerenciar os eventos de a��o da janela (clique, por exemplo).
	 * 
	 * @param e Objeto ActionEvent do Java com o evento ocorrido.
	 */
	public void actionPerformed(ActionEvent e)
	{
		dispose();
	}
}