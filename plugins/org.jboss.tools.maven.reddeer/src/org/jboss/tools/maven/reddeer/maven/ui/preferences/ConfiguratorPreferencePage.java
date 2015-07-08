package org.jboss.tools.maven.reddeer.maven.ui.preferences;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;

public class ConfiguratorPreferencePage extends PreferencePage{
	
	public ConfiguratorPreferencePage(){
		super("JBoss Tools","JBoss Maven Integration");
	}
	
	public ConfigureMavenRepositoriesWizard configureRepositories(){
		ConfigureMavenRepositoriesWizard cw = new ConfigureMavenRepositoriesWizard();
		cw.open();
		return cw;
	}

}
