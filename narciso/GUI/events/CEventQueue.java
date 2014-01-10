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
 
package GUI.events;

import java.awt.*;
import GUI.*;

/**
 * Classe utilizada para encapsular a fila de eventos da m�quina virtual do Java, com o prop�sito de
 * for�ar automaticamente a avalia��o dos objetos da janela principal, permitindo que os bot�es, menus, e
 * outros objetos sejam habilitados ou desabilitados conforme ocorrem altera��es nos estados dos quais
 * dependem.
 * 
 * Por exemplo, � por meio dessa classe que o menu de execu��o de opera��es na janela principal do sistema
 * � habilitado quando uma ou mais imagens s�o selecionadas na janela de miniaturas, e desabilitado quando
 * n�o h� nenhuma imagem selecionada. 
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 * 
 * @see Narciso
 */

public class CEventQueue extends EventQueue 
{
	/**
	 * M�todo utilizado para capturar o evento de distribui��o dos demais eventos do sistema.
	 * Sua tarefa � simplesmente executar o m�todo verifyEvents de CMainWindow, e continuar o processamento
	 * natural dos eventos chamando o m�todo dispatchEvent implementado originalmente na classe pai.
	 * @param e Objeto AWTEvent com o evento ocorrido.
	 */
	protected void dispatchEvent(AWTEvent e)
	{
		try
		{
			CWindowManager.getMainWindow().verifyEvents();
			super.dispatchEvent(e);
		}
		catch(Exception ex)
		{
			CExceptionDialog.showException(ex);
		}
	}
}