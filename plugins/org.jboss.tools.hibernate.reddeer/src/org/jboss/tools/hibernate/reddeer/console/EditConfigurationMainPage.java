package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.DefaultText;

public class EditConfigurationMainPage {

	public interface PredefinedConnection {
	    String HIBERNATE_CONFIGURED_CONNECTION = "[Hibernate configured connection]";
	    String JPA_PROJECT_CONFIGURED_CONNECTION = "[JPA Project Configured Connection]";
	}
	
	public void setProject(String project) {
		DefaultGroup g = new DefaultGroup("Project:");
		new DefaultText(g,0).setText(project);
	}
	
	/**
	 * 
	 * @param version acceptable values are "3.5","3.6" and "4.0"
	 */
	public void setHibernateVersion(String version) {
		new DefaultCombo("Hibernate Version:").setSelection(version);
	}
	
	public void setDatabaseConnection(String connection) {
		DefaultGroup g = new DefaultGroup("Database connection:");
		new DefaultCombo(g,0).setText(connection);
	}
	
	public void setConfigurationFile(String file) {
		DefaultGroup g = new DefaultGroup("Configuration file:");
		new DefaultText(g,0).setText(file);		
	}

	public void ok() {
		// TODO Auto-generated method stub
		
	}	
	
}
