package org.jboss.tools.forge.ui.bot.test.util;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.forge.ui.bot.test.suite.ForgeTest;

public class ConsoleUtils {
	

	/**
	 * 
	 * @param text
	 * @param sleepTime
	 * @param timeOut
	 * @return true if text found in Forge Console
	 */
	public static boolean waitUntilTextInConsole(String text, long sleepTime, long timeOut){
	
		if(!ForgeTest.isForgeViewActive())
			return false;
		
		SWTBot viewBot = ForgeTest.getForgeViewBot();		
		String consoleText = ForgeTest.getStyledText().getText();

		long estimatedTime = 0;
		
		while ((estimatedTime < timeOut) && (!consoleText.contains(text)) ){
			viewBot.sleep(sleepTime);
			estimatedTime += sleepTime;
			consoleText = ForgeTest.getStyledText().getText();
		}
		if(consoleText.contains(text))
			return true;
		else
			return false;
		
	}
	
}
