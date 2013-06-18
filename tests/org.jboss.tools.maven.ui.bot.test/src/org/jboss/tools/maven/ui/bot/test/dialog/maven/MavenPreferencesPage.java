package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

public class MavenPreferencesPage extends PreferencePage{
	
	public MavenPreferencesPage(){
		super("Maven");
	}
	
	public void updateIndexesOnStartup(boolean update){
		CheckBox cb = new CheckBox("Download repository index updates on startup");
		cb.toggle(update);
	}
}
