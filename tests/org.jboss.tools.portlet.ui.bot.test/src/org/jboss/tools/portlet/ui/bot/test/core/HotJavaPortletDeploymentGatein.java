package org.jboss.tools.portlet.ui.bot.test.core;

import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.NewJavaPortletDialog;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.NewJavaPortletWizardPage;
import org.jboss.tools.portlet.ui.bot.test.template.CreateJavaPortletTemplate;
import org.jboss.tools.portlet.ui.bot.test.template.HotDeploymentGateinTemplate;

/**
 * Creates a java portlet and checks if the project is re-deployed. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class HotJavaPortletDeploymentGatein extends HotDeploymentGateinTemplate {

	@Override
	protected String getProjectName() {
		return CreateJavaPortletProject.PROJECT_NAME;
	}

	@Override
	protected void createPortlet() {
		NewJavaPortletDialog dialog = new NewJavaPortletDialog();
		dialog.open();
		NewJavaPortletWizardPage page = new NewJavaPortletWizardPage();
		page.setClassName("HotDeployedJavaPortlet");
		page.setPackage(CreateJavaPortletTemplate.PACKAGE_NAME);
		page.setProject(CreateJavaPortletProject.PROJECT_NAME);
		dialog.finish();
	}

}
