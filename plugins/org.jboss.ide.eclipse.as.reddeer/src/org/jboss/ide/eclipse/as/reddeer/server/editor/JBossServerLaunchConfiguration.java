package org.jboss.ide.eclipse.as.reddeer.server.editor;

import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;

/**
 * Represents a server editor's launch configuration  with entries specific 
 * for JBoss servers {@link JBossServer}
 * @author Lucia Jelinkova
 *
 */
public class JBossServerLaunchConfiguration {

	public String getProgramArguments(){
//		return SWTBotFactory.getBot().textInGroup("Program arguments:").getText();
		throw new UnsupportedOperationException();
	}
	
	public String getVMArguments(){
//		return SWTBotFactory.getBot().textInGroup("VM arguments:").getText();
		throw new UnsupportedOperationException();
	}
}
