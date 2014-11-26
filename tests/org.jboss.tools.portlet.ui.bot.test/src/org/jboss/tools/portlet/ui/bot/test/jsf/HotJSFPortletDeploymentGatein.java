package org.jboss.tools.portlet.ui.bot.test.jsf;

import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.NewJSFSeamPortletDialog;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.NewJSFSeamPortletWizardPage;
import org.jboss.tools.portlet.ui.bot.test.template.HotDeploymentGateinTemplate;

/**
 * Creates a java portlet and checks if the project is re-deployed. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class HotJSFPortletDeploymentGatein extends HotDeploymentGateinTemplate {

	@Override
	protected String getProjectName() {
		return CreateJSFPortletProject.PROJECT_NAME;
	}

	@Override
	protected void createPortlet() {
		NewJSFSeamPortletDialog dialog = new NewJSFSeamPortletDialog();
		dialog.open();
		dialog.next();
		NewJSFSeamPortletWizardPage page = (NewJSFSeamPortletWizardPage) dialog.getCurrentWizardPage();
		page.setName("hotDeployment");
		page.setDisplayName(page.getDisplayName() + " Hot Deploy");
		page.setTitle(page.getTitle() + " Hot Deploy");
		dialog.finish();
	}
	
	@Override
	protected boolean needRestart() {
		return true;
	}
}
