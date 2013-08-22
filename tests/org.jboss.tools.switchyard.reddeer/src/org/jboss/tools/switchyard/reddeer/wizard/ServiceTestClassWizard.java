package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.util.Bot;

/**
 * Wizard for creating a service test class.
 * 
 * @author apodhrad
 * 
 */
public class ServiceTestClassWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "Service Test Class";

	public ServiceTestClassWizard activate() {
		Bot.get().shell(DIALOG_TITLE).activate();
		return this;
	}

}
