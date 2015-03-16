package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * aka Hibernate Console Wizard
 * 
 * @author Jiri Peterka
 *
 */
public class ConsoleConfigurationCreationWizard extends NewWizardDialog {

	/**
	 * Initialize New JPA project wizard
	 */
	public ConsoleConfigurationCreationWizard() {
		super("Hibernate", "Hibernate Console Configuration");
	}

}