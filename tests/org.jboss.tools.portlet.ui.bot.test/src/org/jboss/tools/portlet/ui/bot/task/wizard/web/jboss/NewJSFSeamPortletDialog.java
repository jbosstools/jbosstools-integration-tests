package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * Creates a New JSF/Seam Portlet using the wizard dialog.
 * 
 * @author psuchy
 * 
 */
public class NewJSFSeamPortletDialog extends NewWizardDialog {

	public NewJSFSeamPortletDialog() {
		super("JBoss Tools Web", "Portlet", "JBoss JSF/Seam Portlet");
		addWizardPage(new NewJSFSeamPortletWizardPage(), 1);
	}

}