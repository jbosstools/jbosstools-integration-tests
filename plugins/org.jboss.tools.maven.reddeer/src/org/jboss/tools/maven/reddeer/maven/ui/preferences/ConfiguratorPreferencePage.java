package org.jboss.tools.maven.reddeer.maven.ui.preferences;

import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;

public class ConfiguratorPreferencePage extends WorkbenchPreferencePage{
	
	public ConfiguratorPreferencePage(){
		super("JBoss Tools","JBoss Maven Integration");
	}
	
	public ConfigureMavenRepositoriesWizard configureRepositories(){
		ConfigureMavenRepositoriesWizard cw = new ConfigureMavenRepositoriesWizard();
		cw.open();
		return cw;
	}

}
