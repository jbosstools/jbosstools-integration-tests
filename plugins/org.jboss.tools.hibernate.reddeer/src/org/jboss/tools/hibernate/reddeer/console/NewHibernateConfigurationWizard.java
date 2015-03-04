package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
/**
 * New Hibernate Configuration File Wizard RedDeer implemenation
 * @author jpeterka
 *
 */
public class NewHibernateConfigurationWizard extends NewWizardDialog{

	/**
	 * Initializes wizard
	 */
	public NewHibernateConfigurationWizard() {
		super("Hibernate","Hibernate Configuration File (cfg.xml)");
	}

}
