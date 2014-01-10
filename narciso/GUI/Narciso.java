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
import GUI.events.*;

/**
 * Esta � a classe principal do sistema Narciso, respons�vel pela inicializa��o do sistema.
 * 
 * @author Kiran Mantripragada
 * @author Luiz Carlos Vieira
 * @version 1.0
 * 
 * @see CMainWindow
 * @see CSplash
 */

public class Narciso
{
	/** Membro privado utilizado para armazenar o objeto de exibi��o da janela de Splash. */
	private static CSplash m_pSplash;
	
	/**
	 * M�todo principal do sistema, respons�vel por carregar toda a aplica��o.
	 * 
	 * @param args - Matriz de strings com a lista de argumentos da linha de comando.
	 * N�o � utilizado na vers�o atual do sistema.
	 */
	public static void main(String args[])
	{
		Thread.setDefaultUncaughtExceptionHandler(new CNarcisoExceptionHandler());
		showSplashScreen();
		CMainWindow pWindow = new CMainWindow();
		CWindowManager.setMainWindow(pWindow);
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new CEventQueue());
		pWindow.start();
		EventQueue.invokeLater(new SplashScreenCloser());
	}
	
	/**
	 * M�todo utilizado para exibir a janela de Splash durante o carregamento do sistema.
	 */
	private static void showSplashScreen()
	{
		m_pSplash = new CSplash("images/splash.jpg");
		m_pSplash.splash();
	}

	/**
	 * Inner-class utilizada para controlar o encerramento da janela de Splash assim que o sistema
	 * for totalmente carregado.
	 * 
	 * @author Kiran Mantripragada
	 * @author Luiz Carlos Vieira
	 * @version 1.0
	 */
	
	private static final class SplashScreenCloser implements Runnable
	{
		/**
		 * Implementa o m�todo a ser executado assim que a sinaliza��o de t�rmino do carregamento do sistema
		 * for ativada.
		 */
		public void run()
		{
			m_pSplash.dispose();
		}
	}	
}