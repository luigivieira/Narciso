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
import java.awt.Dimension;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.tree.*;
import core.images.*;
import core.info.*;

/**
 * Classe utilizada para implementar a janela de exibição de propriedades de uma imagem.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

@SuppressWarnings("serial")
public class CPropertiesDialog extends JDialog implements ActionListener
{
	/** Membro privado utilizado para armazenar o objeto de navegação em árvore da janela. */
	private JTree m_pTree;

	/**
	 * Construtor da classe.
	 * @param pImage Objeto CImage com a imagem a ser analisada para extração de propriedades.
	 */
	public CPropertiesDialog(CImage pImage)
	{
		super();
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setName("pPropertiesDialog");
		setModal(true);
		setTitle("Propriedades");
		setBounds(100, 100, 631, 347);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setLocation((screenSize.width -  getSize().width) / 2, (screenSize.height - getSize().height) / 2);		

		final JPanel panel = new JPanel();
		final FlowLayout flowLayout = new FlowLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		panel.setLayout(flowLayout);
		panel.setPreferredSize(new Dimension(0, 35));
		getContentPane().add(panel, BorderLayout.SOUTH);

		final JButton btExpand = new JButton();
		btExpand.setToolTipText("Abre todos os nós das propriedades exibidas");
		btExpand.setText("Expandir");
		btExpand.setName("expand");
		btExpand.addActionListener(this);
		panel.add(btExpand);

		final JButton btCollapse = new JButton();
		btCollapse.setToolTipText("Fecha todos os nós das propriedades exibidas");
		btCollapse.setText("Fechar");
		btCollapse.setName("collapse");
		btCollapse.addActionListener(this);
		panel.add(btCollapse);

		final JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(50, 0));
		panel.add(panel_1);

		final JButton btOk = new JButton();
		btOk.setPreferredSize(new Dimension(80, 27));
		btOk.setText("Ok");
		btOk.setName("ok");
		btOk.addActionListener(this);
		panel.add(btOk);

		final JScrollPane scrollArea = new JScrollPane();
		getContentPane().add(scrollArea, BorderLayout.CENTER);

		CPropertyBag pBag = CPropertyExtractor.getImageProperties(pImage);
		DefaultMutableTreeNode pRoot = new DefaultMutableTreeNode("Imagem");
		createTreeNodesFromPropertyBag(pBag, pRoot);
		
		m_pTree = new JTree(pRoot);
		scrollArea.setViewportView(m_pTree);
		expandAll(m_pTree, true);
		//
	}
	
	/**
	 * Método de utilização interna (privado) responsável pela atualização dos nós da árvore de propriedades
	 * com base nas propriedades extraídas da imagem.
	 * 
	 * Obs.: Esse é um método recursivo, rechamado para cada subconjunto de propriedades.
	 * 
	 * @param pBag Objeto CPropertyBag com o pacote de propriedades extraido da imagem.
	 * @param pRoot Objeto DefaultMutableTreeNode com a raíz da árvore.
	 */
	private void createTreeNodesFromPropertyBag(CPropertyBag pBag, DefaultMutableTreeNode pRoot)
	{
		DefaultMutableTreeNode pNameNode;

		pNameNode = new DefaultMutableTreeNode(pBag.getName());
		
		if(pBag.getType() == CPropertyBag.CPropertyTypeEnum.COMPOSITE)
		{
			CPropertyComposite pComp = (CPropertyComposite) pBag;
			for(int i = 0; i < pComp.getPropertyCount(); i++)
			{
				CPropertyBag pCur = pComp.getPropertyByIndex(i);
				createTreeNodesFromPropertyBag(pCur, pNameNode);
			}
		}
		else
		{
			DefaultMutableTreeNode pValueNode = new DefaultMutableTreeNode(((CPropertyItem) pBag).toString());
			pNameNode.add(pValueNode);
		}

		pRoot.add(pNameNode);
	}

	/**
	 * Método utilizado para expandir ou fechar visualmente todos os nós da árvore de propriedades.
	 * 
	 * Obs.: Esse é um método recursivo, rechamado para cada nó da árvore.
	 * 
	 * @param pTree Objeto JTree com a árvore.
	 * @param bExpand Indicação lógica se o nó deve ser expandido (true) ou fechado (false). 
	 */
	private void expandAll(JTree pTree, boolean bExpand)
	{
        TreeNode pRoot = (TreeNode) pTree.getModel().getRoot();
        expandAll(pTree, new TreePath(pRoot), bExpand);
    }
	
	/**
	 * Método utilizado para expandir ou exconder visualmente todos os nós da árvore de propriedades.
	 * 
	 * Obs.: Esse é um método recursivo, rechamado para cada nó da árvore.
	 * 
	 * @param pTree Objeto JTree com a árvore.
	 * @param pParant Objeto TreePath com o nó pai do nó em pTree.
	 * @param bExpand Indicação lógica se o nó deve ser expandido (true) ou fechado (false).
	 */
    private void expandAll(JTree pTree, TreePath pParent, boolean bExpand)
    {
        TreeNode pNode = (TreeNode) pParent.getLastPathComponent();
        if(pNode.getChildCount() >= 0)
        {
            for(Enumeration pEnum = pNode.children(); pEnum.hasMoreElements(); )
            {
                TreeNode pCur = (TreeNode) pEnum.nextElement();
                TreePath pPath = pParent.pathByAddingChild(pCur);
                expandAll(pTree, pPath, bExpand);
            }
        }
    
        if(bExpand)
            pTree.expandPath(pParent);
        else
            pTree.collapsePath(pParent);
    }
	
    /**
     * Método utilizado para capturar os eventos de clique nos botões da janela.
     * @param e Objeto ActionEvent com o evento ocorrido.
     */
	public void actionPerformed(ActionEvent e)
	{
		JButton pBtn = (JButton) e.getSource();
		if(pBtn.getName().equals("ok"))
			dispose();
		else if(pBtn.getName().equals("expand"))
			expandAll(m_pTree, true);
		else if(pBtn.getName().equals("collapse"))
		{
			expandAll(m_pTree, false);
			m_pTree.expandRow(0);
		}
	}
}