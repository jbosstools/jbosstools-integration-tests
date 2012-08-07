package org.jboss.ide.eclipse.as.ui.bot.test.editor;

import org.jboss.tools.ui.bot.ext.SWTBotFactory;

public class ServerLaunchConfiguration {

	public String getProgramArguments(){
		return SWTBotFactory.getBot().textInGroup("Program arguments:").getText();
	}
	
	public String getVMArguments(){
		return SWTBotFactory.getBot().textInGroup("VM arguments:").getText();
	}
}
