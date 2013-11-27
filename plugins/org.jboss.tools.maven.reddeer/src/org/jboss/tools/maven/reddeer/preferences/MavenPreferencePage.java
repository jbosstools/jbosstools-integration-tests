package org.jboss.tools.maven.reddeer.preferences;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

public class MavenPreferencePage extends PreferencePage{
	
	public MavenPreferencePage(){
		super("Maven");
	}
	
	public void updateIndexesOnStartup(boolean update){
		CheckBox cb = new CheckBox("Download repository index updates on startup");
		cb.toggle(update);
	}
}
