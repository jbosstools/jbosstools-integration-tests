package org.jboss.tools.portlet.ui.bot.test.jsf;

import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageDefaultsFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.AbstractPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JSFPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JSFPortletWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.template.HotDeploymentGateinTemplate;

/**
 * Creates a java portlet and checks if the project is re-deployed. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class HotJSFPortletDeploymentGatein extends HotDeploymentGateinTemplate {

	@Override
	protected String getProjectName() {
		return CreateJSFPortletProject.PROJECT_NAME;
	}

	@Override
	protected AbstractPortletCreationTask createPortlet() {
		JSFPortletWizardPageFillingTask jsfPage = new JSFPortletWizardPageFillingTask();
		jsfPage.setName("hotDeployment");
		
		JSFPortletCreationTask wizard = new JSFPortletCreationTask();
		wizard.addWizardPage(new WizardPageDefaultsFillingTask());
		wizard.addWizardPage(jsfPage);
		wizard.addWizardPage(new WizardPageDefaultsFillingTask());
		return wizard;
	}
}
