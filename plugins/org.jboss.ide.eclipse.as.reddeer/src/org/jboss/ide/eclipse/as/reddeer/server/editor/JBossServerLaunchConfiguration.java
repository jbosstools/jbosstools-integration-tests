package org.jboss.ide.eclipse.as.reddeer.server.editor;

import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * Represents a server editor's launch configuration  with entries specific 
 * for JBoss servers {@link JBossServer}
 * @author Lucia Jelinkova
 *
 */
public class JBossServerLaunchConfiguration {

	public String getProgramArguments(){
		return new DefaultText(new DefaultGroup("Program arguments:")).getText();
	}
	
	public void setProgramArguments(String arguments){
		new DefaultText(new DefaultGroup("Program arguments:")).setText(arguments);
	}
	
	public String getVMArguments(){
		return new DefaultText(new DefaultGroup("VM arguments:")).getText();
	}
	
	public void setVMArguments(String arguments){
		new DefaultText(new DefaultGroup("VM arguments:")).setText(arguments);
	}
}
