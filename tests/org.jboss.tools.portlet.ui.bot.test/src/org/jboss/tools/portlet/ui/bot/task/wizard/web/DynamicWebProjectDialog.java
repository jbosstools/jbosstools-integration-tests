package org.jboss.tools.portlet.ui.bot.task.wizard.web;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * Creates a New Dynamic Web Project using the wizard dialog.
 * 
 * @author Petr Suchy
 * 
 */
public class DynamicWebProjectDialog extends NewWizardDialog {

	public DynamicWebProjectDialog() {
		super("Web", "Dynamic Web Project");
		addWizardPage(new DynamicWebProjectWizardPage(), 0);
	}

}