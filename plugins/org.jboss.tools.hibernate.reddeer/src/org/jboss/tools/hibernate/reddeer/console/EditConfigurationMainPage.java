package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * Hibernate Console Configuration Main Page
 * @author jpeterka
 *
 */
public class EditConfigurationMainPage {

	public interface PredefinedConnection {
	    String HIBERNATE_CONFIGURED_CONNECTION = "[Hibernate configured connection]";
	    String JPA_PROJECT_CONFIGURED_CONNECTION = "[JPA Project Configured Connection]";
	}
	
	/**
	 * Sets project for Hibernate Console Configuration 
	 * @param project given project name
	 */
	public void setProject(String project) {
		DefaultGroup g = new DefaultGroup("Project:");
		new DefaultText(g,0).setText(project);
	}
	
	/**
	 * Sets Hibernate version for Hibernate Console Configuration
	 * @param version given version, acceptable values are "3.5","3.6" and "4.0"
	 */
	public void setHibernateVersion(String version) {
		new DefaultCombo("Hibernate Version:").setSelection(version);
	}
	
	/**
	 * Sets database connection for Hibernate Console Configuration
	 * @param connection given connection
	 */
	public void setDatabaseConnection(String connection) {
		DefaultGroup g = new DefaultGroup("Database connection:");
		new DefaultCombo(g,0).setText(connection);
	}
	
	/**
	 * Set configuration file for Hibernate Console Configuration
	 * @param file given file path
	 */
	public void setConfigurationFile(String file) {
		DefaultGroup g = new DefaultGroup("Configuration file:");
		new DefaultText(g,0).setText(file);		
	}	
}
