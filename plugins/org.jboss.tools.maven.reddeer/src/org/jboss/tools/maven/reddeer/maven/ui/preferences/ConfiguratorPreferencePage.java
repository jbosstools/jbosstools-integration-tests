package org.jboss.tools.maven.reddeer.maven.ui.preferences;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;

public class ConfiguratorPreferencePage extends WorkbenchPreferencePage{
	
	public ConfiguratorPreferencePage(){
		super("JBoss Tools","JBoss Maven Integration");
	}
	
	public ConfigureMavenRepositoriesWizard configureRepositories(){
		new PushButton("Configure Maven Repositories...").click();
		return new ConfigureMavenRepositoriesWizard();
	}

}
