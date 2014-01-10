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

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * Classe utilizada para implementar o gerenciador de exceções do sistema Narciso.
 * Esse gerenciador é configurado no engine central do Java logo na inicialização do sistema,
 * tornando-se responsável pela captura e tratamento de todas as exceções não tratadas. Seu objetivo
 * é gerar uma tela amigável indicando o erro para o usuário, sem deixar o sistema cair pela exceção. 
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 *
 */

public class CNarcisoExceptionHandler implements UncaughtExceptionHandler
{
	/**
	 * Método chamado automaticamente pela interface central do Java quando uma exceção não tratada é
	 * encontrada.
	 * 
	 * @param t Objeto com a Thread onde a exceção foi encontrada.
	 * @param e Objeto Throwable contendo a exceção gerada. 
	 */
	  public void uncaughtException(Thread t, Throwable e)
	  {
		  CExceptionDialog.showException((Exception) e);
	  }
}