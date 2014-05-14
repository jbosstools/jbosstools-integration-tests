package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;

/**
 * Creates a New JSF/Seam Portlet using the wizard dialog.
 * 
 * @author psuchy
 * 
 */
public class NewJSFSeamPortletDialog extends NewWizardDialog {

	public NewJSFSeamPortletDialog() {
		super("JBoss Tools Web", "Portlet", "JBoss JSF/Seam Portlet");
	}

	@Override
	public WizardPage getFirstPage() {
		return new NewJSFSeamPortletWizardPage();
	}

}