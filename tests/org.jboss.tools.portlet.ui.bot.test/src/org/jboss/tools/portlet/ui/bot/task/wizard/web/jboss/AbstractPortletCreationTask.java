package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.tools.portlet.ui.bot.task.wizard.WizardFillingTask;

/**
 * 
 * Common ancestor for tasks creating portlets.
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class AbstractPortletCreationTask extends WizardFillingTask {

	private static final String PORTLET_WIZARD_PATH = "JBoss Tools Web/Portlet";
	
	public AbstractPortletCreationTask(String portletWizardName) {
		super(portletWizardName, PORTLET_WIZARD_PATH);
	}
}
