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
 
package GUI.events;

import java.awt.*;
import GUI.*;

/**
 * Classe utilizada para encapsular a fila de eventos da máquina virtual do Java, com o propósito de
 * forçar automaticamente a avaliação dos objetos da janela principal, permitindo que os botões, menus, e
 * outros objetos sejam habilitados ou desabilitados conforme ocorrem alterações nos estados dos quais
 * dependem.
 * 
 * Por exemplo, é por meio dessa classe que o menu de execução de operações na janela principal do sistema
 * é habilitado quando uma ou mais imagens são selecionadas na janela de miniaturas, e desabilitado quando
 * não há nenhuma imagem selecionada. 
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
	 * Método utilizado para capturar o evento de distribuição dos demais eventos do sistema.
	 * Sua tarefa é simplesmente executar o método verifyEvents de CMainWindow, e continuar o processamento
	 * natural dos eventos chamando o método dispatchEvent implementado originalmente na classe pai.
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