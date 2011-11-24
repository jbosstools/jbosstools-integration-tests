package org.jboss.tools.portlet.ui.bot.task.wizard.web;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.facet.FacetsSelectionTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;

/**
 * Fills in the information of the first page of Dynamic Web Project Wizard. 
 * @author ljelinko
 *
 */
public class DynamicWebProjectWizardPageFillingTask extends AbstractSWTTask
	implements WizardPageFillingTask {

	private String projectName;

	private String webModuleVersion;

	private String serverName;

	private FacetsSelectionTask selectFacetsTask;

	@Override
	public void perform() {
		getBot().textWithLabel("Project name:").typeText(projectName);

		if (webModuleVersion != null){
			getBot().comboBoxInGroup("Dynamic web module version").setSelection(webModuleVersion);
		}

		if (serverName != null){
			getBot().comboBoxInGroup("Target runtime").setSelection(serverName);
		}

		if (selectFacetsTask != null){
			getBot().button("Modify...").click();
			performInnerTask(selectFacetsTask);
			getBot().button("OK").click();
		}		
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setWebModuleVersion(String webModuleVersion) {
		this.webModuleVersion = webModuleVersion;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setSelectFacetsTask(FacetsSelectionTask selectFacetsTask) {
		this.selectFacetsTask = selectFacetsTask;
	}
}
