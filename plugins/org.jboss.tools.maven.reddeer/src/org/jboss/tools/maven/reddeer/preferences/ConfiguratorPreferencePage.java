package org.jboss.tools.maven.reddeer.preferences;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.maven.reddeer.wizards.ConfigureMavenRepositoriesWizard;

public class ConfiguratorPreferencePage extends PreferencePage{
	
	public ConfiguratorPreferencePage(){
		super("JBoss Tools","JBoss Maven Integration");
	}
	
	public ConfigureMavenRepositoriesWizard configureRepositories(){
		new PushButton("Configure Maven Repositories...");
		return new ConfigureMavenRepositoriesWizard();
	}

}
