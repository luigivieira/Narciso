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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Classe utilizada para implementar a janela de exibição de exceção, exibida sempre que um erro ocorre no sistema.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public class CExceptionDialog extends JDialog
{
	
	/**
	 * Inner-Classe utilizada localmente em CExceptionDialog para a implementação da captura dos eventos dos botões de ação.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */

	public class ButtonListener implements ActionListener
	{
		/** Membro privado utilizado para armazenar a janela pai (janela de exceção) onde o evento foi gerado. */
		private CExceptionDialog m_pDialog;
		
		/**
		 * Construtor da classe. 
		 * @param pDialog Objeto CExceptionDialog com a janela pai para a captura dos eventos.
		 */
		public ButtonListener(CExceptionDialog pDialog)
		{
			m_pDialog = pDialog;
		}
		
		/**
		 * Método utilizado para a captura dos eventos de ação dos botões.
		 * @param e Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			m_pDialog.dispose();
		}
	}
	
	/** Membro privado utilizado para a exibição da pilha de chamadas. */
	private JTextPane m_edCallStack;
	
	/** Membro privado utilizado para a exibição da descrição da exceção ocorrida. */
	private JTextField m_edException;

	/**
	 * Método publico e estático utilizado para acionar a exibição da exceção dada.
	 * @param e Exceção ocorrida, que será analisada e descrita de forma amigável na janela apresentada.
	 */
	public static void showException(Exception e)
	{
		CExceptionDialog pDlg = new CExceptionDialog();
		StackTraceElement aStack[] = e.getStackTrace();
		String sText = "";
		for(int i = 0; i < aStack.length; i++)
		{
			String sFile = aStack[i].getFileName();			
			String sClass = aStack[i].getClassName();
			int    iLine = aStack[i].getLineNumber();
			String sStack;
			
			if(sFile == null)
				sFile = "(não definido)";
			
			sStack = "Arquivo " + sFile + ", classe " + sClass + ", linha " + iLine;
			
			if(i != 0)
				sText = sText + "\n";
			
			sText = sText + sStack;
		}

		pDlg.m_edException.setText(e.toString());		
		pDlg.m_edCallStack.setText(sText);
		pDlg.m_edCallStack.setCaretPosition(0);
		pDlg.setVisible(true);
	}
	
	/**
	 * Construtor da classe.
	 */
	public CExceptionDialog()
	{
		super();
		setName("pExceptionDialog");
		setModal(true);
		setTitle("Exceção Ocorrida");
		setBounds(100, 100, 552, 329);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation((screenSize.width -  getSize().width) / 2, (screenSize.height - getSize().height) / 2);		
		
		final JTextPane textPane = new JTextPane();
		textPane.setBorder(new LineBorder(Color.black, 1, false));
		textPane.setBackground(new Color(217, 255, 217));
		textPane.setPreferredSize(new Dimension(0, 55));
		textPane.setEditable(false);
		textPane.setText("A operação executada gerou uma exceção, conforme descrição do erro provida abaixo. Por favor, verifique as condições de erro e tente novamente. Se desejar, por favor entre em contato com a equipe de desenvolvimento do Narciso.");
		getContentPane().add(textPane, BorderLayout.NORTH);

		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);

		final JPanel panel_1 = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel_1.setLayout(flowLayout);
		panel_1.setPreferredSize(new Dimension(0, 35));
		panel.add(panel_1, BorderLayout.SOUTH);

		final JButton okButton = new JButton();
		okButton.addActionListener(new ButtonListener(this));
		okButton.setPreferredSize(new Dimension(70, 25));
		okButton.setText("Ok");
		panel_1.add(okButton);

		final JPanel panel_2 = new JPanel();
		final BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(10);
		panel_2.setLayout(borderLayout);
		panel_2.setPreferredSize(new Dimension(0, 25));
		panel.add(panel_2, BorderLayout.NORTH);

		final JLabel label = new JLabel();
		label.setText(" Exceção:");
		panel_2.add(label, BorderLayout.WEST);

		m_edException = new JTextField();
		m_edException.setEditable(false);
		m_edException.setMargin(new Insets(1, 1, 1, 1));
		panel_2.add(m_edException, BorderLayout.CENTER);

		final JPanel pCenter = new JPanel();
		pCenter.setLayout(new BorderLayout());
		panel.add(pCenter, BorderLayout.CENTER);

		final JLabel pilhaDeChamadasLabel = new JLabel();
		pilhaDeChamadasLabel.setText(" Pilha de Chamadas:");
		pCenter.add(pilhaDeChamadasLabel, BorderLayout.NORTH);

		final JScrollPane scrollPane = new JScrollPane();
		pCenter.add(scrollPane);

		m_edCallStack = new JTextPane();
		scrollPane.setViewportView(m_edCallStack);
		m_edCallStack.setFont(new Font("Courier New", Font.PLAIN, 12));
		m_edCallStack.setEditable(false);
		//
	}
}