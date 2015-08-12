package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * Creates a New JavaPortlet using the wizard dialog.
 * 
 * @author psuchy
 * 
 */
public class NewJavaPortletDialog extends NewWizardDialog {

	public NewJavaPortletDialog() {
		super("JBoss Tools Web", "Portlet", "Java Portlet");
	}

}