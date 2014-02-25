package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

public class NewJavaEnumWizardDialog extends NewWizardDialog{
	
	public NewJavaEnumWizardDialog() {
		super("Java", "Enum");
	}
	
	@Override
	public NewJavaEnumWizardPage getFirstPage() {
		return new NewJavaEnumWizardPage();
	}

}
