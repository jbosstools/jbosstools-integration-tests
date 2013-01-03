package org.jboss.tools.portlet.ui.bot.test.seam;

import static org.jboss.tools.portlet.ui.bot.matcher.WorkspaceAssert.assertThatInWorkspace;

import org.jboss.tools.portlet.ui.bot.matcher.console.ConsoleOutputMatcher;
import org.jboss.tools.portlet.ui.bot.task.console.ConsoleClearingTask;
import org.jboss.tools.portlet.ui.bot.task.editor.CloseAllEditors;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageDefaultsFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.AbstractPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JSFPortletCreationTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JSFPortletWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.template.HotDeploymentGateinTemplate;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.junit.Test;

/**
 * Creates a java portlet and checks if the project is re-deployed. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class HotSeamPortletDeploymentGatein extends HotDeploymentGateinTemplate {

	@Override
	protected String getProjectName() {
		return CreateSeamPortletProject.PROJECT_NAME;
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
	
	// need to restart server - epp does not recognize creation of portlet sometimes 
	@Override
	protected boolean needRestart(){
		return true;
	}

}
