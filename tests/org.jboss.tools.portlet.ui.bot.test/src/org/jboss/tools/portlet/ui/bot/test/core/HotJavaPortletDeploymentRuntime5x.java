package org.jboss.tools.portlet.ui.bot.test.core;

import static org.jboss.tools.portlet.ui.bot.test.core.CreateJavaPortletProject.PROJECT_NAME;

import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.AbstractPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JavaPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JavaPortletWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.template.CreateJavaPortletTemplate;
import org.jboss.tools.portlet.ui.bot.test.template.HotDeploymentRuntime5xTemplate;

/**
 * Creates a java portlet and checks if the project is re-deployed. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class HotJavaPortletDeploymentRuntime5x extends HotDeploymentRuntime5xTemplate {

	@Override
	protected String getProjectName() {
		return CreateJavaPortletProject.PROJECT_NAME;
	}

	@Override
	protected AbstractPortletCreationTask createPortlet() {
		JavaPortletWizardPageFillingTask task = new JavaPortletWizardPageFillingTask();
		task.setProject(PROJECT_NAME);
		task.setPackageName(CreateJavaPortletTemplate.PACKAGE_NAME);
		task.setClassName("HotDeployedJavaPortlet");
		
		JavaPortletCreationTask wizardTask = new JavaPortletCreationTask();
		wizardTask.addWizardPage(task);
		return wizardTask;
	}

}
