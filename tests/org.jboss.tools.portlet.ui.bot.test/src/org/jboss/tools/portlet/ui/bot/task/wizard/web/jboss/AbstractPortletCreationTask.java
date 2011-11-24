package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.tools.portlet.ui.bot.task.wizard.WizardFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardOpeningTask;

/**
 * 
 * Common ancestor for tasks creating portlets.
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class AbstractPortletCreationTask extends WizardFillingTask {

	private static final String PORTLET_WIZARD_PATH = "JBoss Tools Web/Portlet";
	
	private String portletWizardName;
	
	public AbstractPortletCreationTask(String portletWizardName) {
		this.portletWizardName = portletWizardName;
	}
	
	@Override
	public void perform() {
		performInnerTask(new WizardOpeningTask(portletWizardName, PORTLET_WIZARD_PATH));
		super.perform();
	}
}
