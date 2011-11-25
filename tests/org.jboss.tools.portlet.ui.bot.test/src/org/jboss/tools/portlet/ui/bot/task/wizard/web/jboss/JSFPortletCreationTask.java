package org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss;

import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageDefaultsFillingTask;


/**
 * Creates a new java portlet using a JBoss wizard. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class JSFPortletCreationTask extends AbstractPortletCreationTask {

	public JSFPortletCreationTask() {
		super("JBoss JSF/Seam Portlet");
		// leave defaults on the first page
		addWizardPage(new WizardPageDefaultsFillingTask());
	}
}
