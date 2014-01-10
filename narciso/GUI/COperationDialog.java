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
 * Classe utilizada para implementar a janela de diálogo para a execução de operações e roteiros.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public class COperationDialog extends JDialog implements ActionListener, ListSelectionListener, ChangeListener
{
	/** Membro privado utilizado para conter o objeto de exibição da descrição da operação. */
	private JTextArea m_edDesc;
	
	/** Membro privado utilizado para conter a lista de operações para execução em roteiro. */
	private JList m_lsRight;
	
	/** Membro privado utilizado para conter a lista de operações disponíveis para execução de um roteiro. */
	private JList m_lsLeft;
	
	/** Membro privado utilizado para conter o objeto de exibição do texto indicativo do número de imagens selecionadas na janela de miniaturas. */
	private JLabel m_lblNumImgs = null;
	
	/** Membro privado utilizado para conter o objeto de exibição da descrição do roteiro. */
	private JTextPane m_edDescription = null;
	
	/** Membro privado utilizado para conter o objeto de exibição/edição dos parâmetros de uma operação ou roteiro. */
	private JTextField m_edParams = null;

	/** Membro privado utilizado para conter a lista de operações disponíveis para execução individual. */
	private JComboBox m_cbOperations = null;

	/** Membro privado utilizado para conter o objeto de edição do número de iterações de uma operação para execução em um roteiro. */
	private JSpinner m_edIter = null;
	
	/** Membro privado utilizado para armazenar os objetos de imagem selecionados na janela de miniaturas (com as imagens a serem utilizadas como parâmetros de operações ou roteiros). */
	private Vector<CThumbnail> m_vThumbs = null;

	/** Membro privado utilizado para conter os objetos das operações registradas no sistema. */
	private Vector<COperation> m_vOperations = null;
	
	/** Membro privado utilizado para conter a ação de adicionar operação selecionada para execução em roteiro. */
	private CAddAction m_acAdd;
	
	/** Membro privado utilizado para conter a ação de adicionar todas as operações disponíveis para execução em roteiro. */
	private CAddAllAction m_acAddAll;

	/** Membro privado utilizado para conter a ação de remover a operação selecionada da execução em roteiro. */
	private CDelAction m_acDel;

	/** Membro privado utilizado para conter a ação de remover todas as operações contindas na execução do roteiro. */
	private CDelAllAction m_acDelAll;
	
	/** Membro privado utilizado para conter a ação de priorizar (subir um nível de execução) a operação selecionada no roteiro. */
	private CUpAction m_acUp;
	
	/** Membro privado utilizado para conter a ação de priorizar totalmente (elevar ao primeiro nível de execução) a operação selecionada no roteiro. */
	private CUpAllAction m_acUpAll;
	
	/** Membro privado utilizado para conter a ação de despriorizar (descer um nível de execução) a operação selecionada no roteiro. */
	private CDownAction m_acDown;
	
	/** Membro privado utilizado para conter a ação de despriorizar totalmente (rebaixar ao último nível de execução) a operação selecionada no roteiro. */
	private CDownAllAction m_acDownAll;
	
	/** Membro privado utilizado para conter a ação gravar o roteiro criado em um arquivo XML para posterior execução. */
	private CSaveAction m_acSave;
	
	/** Membro privado utilizado para conter a ação carregar um roteiro previamente gravado em um arquivo XML. */
	private CLoadAction m_acLoad;
	
	/** Membro privado utilizado para conter a lista de iterações configuradas para as operações disponíveis no roteiro. */
	private Map<String, Integer> m_vIteractions;
	
	/** Membro privado utilizado para conter as pastas (folders) delimitadoras da execução de uma única operação ou de um roteiro, na janela de execução. */
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
		setTitle("Executar Operação");
		setBounds(100, 100, 500, 370);
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation((screenSize.width -  getSize().width) / 2, (screenSize.height - getSize().height) / 2);		

		m_pTabbedPane = new JTabbedPane();
		getContentPane().add(m_pTabbedPane, BorderLayout.CENTER);

		final JPanel areaOperation = new JPanel();
		areaOperation.setLayout(null);
		m_pTabbedPane.addTab("Operação", null, areaOperation, null);

		final JLabel label = new JLabel();
		label.setText("Operações Disponíveis:");
		label.setBounds(10, 10, 139, 16);
		areaOperation.add(label);

		final JLabel label_1 = new JLabel();
		label_1.setText("Descrição:");
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
		label_3.setText("Operações Disponíveis:");
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
		label_4.setText("Descrição:");
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
		label_5.setText("Operações p/ Execução:");
		label_5.setBounds(185, 10, 142, 16);
		areaScript.add(label_5);

		final JLabel label_6 = new JLabel();
		label_6.setText("Num. Iterações:");
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
		noImagesSelecLabel.setText("Número de Imagens Selecionadas:");
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
		label_2.setText("Parâmetros:");
		
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
	 * Método protegido utilizado para desmembrar os parâmetros descritos pelo usuário, que devem
	 * ser informados separados pelo caractere de ponto-e-vírgula (;).
	 * @param sParams Texto com todos os parâmetros identificados por nome e valor separados por um sinal de
	 * igual (=) e separados dos demais parâmetros por um caractere de ponto-e-vírgula (;). Por exemplo:
	 * param1=valor1;param2=valor2;paramN=valorN
	 * @return Retorna um objeto Properties do Java contendo os parâmetros devidamente desmembrados e prontos
	 * para serem utilizados em uma operação definida no sistema Narciso.
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
	 * Método protegido utilizado para executar uma operação registrada com o nome e texto de parâmetros
	 * informados. É utilizado pela janela quando o usuário executa uma operação simples. 
	 * @param sName Nome da operação a ser executada.
	 * @param sParams Texto com os parâmetros e seus respectivos valores. Segue o formato esperado pelo método parseParams. 
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
					JOptionPane.showMessageDialog(null, "Erro na execução da operação [" + sName + "].\nDetalhes do erro:\nCódigo [" + iError + "]\nMensagem [" + sMsg + "]", "Erro", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}		
	}
	
	/**
	 * Método protegido utilizado para executar um roteiro criado pelo usuário. É utilizado pela janela quando o usuário executa um roteiro de operações. 
	 * @param aOper Matriz bidimensional com os nomes das operações selecionadas e seus respectivos números de iterações.
	 * @param sParams Texto com os parâmetros e seus respectivos valores. Segue o formato esperado pelo método parseParams. 
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
					JOptionPane.showMessageDialog(null, "Ocorreu um erro inesperado: a operação " + sName + " não está devidamente registrada no Narciso e não pode ser executada!" , "Erro", JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(null, "Erro na execução da operação em roteiro.\nDetalhes do erro:\nCódigo [" + iError + "]\nMensagem [" + sMsg + "]", "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}		
	}
	
	/**
	 * Método utilizado para capturar a execução das ações definidas nos botões disponíveis na janela.
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
	        	m_edDescription.setText("Erro: operação (" + sName + ") inválida!");
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
	 * Método utilizado para forçar uma reavaliação dos objetos da janela quando eventos são
	 * ocorridos. É utilizado, por exemplo, para habilitar ou desabilitar botões quando uma 
	 * operação é selecionada.
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
	 * Método utilizado para capturar o evento de seleção de operações na lista de operações a serem executadas
	 * no roteiro criado. Serve para atualizar o valor do campo iterações conforme a seleção da operação é alterada.
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
	 * Método utilizado para capturar o evento de incremento/decremento do campo iterações. Serve para
	 * atualizar o valor do número de iterações de acordo com a operação selecionada.
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
	 * Inner-classe utilizada para implementar a ação de adição de uma operação selecionada no roteiro.
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
			putValue(Action.SHORT_DESCRIPTION, "Adiciona a(s) operação(ões) selecionada(s) para execução em roteiro");
		}

		/**
		 * Método utilizado para capturar o evento de execução da ação.
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
	 * Inner-classe utilizada para implementar a ação de adição de todas as operações disponíveis ao roteiro.
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
			putValue(Action.SHORT_DESCRIPTION, "Adiciona todas as operações disponíveis para execução em roteiro");
		}
		
		/**
		 * Método utilizado para capturar o evento de execução da ação.
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
	 * Inner-classe utilizada para implementar a ação de remoção da operação selecionada do roteiro.
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
			putValue(Action.SHORT_DESCRIPTION, "Remove a(s) operação(ões) selecionada(s) da execução em roteiro");
		}
		
		/**
		 * Método utilizado para capturar o evento de execução da ação.
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
	 * Inner-classe utilizada para implementar a ação de remoção de todas as operações configuradas no roteiro.
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
			putValue(Action.SHORT_DESCRIPTION, "Remove todas as operações da execução em roteiro");
		}

		/**
		 * Método utilizado para capturar o evento de execução da ação.
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
	 * Inner-classe utilizada para implementar a ação de priorização da operação selecionada em um nível de execução.
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
			putValue(Action.SHORT_DESCRIPTION, "Muda a posição da operação selecionada em um nível para cima na execução em roteiro");
		}
		
		/**
		 * Método utilizado para capturar o evento de execução da ação.
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
	 * Inner-classe utilizada para implementar a ação de priorização da operação selecionada ao primeiro nível de execução.
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
			putValue(Action.SHORT_DESCRIPTION, "Coloca a operação selecionada como a primeira na execução em roteiro");
		}
		
		/**
		 * Método utilizado para capturar o evento de execução da ação.
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
	 * Inner-classe utilizada para implementar a ação de despriorização da operação selecionada em um nível de execução.
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
			putValue(Action.SHORT_DESCRIPTION, "Muda a posição da operação selecionada em um nível para baixo na execução em roteiro");
		}
		
		/**
		 * Método utilizado para capturar o evento de execução da ação.
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
	 * Inner-classe utilizada para implementar a ação de despriorização da operação selecionada ao último nível de execução.
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
			putValue(Action.SHORT_DESCRIPTION, "Coloca a operação selecionada como a última na execução em roteiro");
		}

		/**
		 * Método utilizado para capturar o evento de execução da ação.
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
	 * Inner-classe utilizada para implementar a ação de gravação do roteito em um arquivo XML.
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
			putValue(Action.SHORT_DESCRIPTION, "Salva o roteiro corrente em um arquivo para execução posterior");
		}
		
		/**
		 * Método utilizado para capturar o evento de execução da ação.
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
					JOptionPane.showMessageDialog(null, "Ocorreu um erro inesperado: a operação " + sName + " não está devidamente registrada no Narciso e não pode ser executada!" , "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			
			iRet = pMacroOper.saveToFile(sFile);
			if(iRet != CErrors.SUCCESS)
				JOptionPane.showMessageDialog(null, "Erro na gravação do arquivo:\n" + CErrors.getErrorDescription(iRet) , "Erro", JOptionPane.ERROR_MESSAGE);				
		}
	}

	/**
	 * Inner-classe utilizada para implementar a ação de carregamento do roteito a partir de um arquivo XML.
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
		 * Método utilizado para capturar o evento de execução da ação.
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