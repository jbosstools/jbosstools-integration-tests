package org.jboss.tools.portlet.ui.bot.task.wizard.web;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;

/**
 * Creates a New Dynamic Web Project using the wizard dialog.
 * 
 * @author Petr Suchy
 * 
 */
public class DynamicWebProjectDialog extends NewWizardDialog {

	public DynamicWebProjectDialog() {
		super("Web", "Dynamic Web Project");
	}

	@Override
	public WizardPage getFirstPage() {
		return new DynamicWebProjectWizardPage(this);
	}

}