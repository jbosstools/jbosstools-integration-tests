package org.jboss.tools.portlet.ui.bot.test.task.wizard.web;

import org.jboss.tools.portlet.ui.bot.test.task.dialog.AssociatedPerspectiveDialogClosingTask;
import org.jboss.tools.portlet.ui.bot.test.task.facet.FacetsSelectionTask;
import org.jboss.tools.portlet.ui.bot.test.task.wizard.WizardFillingTask;
import org.jboss.tools.portlet.ui.bot.test.task.wizard.WizardOpeningTask;

/**
 * Creates a new dynamic web project using the wizard. 
 * 
 * @author ljelinko
 *
 */
public class DynamicWebProjectCreationTask extends WizardFillingTask {

	private DynamicWebProjectWizardPageFillingTask firstPage;
	
	public DynamicWebProjectCreationTask() {
		super();
		firstPage = new DynamicWebProjectWizardPageFillingTask();
		addWizardPage(firstPage);
	}
	
	@Override
	public void perform() {
		performInnerTask(new WizardOpeningTask("Dynamic Web Project", "Web"));
		super.perform();
		performInnerTask(new AssociatedPerspectiveDialogClosingTask(false));
	}

	public void setProjectName(String projectName) {
		firstPage.setProjectName(projectName);
	}

	public void setWebModuleVersion(String webModuleVersion) {
		firstPage.setWebModuleVersion(webModuleVersion);
	}

	public void setServerName(String serverName) {
		firstPage.setServerName(serverName);
	}
	
	public void setSelectFacetsTask(FacetsSelectionTask selectFacetsTask) {
		firstPage.setSelectFacetsTask(selectFacetsTask);
	}
}
