package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;

/**
 * Wizard for creating a service test class.
 * 
 * @author apodhrad
 * 
 */
public class ServiceTestClassWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "Service Test Class";

	private static SWTWorkbenchBot bot = new SWTWorkbenchBot(); 
	
	public ServiceTestClassWizard activate() {
		bot.shell(DIALOG_TITLE).activate();
		return this;
	}

}
