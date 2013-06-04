package org.jboss.tools.forge.ui.bot.test.util;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeConsoleTestBase;

public class ConsoleUtils {
	

	/**
	 * 
	 * @param text
	 * @param sleepTime
	 * @param timeOut
	 * @return true if text found in Forge Console
	 */
	public static boolean waitUntilTextInConsole(String text, long sleepTime, long timeOut){
	
		if(!ForgeConsoleTestBase.isForgeViewActive())
			return false;
		
		SWTBot viewBot = ForgeConsoleTestBase.getForgeViewBot();		
		String consoleText = ForgeConsoleTestBase.getStyledText().getText();

		long estimatedTime = 0;
		
		while ((estimatedTime < timeOut) && (!consoleText.contains(text)) ){
			viewBot.sleep(sleepTime);
			estimatedTime += sleepTime;
			consoleText = ForgeConsoleTestBase.getStyledText().getText();
		}
		if(consoleText.contains(text))
			return true;
		else
			return false;
		
	}
	
}
