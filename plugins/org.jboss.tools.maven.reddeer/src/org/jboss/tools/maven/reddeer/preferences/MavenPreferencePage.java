package org.jboss.tools.maven.reddeer.preferences;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

public class MavenPreferencePage extends WorkbenchPreferencePage{
	
	public MavenPreferencePage(){
		super("Maven");
	}
	
	public void updateIndexesOnStartup(boolean update){
		CheckBox cb = new CheckBox("Download repository index updates on startup");
		cb.toggle(update);
	}
}
