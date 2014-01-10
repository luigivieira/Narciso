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
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import GUI.filechoosers.*;

import core.errors.*;
import core.images.*;
import core.operations.*;

/**
 * Classe utilizada para implementar a janela de di�logo para a execu��o de opera��es e roteiros.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public class COperationDialog extends JDialog implements ActionListener, ListSelectionListener, ChangeListener
{
	/** Membro privado utilizado para conter o objeto de exibi��o da descri��o da opera��o. */
	private JTextArea m_edDesc;
	
	/** Membro privado utilizado para conter a lista de opera��es para execu��o em roteiro. */
	private JList m_lsRight;
	
	/** Membro privado utilizado para conter a lista de opera��es dispon�veis para execu��o de um roteiro. */
	private JList m_lsLeft;
	
	/** Membro privado utilizado para conter o objeto de exibi��o do texto indicativo do n�mero de imagens selecionadas na janela de miniaturas. */
	private JLabel m_lblNumImgs = null;
	
	/** Membro privado utilizado para conter o objeto de exibi��o da descri��o do roteiro. */
	private JTextPane m_edDescription = null;
	
	/** Membro privado utilizado para conter o objeto de exibi��o/edi��o dos par�metros de uma opera��o ou roteiro. */
	private JTextField m_edParams = null;

	/** Membro privado utilizado para conter a lista de opera��es dispon�veis para execu��o individual. */
	private JComboBox m_cbOperations = null;

	/** Membro privado utilizado para conter o objeto de edi��o do n�mero de itera��es de uma opera��o para execu��o em um roteiro. */
	private JSpinner m_edIter = null;
	
	/** Membro privado utilizado para armazenar os objetos de imagem selecionados na janela de miniaturas (com as imagens a serem utilizadas como par�metros de opera��es ou roteiros). */
	private Vector<CThumbnail> m_vThumbs = null;

	/** Membro privado utilizado para conter os objetos das opera��es registradas no sistema. */
	private Vector<COperation> m_vOperations = null;
	
	/** Membro privado utilizado para conter a a��o de adicionar opera��o selecionada para execu��o em roteiro. */
	private CAddAction m_acAdd;
	
	/** Membro privado utilizado para conter a a��o de adicionar todas as opera��es dispon�veis para execu��o em roteiro. */
	private CAddAllAction m_acAddAll;

	/** Membro privado utilizado para conter a a��o de remover a opera��o selecionada da execu��o em roteiro. */
	private CDelAction m_acDel;

	/** Membro privado utilizado para conter a a��o de remover todas as opera��es contindas na execu��o do roteiro. */
	private CDelAllAction m_acDelAll;
	
	/** Membro privado utilizado para conter a a��o de priorizar (subir um n�vel de execu��o) a opera��o selecionada no roteiro. */
	private CUpAction m_acUp;
	
	/** Membro privado utilizado para conter a a��o de priorizar totalmente (elevar ao primeiro n�vel de execu��o) a opera��o selecionada no roteiro. */
	private CUpAllAction m_acUpAll;
	
	/** Membro privado utilizado para conter a a��o de despriorizar (descer um n�vel de execu��o) a opera��o selecionada no roteiro. */
	private CDownAction m_acDown;
	
	/** Membro privado utilizado para conter a a��o de despriorizar totalmente (rebaixar ao �ltimo n�vel de execu��o) a opera��o selecionada no roteiro. */
	private CDownAllAction m_acDownAll;
	
	/** Membro privado utilizado para conter a a��o gravar o roteiro criado em um arquivo XML para posterior execu��o. */
	private CSaveAction m_acSave;
	
	/** Membro privado utilizado para conter a a��o carregar um roteiro previamente gravado em um arquivo XML. */
	private CLoadAction m_acLoad;
	
	/** Membro privado utilizado para conter a lista de itera��es configuradas para as opera��es dispon�veis no roteiro. */
	private Map<String, Integer> m_vIteractions;
	
	/** Membro privado utilizado para conter as pastas (folders) delimitadoras da execu��o de uma �nica opera��o ou de um roteiro, na janela de execu��o. */
	private JTabbedPane m_pTabbedPane;
	
	/**
	 * Construtor da classe.
	 */
	public COperationDialog()
	{
		super();
		
		m_vIteractions = new HashMap<String, Integer>();
		
		m_acAdd = new CAddAction();
		m_acAddAll = new CAddAllAction();
		m_acDel = new CDelAction();
		m_acDelAll = new CDelAllAction();
		m_acUp = new CUpAction();
		m_acUpAll = new CUpAllAction();
		m_acDown = new CDownAction();
		m_acDownAll = new CDownAllAction();
		m_acSave = new CSaveAction();
		m_acLoad = new CLoadAction();
		
		m_vThumbs = CWindowManager.getThumbnailWindow().getMarkedThumbs();
		m_vOperations = COperationFactory.getRegisteredOperations();
		
		setModal(true);
		setResizable(false);
		setTitle("Executar Opera��o");
		setBounds(100, 100, 500, 370);
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation((screenSize.width -  getSize().width) / 2, (screenSize.height - getSize().height) / 2);		

		m_pTabbedPane = new JTabbedPane();
		getContentPane().add(m_pTabbedPane, BorderLayout.CENTER);

		final JPanel areaOperation = new JPanel();
		areaOperation.setLayout(null);
		m_pTabbedPane.addTab("Opera��o", null, areaOperation, null);

		final JLabel label = new JLabel();
		label.setText("Opera��es Dispon�veis:");
		label.setBounds(10, 10, 139, 16);
		areaOperation.add(label);

		final JLabel label_1 = new JLabel();
		label_1.setText("Descri��o:");
		label_1.setBounds(10, 32, 64, 16);
		areaOperation.add(label_1);
		
		m_edDescription = new JTextPane();
		m_edDescription.setBorder(new LineBorder(Color.black, 1, false));
		m_edDescription.setEditable(false);
		m_edDescription.setBounds(10, 53, 469, 169);
		areaOperation.add(m_edDescription);

		m_edParams = new JTextField();
		
		m_cbOperations = new JComboBox();
		m_cbOperations.setBounds(148, 10, 331, 24);
		areaOperation.add(m_cbOperations);
		m_cbOperations.addActionListener(this);
	
		final JPanel areaScript = new JPanel();
		areaScript.setLayout(null);
		m_pTabbedPane.addTab("Roteiro", null, areaScript, null);

		final JScrollPane scrPaneLeft = new JScrollPane();
		scrPaneLeft.setBounds(10, 32, 142, 104);
		areaScript.add(scrPaneLeft);

		m_lsLeft = new JList(new DefaultListModel());
		scrPaneLeft.setViewportView(m_lsLeft);
		m_lsLeft.setBorder(new LineBorder(Color.black, 1, false));
		m_lsLeft.addListSelectionListener(this);

		final JScrollPane scrPaneRight = new JScrollPane();
		scrPaneRight.setBounds(185, 34, 142, 102);
		areaScript.add(scrPaneRight);

		m_lsRight = new JList(new DefaultListModel());
		scrPaneRight.setViewportView(m_lsRight);
		m_lsRight.setBorder(new LineBorder(Color.black, 1, false));
		m_lsRight.addListSelectionListener(this);		
		
		final JLabel label_3 = new JLabel();
		label_3.setText("Opera��es Dispon�veis:");
		label_3.setBounds(10, 10, 142, 16);
		areaScript.add(label_3);
	
		final JButton btAdd = new JButton();
		btAdd.setAction(m_acAdd);
		btAdd.setBounds(158, 34, 21, 21);
		areaScript.add(btAdd);

		final JButton btDel = new JButton();
		btDel.setAction(m_acDel);
		btDel.setBounds(158, 61, 21, 21);
		areaScript.add(btDel);

		final JButton btAddAll = new JButton();
		btAddAll.setAction(m_acAddAll);
		btAddAll.setBounds(158, 88, 21, 21);
		areaScript.add(btAddAll);

		final JButton btDelAll = new JButton();
		btDelAll.setAction(m_acDelAll);
		btDelAll.setBounds(158, 115, 21, 21);
		areaScript.add(btDelAll);

		final JButton btToUpp = new JButton();
		btToUpp.setAction(m_acUp);
		btToUpp.setBounds(333, 34, 21, 21);
		areaScript.add(btToUpp);

		final JButton btToDown = new JButton();
		btToDown.setAction(m_acDown);
		btToDown.setBounds(333, 61, 21, 21);
		areaScript.add(btToDown);

		final JButton btToTop = new JButton();
		btToTop.setAction(m_acUpAll);
		btToTop.setBounds(333, 88, 21, 21);
		areaScript.add(btToTop);

		final JButton btToBottom = new JButton();
		btToBottom.setAction(m_acDownAll);
		btToBottom.setBounds(333, 115, 21, 21);
		areaScript.add(btToBottom);

		final JLabel label_4 = new JLabel();
		label_4.setText("Descri��o:");
		label_4.setBounds(10, 142, 64, 16);
		areaScript.add(label_4);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 161, 469, 56);
		areaScript.add(scrollPane);

		m_edDesc = new JTextArea();
		m_edDesc.setWrapStyleWord(true);
		m_edDesc.setLineWrap(true);
		scrollPane.setViewportView(m_edDesc);

		final JButton carregarButton = new JButton();
		carregarButton.setAction(m_acLoad);
		carregarButton.setText("Carregar...");
		carregarButton.setBounds(374, 111, 105, 25);
		areaScript.add(carregarButton);

		final JButton salvarButton = new JButton();
		salvarButton.setAction(m_acSave);
		salvarButton.setText("Salvar...");
		salvarButton.setBounds(374, 80, 105, 25);
		areaScript.add(salvarButton);

		final JLabel label_5 = new JLabel();
		label_5.setText("Opera��es p/ Execu��o:");
		label_5.setBounds(185, 10, 142, 16);
		areaScript.add(label_5);

		final JLabel label_6 = new JLabel();
		label_6.setText("Num. Itera��es:");
		label_6.setBounds(359, 10, 102, 16);
		areaScript.add(label_6);

		m_edIter = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
		m_edIter.setBounds(360, 32, 119, 22);
		m_edIter.setValue(1);
		m_edIter.addChangeListener(this);
		areaScript.add(m_edIter);

		final JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		getContentPane().add(southPanel, BorderLayout.SOUTH);

		final JPanel statusBar = new JPanel();
		statusBar.setBorder(new LineBorder(Color.black, 1, false));
		southPanel.add(statusBar, BorderLayout.SOUTH);
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setVgap(0);
		flowLayout.setHgap(10);
		flowLayout.setAlignment(FlowLayout.LEFT);
		statusBar.setLayout(flowLayout);
		statusBar.setPreferredSize(new Dimension(0, 20));

		final JLabel noImagesSelecLabel = new JLabel();
		noImagesSelecLabel.setText("N�mero de Imagens Selecionadas:");
		statusBar.add(noImagesSelecLabel);

		m_lblNumImgs = new JLabel();	
		m_lblNumImgs.setText(String.valueOf(m_vThumbs.size()));
		statusBar.add(m_lblNumImgs);

		final JPanel commonPanel = new JPanel();
		commonPanel.setPreferredSize(new Dimension(0, 65));
		final FlowLayout flowLayout_1 = new FlowLayout();
		flowLayout_1.setAlignment(FlowLayout.RIGHT);
		commonPanel.setLayout(flowLayout_1);
		southPanel.add(commonPanel, BorderLayout.NORTH);
		
		final JLabel label_2 = new JLabel();
		commonPanel.add(label_2);
		label_2.setToolTipText("Formato: nome1=valor1,nome2=valor2,etc");
		label_2.setText("Par�metros:");
		
		commonPanel.add(m_edParams);
		m_edParams.setPreferredSize(new Dimension(400, 25));
		m_edParams.setToolTipText("Formato: nome1=valor1,nome2=valor2,etc");

		final JButton executarButton = new JButton();
		commonPanel.add(executarButton);
		executarButton.setName("run");
		executarButton.setText("Executar");
		executarButton.addActionListener(this);
		
		final JButton cancelarButton = new JButton();
		commonPanel.add(cancelarButton);
		cancelarButton.setName("cancel");
		cancelarButton.setText("Cancelar");
		cancelarButton.addActionListener(this);
		
		for(int i = 0; i < m_vOperations.size(); i++)
		{
			String sName = m_vOperations.get(i).getName();
			m_cbOperations.addItem(sName);
			((DefaultListModel) m_lsLeft.getModel()).addElement(sName);
		}
		evaluateActions();		
		//
	}

	/**
	 * M�todo protegido utilizado para desmembrar os par�metros descritos pelo usu�rio, que devem
	 * ser informados separados pelo caractere de ponto-e-v�rgula (;).
	 * @param sParams Texto com todos os par�metros identificados por nome e valor separados por um sinal de
	 * igual (=) e separados dos demais par�metros por um caractere de ponto-e-v�rgula (;). Por exemplo:
	 * param1=valor1;param2=valor2;paramN=valorN
	 * @return Retorna um objeto Properties do Java contendo os par�metros devidamente desmembrados e prontos
	 * para serem utilizados em uma opera��o definida no sistema Narciso.
	 */
	protected Properties parseParams(String sParams)
	{
		int iPos, iEqPos;
		String sKey, sValue, sParam;
		Properties pProps = new Properties();
		
		while((iPos = sParams.indexOf(',')) >= 0)
		{
			sParam = sParams.substring(0, iPos);
			
			iEqPos = sParam.indexOf('=');
			if(iEqPos >= 0)
			{
				sKey = sParam.substring(0, iEqPos);
				sValue = sParam.substring(iEqPos+1);
			}
			else
			{
				sKey = sParam;
				sValue = "";
			}
			pProps.put(sKey, sValue);
			
			sParams = sParams.substring(iPos+1);
		}
		
		if(sParams.length() > 0)
		{
			iEqPos = sParams.indexOf('=');
			if(iEqPos >= 0)
			{
				sKey = sParams.substring(0, iEqPos);
				sValue = sParams.substring(iEqPos+1);
			}
			else
			{
				sKey = sParams;
				sValue = "";
			}
			pProps.put(sKey, sValue);
		}
		
		return pProps;
	}
	
	/**
	 * M�todo protegido utilizado para executar uma opera��o registrada com o nome e texto de par�metros
	 * informados. � utilizado pela janela quando o usu�rio executa uma opera��o simples. 
	 * @param sName Nome da opera��o a ser executada.
	 * @param sParams Texto com os par�metros e seus respectivos valores. Segue o formato esperado pelo m�todo parseParams. 
	 */
	protected void executeOperation(String sName, String sParams)
	{
		try
		{
			CThumbnailWindow pWindow = CWindowManager.getThumbnailWindow();
			COperation pOper = COperationFactory.getOperation(sName);
			if(pOper != null)
			{
				int i;
				Properties pParams = parseParams(sParams);
				Vector<CThumbnail> vThumbs = pWindow.getMarkedThumbs();
				Vector<Object> vResult, vSource = new Vector<Object>();
				
				for(i = 0; i < vThumbs.size(); i++)
				{
					CImage pImage = vThumbs.get(i).getImage();
					vSource.add((Object) pImage);
				}
					
				vResult = pOper.execute(vSource, pParams);
				
				if(vResult != null)
				{
					for(i = 0; i < vResult.size(); i++)
					{
						CImage pImage = (CImage) vResult.get(i);
						pWindow.addImage(pImage);
					}
				}
				else
				{
					int iError = Integer.parseInt((String) pParams.get("error"));
					String sMsg = CErrors.getErrorDescription(iError);
					JOptionPane.showMessageDialog(null, "Erro na execu��o da opera��o [" + sName + "].\nDetalhes do erro:\nC�digo [" + iError + "]\nMensagem [" + sMsg + "]", "Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}		
	}
	
	/**
	 * M�todo protegido utilizado para executar um roteiro criado pelo usu�rio. � utilizado pela janela quando o usu�rio executa um roteiro de opera��es. 
	 * @param aOper Matriz bidimensional com os nomes das opera��es selecionadas e seus respectivos n�meros de itera��es.
	 * @param sParams Texto com os par�metros e seus respectivos valores. Segue o formato esperado pelo m�todo parseParams. 
	 */
	protected void executeMacroOperation(Object aOper[][], String sParams)
	{
		try
		{
			COperation pOper;
			CMacroOperation pMacroOper = new CMacroOperation("RunMacroOperation", m_edDesc.getText());
			int iIter, i;
			String sName;
			
			for(i = 0; i < aOper.length; i++)
			{
				sName = (String) aOper[i][0];
				iIter = (Integer) aOper[i][1];
				pOper = COperationFactory.getOperation(sName);
				if(pOper != null)
					pMacroOper.addOperation(pOper, iIter);
				else
				{
					JOptionPane.showMessageDialog(null, "Ocorreu um erro inesperado: a opera��o " + sName + " n�o est� devidamente registrada no Narciso e n�o pode ser executada!" , "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			CThumbnailWindow pWindow = CWindowManager.getThumbnailWindow();
			
			Properties pParams = parseParams(sParams);
			Vector<CThumbnail> vThumbs = pWindow.getMarkedThumbs();
			Vector<Object> vResult, vSource = new Vector<Object>();
				
			for(i = 0; i < vThumbs.size(); i++)
			{
				CImage pImage = vThumbs.get(i).getImage();
				vSource.add((Object) pImage);
			}
					
			vResult = pMacroOper.execute(vSource, pParams);
				
			if(vResult != null)
			{
				for(i = 0; i < vResult.size(); i++)
				{
					CImage pImage = (CImage) vResult.get(i);
					pWindow.addImage(pImage);
				}
			}
			else
			{
				int iError = Integer.parseInt((String) pParams.get("error"));
				String sMsg = CErrors.getErrorDescription(iError);
				JOptionPane.showMessageDialog(null, "Erro na execu��o da opera��o em roteiro.\nDetalhes do erro:\nC�digo [" + iError + "]\nMensagem [" + sMsg + "]", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}		
	}
	
	/**
	 * M�todo utilizado para capturar a execu��o das a��es definidas nos bot�es dispon�veis na janela.
	 * @param e Objeto ActionEvent com o evento ocorrido.
	 */
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JComboBox)
		{
			JComboBox pCombo = (JComboBox) e.getSource();
	        String sName = (String) pCombo.getSelectedItem();
	        COperation pOper = COperationFactory.getOperation(sName);
	        String sParamEx = COperationFactory.getParamExample(sName);
	        if(pOper != null)
	        {
	        	m_edDescription.setText(pOper.getDescription());
	        	m_edParams.setText(sParamEx);
	        }
	        else
	        {
	        	m_edDescription.setText("Erro: opera��o (" + sName + ") inv�lida!");
	        	m_edParams.setText("");
	        }
		}
		else if(e.getSource() instanceof JButton)
		{
			JButton pButton = (JButton) e.getSource();
			if(pButton.getName().equals("cancel"))
				dispose();
			else if(pButton.getName().equals("run"))
			{
				String sParams = m_edParams.getText();
				
				if(m_pTabbedPane.getSelectedIndex() == 0)
				{
					String sName = (String) m_cbOperations.getSelectedItem();
					executeOperation(sName, sParams);
				}
				else
				{
					int iSize = m_lsRight.getModel().getSize();
					Object aOper[][] = new Object[iSize][2];
					for(int i = 0; i < iSize; i++)
					{
						String sName = (String) m_lsRight.getModel().getElementAt(i);
						int iIter = m_vIteractions.get(sName);
						aOper[i][0] = sName;
						aOper[i][1] = iIter;
						if((Integer) aOper[i][1] <= 0)
							aOper[i][1] = 1;
					}
					executeMacroOperation(aOper, sParams);
				}
				dispose();
			}
		}
	}

	/**
	 * M�todo utilizado para for�ar uma reavalia��o dos objetos da janela quando eventos s�o
	 * ocorridos. � utilizado, por exemplo, para habilitar ou desabilitar bot�es quando uma 
	 * opera��o � selecionada.
	 */
	private void evaluateActions()
	{
		m_acAdd.setEnabled(m_lsLeft.getSelectedValues().length > 0);
		m_acAddAll.setEnabled(m_lsLeft.getModel().getSize() != 0);
		m_acDel.setEnabled(m_lsRight.getSelectedValues().length > 0);
		m_acDelAll.setEnabled(m_lsRight.getModel().getSize() != 0);
		
		m_acUp.setEnabled(m_lsRight.getSelectedIndex() > 0 && m_lsRight.getSelectedValues().length == 1);
		m_acUpAll.setEnabled(m_lsRight.getSelectedIndex() > 0 && m_lsRight.getSelectedValues().length == 1);
		m_acDown.setEnabled(m_lsRight.getSelectedIndex() != -1 && m_lsRight.getSelectedIndex() < m_lsRight.getModel().getSize() - 1 && m_lsRight.getSelectedValues().length == 1);
		m_acDownAll.setEnabled(m_lsRight.getSelectedIndex() != -1 && m_lsRight.getSelectedIndex() < m_lsRight.getModel().getSize() - 1 && m_lsRight.getSelectedValues().length == 1);
		
		m_edIter.setEnabled(m_lsRight.getSelectedIndex() != -1 && m_lsRight.getSelectedValues().length == 1);
		
		m_acSave.setEnabled(m_lsRight.getModel().getSize() > 0 && m_edDesc.getText().length() > 0);
	}

	/**
	 * M�todo utilizado para capturar o evento de sele��o de opera��es na lista de opera��es a serem executadas
	 * no roteiro criado. Serve para atualizar o valor do campo itera��es conforme a sele��o da opera��o � alterada.
	 * @param e Objeto ListSelectionEvent com o evento ocorrido.
	 */
	public void valueChanged(ListSelectionEvent e)
	{
		if(m_lsRight.getSelectedIndex() != -1)
		{
			String sName = (String) m_lsRight.getSelectedValue();
			int iIter = m_vIteractions.get(sName);
			if(iIter <= 0)
			{
				m_vIteractions.put(sName, 1);
				m_edIter.setValue(1);
			}
			else
				m_edIter.setValue(iIter);
		}
		evaluateActions();
	}
	
	/**
	 * M�todo utilizado para capturar o evento de incremento/decremento do campo itera��es. Serve para
	 * atualizar o valor do n�mero de itera��es de acordo com a opera��o selecionada.
	 * @param e Objeto ChangeEvent com o evento ocorrido.
	 */
	public void stateChanged(ChangeEvent e)
	{
		int iValue = (Integer) m_edIter.getValue();
		String sName = (String) m_lsRight.getSelectedValue();
		m_vIteractions.remove(sName);
		m_vIteractions.put(sName, iValue);
	}
	
	/**
	 * Inner-classe utilizada para implementar a a��o de adi��o de uma opera��o selecionada no roteiro.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */

	private class CAddAction extends AbstractAction
	{
		/**
		 * Construtor da classe.
		 */
		public CAddAction()
		{
			super("");
			putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/Forward16.gif")));
			putValue(Action.SHORT_DESCRIPTION, "Adiciona a(s) opera��o(�es) selecionada(s) para execu��o em roteiro");
		}

		/**
		 * M�todo utilizado para capturar o evento de execu��o da a��o.
		 * @param Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			for(int i = 0; i < m_lsLeft.getModel().getSize(); i++)
				if(m_lsLeft.isSelectedIndex(i))
				{
					String sName = (String) m_lsLeft.getModel().getElementAt(i);
					((DefaultListModel) m_lsRight.getModel()).addElement(sName);
					m_vIteractions.put(sName, 1);
				}
			for(int i = m_lsLeft.getModel().getSize() -1; i >=0; i--)
				if(m_lsLeft.isSelectedIndex(i))
					((DefaultListModel) m_lsLeft.getModel()).remove(i);
			evaluateActions();
		}		
	}

	/**
	 * Inner-classe utilizada para implementar a a��o de adi��o de todas as opera��es dispon�veis ao roteiro.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */

	private class CAddAllAction extends AbstractAction
	{
		/**
		 * Cosntrutor da classe.
		 */
		public CAddAllAction()
		{
			super("");
			putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/TwoForward16.gif")));
			putValue(Action.SHORT_DESCRIPTION, "Adiciona todas as opera��es dispon�veis para execu��o em roteiro");
		}
		
		/**
		 * M�todo utilizado para capturar o evento de execu��o da a��o.
		 * @param Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			for(int i = 0; i < m_lsLeft.getModel().getSize(); i++)
			{
				String sName = (String) m_lsLeft.getModel().getElementAt(i);
				((DefaultListModel) m_lsRight.getModel()).addElement(sName);
				m_vIteractions.put(sName, 1);
			}
			((DefaultListModel) m_lsLeft.getModel()).removeAllElements();
			evaluateActions();
		}		
	}

	/**
	 * Inner-classe utilizada para implementar a a��o de remo��o da opera��o selecionada do roteiro.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */
	
	private class CDelAction extends AbstractAction
	{
		/**
		 * Cosntrutor da classe.
		 */
		public CDelAction()
		{
			super("");
			putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/Back16.gif")));
			putValue(Action.SHORT_DESCRIPTION, "Remove a(s) opera��o(�es) selecionada(s) da execu��o em roteiro");
		}
		
		/**
		 * M�todo utilizado para capturar o evento de execu��o da a��o.
		 * @param Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			for(int i = 0; i < m_lsRight.getModel().getSize(); i++)
				if(m_lsRight.isSelectedIndex(i))
				{
					String sName = (String) m_lsRight.getModel().getElementAt(i);
					((DefaultListModel) m_lsLeft.getModel()).addElement(sName);
					m_vIteractions.remove(sName);
				}
			for(int i = m_lsRight.getModel().getSize() -1; i >=0; i--)
				if(m_lsRight.isSelectedIndex(i))
					((DefaultListModel) m_lsRight.getModel()).remove(i);
			evaluateActions();
		}		
	}

	/**
	 * Inner-classe utilizada para implementar a a��o de remo��o de todas as opera��es configuradas no roteiro.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */
	
	private class CDelAllAction extends AbstractAction
	{
		/**
		 * Cosntrutor da classe.
		 */
		public CDelAllAction()
		{
			super("");
			putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/TwoBack16.gif")));
			putValue(Action.SHORT_DESCRIPTION, "Remove todas as opera��es da execu��o em roteiro");
		}

		/**
		 * M�todo utilizado para capturar o evento de execu��o da a��o.
		 * @param Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			for(int i = 0; i < m_lsRight.getModel().getSize(); i++)
			{
				String sName = (String) m_lsRight.getModel().getElementAt(i);
				((DefaultListModel) m_lsLeft.getModel()).addElement(sName);
				m_vIteractions.remove(sName);
			}
			((DefaultListModel) m_lsRight.getModel()).removeAllElements();
			evaluateActions();
		}		
	}
	
	/**
	 * Inner-classe utilizada para implementar a a��o de prioriza��o da opera��o selecionada em um n�vel de execu��o.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */

	private class CUpAction extends AbstractAction
	{
		/**
		 * Cosntrutor da classe.
		 */
		public CUpAction()
		{
			super("");
			putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/Up16.gif")));
			putValue(Action.SHORT_DESCRIPTION, "Muda a posi��o da opera��o selecionada em um n�vel para cima na execu��o em roteiro");
		}
		
		/**
		 * M�todo utilizado para capturar o evento de execu��o da a��o.
		 * @param Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			int iIdx = m_lsRight.getSelectedIndex();
			String sValue = (String) m_lsRight.getSelectedValue();
			((DefaultListModel) m_lsRight.getModel()).remove(iIdx);
			((DefaultListModel) m_lsRight.getModel()).insertElementAt((Object) sValue, iIdx-1);
			m_lsRight.setSelectedIndex(iIdx-1);
			Point p = m_lsRight.indexToLocation(iIdx-1);
			m_lsRight.scrollRectToVisible(new Rectangle(p.x, p.y, 0, 0));
			evaluateActions();
		}
	}

	/**
	 * Inner-classe utilizada para implementar a a��o de prioriza��o da opera��o selecionada ao primeiro n�vel de execu��o.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */
	
	private class CUpAllAction extends AbstractAction
	{
		/**
		 * Cosntrutor da classe.
		 */
		public CUpAllAction()
		{
			super("");
			putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/TwoUp16.gif")));
			putValue(Action.SHORT_DESCRIPTION, "Coloca a opera��o selecionada como a primeira na execu��o em roteiro");
		}
		
		/**
		 * M�todo utilizado para capturar o evento de execu��o da a��o.
		 * @param Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			int iIdx = m_lsRight.getSelectedIndex();
			String sValue = (String) m_lsRight.getSelectedValue();
			((DefaultListModel) m_lsRight.getModel()).remove(iIdx);
			((DefaultListModel) m_lsRight.getModel()).insertElementAt((Object) sValue, 0);
			m_lsRight.setSelectedIndex(0);
			Point p = m_lsRight.indexToLocation(0);
			m_lsRight.scrollRectToVisible(new Rectangle(p.x, p.y, 0, 0));			
			evaluateActions();
		}
	}

	/**
	 * Inner-classe utilizada para implementar a a��o de desprioriza��o da opera��o selecionada em um n�vel de execu��o.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */

	private class CDownAction extends AbstractAction
	{
		/**
		 * Cosntrutor da classe.
		 */
		public CDownAction()
		{
			super("");
			putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/Down16.gif")));
			putValue(Action.SHORT_DESCRIPTION, "Muda a posi��o da opera��o selecionada em um n�vel para baixo na execu��o em roteiro");
		}
		
		/**
		 * M�todo utilizado para capturar o evento de execu��o da a��o.
		 * @param Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			int iIdx = m_lsRight.getSelectedIndex();
			String sValue = (String) m_lsRight.getSelectedValue();
			((DefaultListModel) m_lsRight.getModel()).remove(iIdx);
			((DefaultListModel) m_lsRight.getModel()).insertElementAt((Object) sValue, iIdx+1);
			m_lsRight.setSelectedIndex(iIdx+1);
			Point p = m_lsRight.indexToLocation(iIdx+1);
			m_lsRight.scrollRectToVisible(new Rectangle(p.x, p.y, 0, 0));			
			evaluateActions();
		}
	}

	/**
	 * Inner-classe utilizada para implementar a a��o de desprioriza��o da opera��o selecionada ao �ltimo n�vel de execu��o.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */
	
	private class CDownAllAction extends AbstractAction
	{
		/**
		 * Cosntrutor da classe.
		 */
		public CDownAllAction()
		{
			super("");
			putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/GUI/images/TwoDown16.gif")));
			putValue(Action.SHORT_DESCRIPTION, "Coloca a opera��o selecionada como a �ltima na execu��o em roteiro");
		}

		/**
		 * M�todo utilizado para capturar o evento de execu��o da a��o.
		 * @param Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			int iIdx = m_lsRight.getSelectedIndex();
			String sValue = (String) m_lsRight.getSelectedValue();
			((DefaultListModel) m_lsRight.getModel()).remove(iIdx);
			((DefaultListModel) m_lsRight.getModel()).addElement((Object) sValue);
			m_lsRight.setSelectedIndex(m_lsRight.getModel().getSize()-1);
			Point p = m_lsRight.indexToLocation(m_lsRight.getModel().getSize()-1);
			m_lsRight.scrollRectToVisible(new Rectangle(p.x, p.y, 0, 0));			
			evaluateActions();
		}
	}

	/**
	 * Inner-classe utilizada para implementar a a��o de grava��o do roteito em um arquivo XML.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */
	
	private class CSaveAction extends AbstractAction
	{
		/**
		 * Cosntrutor da classe.
		 */
		public CSaveAction()
		{
			super("Salvar...");
			putValue(Action.SHORT_DESCRIPTION, "Salva o roteiro corrente em um arquivo para execu��o posterior");
		}
		
		/**
		 * M�todo utilizado para capturar o evento de execu��o da a��o.
		 * @param Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			String sFile;
			String sMacroName;
			JFileChooser pDlg = new JFileChooser();
			pDlg.addChoosableFileFilter(new CXMLFilter());
	        pDlg.setAcceptAllFileFilterUsed(false);
	        
			int iRet = pDlg.showDialog(null, "Salvar");
			if(iRet == JFileChooser.APPROVE_OPTION)
			{
				File pFile = pDlg.getSelectedFile();
				sFile = pFile.getPath();
				sMacroName = pFile.getName();
				if(CUtils.getExtension(pFile) == null)
					sFile = sFile + ".xml";
			}
			else
				return;
			
			COperation pOper;
			CMacroOperation pMacroOper = new CMacroOperation(sMacroName, m_edDesc.getText());
			int iIter, i;
			String sName;
			
			for(i = 0; i < m_lsRight.getModel().getSize(); i++)
			{
				sName = (String) m_lsRight.getModel().getElementAt(i);
				iIter = m_vIteractions.get(sName);
				pOper = COperationFactory.getOperation(sName);
				if(pOper != null)
					pMacroOper.addOperation(pOper, iIter);
				else
				{
					JOptionPane.showMessageDialog(null, "Ocorreu um erro inesperado: a opera��o " + sName + " n�o est� devidamente registrada no Narciso e n�o pode ser executada!" , "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			iRet = pMacroOper.saveToFile(sFile);
			if(iRet != CErrors.SUCCESS)
				JOptionPane.showMessageDialog(null, "Erro na grava��o do arquivo:\n" + CErrors.getErrorDescription(iRet) , "Erro", JOptionPane.ERROR_MESSAGE);				
		}
	}

	/**
	 * Inner-classe utilizada para implementar a a��o de carregamento do roteito a partir de um arquivo XML.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 *
	 */
	
	private class CLoadAction extends AbstractAction
	{
		/**
		 * Cosntrutor da classe.
		 */
		public CLoadAction()
		{
			super("Carregar...");
			putValue(Action.SHORT_DESCRIPTION, "Carrega um roteiro de um arquivo");
		}
		
		/**
		 * M�todo utilizado para capturar o evento de execu��o da a��o.
		 * @param Objeto ActionEvent com o evento ocorrido.
		 */
		public void actionPerformed(ActionEvent e)
		{
			String sFile;
			JFileChooser pDlg = new JFileChooser();
			pDlg.addChoosableFileFilter(new CXMLFilter());
	        pDlg.setAcceptAllFileFilterUsed(false);
	        
			int iRet = pDlg.showDialog(null, "Carregar");
			if(iRet == JFileChooser.APPROVE_OPTION)
			{
				File pFile = pDlg.getSelectedFile();
				sFile = pFile.getPath();
			}
			else
				return;
			
			CMacroOperation pMacroOper = new CMacroOperation("", "");
			iRet = pMacroOper.loadFromFile(sFile);
			if(iRet != CErrors.SUCCESS)
			{
				JOptionPane.showMessageDialog(null, "Erro no carregamento do arquivo:\n" + CErrors.getErrorDescription(iRet) , "Erro", JOptionPane.ERROR_MESSAGE);
				return;
			}
				
			if(m_lsRight.getModel().getSize() > 0)
				m_acDelAll.actionPerformed(null);
			
			int aIdx[] = new int[pMacroOper.getOperationCount()];
			int i, j, iIter;
			
			for(i = 0; i < pMacroOper.getOperationCount(); i++)
			{
				String sName = pMacroOper.getOperationByIndex(i).getName();
				for(j = 0; j < m_lsLeft.getModel().getSize(); j++)
					if(m_lsLeft.getModel().getElementAt(j).equals(sName))
						aIdx[i] = j;
			}
			
			m_lsLeft.setSelectedIndices(aIdx);
			m_acAdd.actionPerformed(null);
			
			m_edDesc.setText(pMacroOper.getDescription());

			for(i = 0; i < pMacroOper.getOperationCount(); i++)
			{
				String sName = pMacroOper.getOperationByIndex(i).getName();
				iIter = pMacroOper.getOperationIterations(sName);
				m_vIteractions.put(sName, iIter);
			}			
		}
	}
}