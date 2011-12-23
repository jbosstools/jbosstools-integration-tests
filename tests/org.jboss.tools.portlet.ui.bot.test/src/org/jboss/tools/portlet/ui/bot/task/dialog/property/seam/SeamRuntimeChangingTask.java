package org.jboss.tools.portlet.ui.bot.task.dialog.property.seam;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.task.dialog.property.ProjectPropertyDialogCloseTask;
import org.jboss.tools.portlet.ui.bot.task.dialog.property.ProjectPropertyDialogOpenTask;

/**
 * Sets the specified Seam runtime for the project via property dialog. 
 * 
 * Requires: Seam runtime already exists. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class SeamRuntimeChangingTask extends AbstractSWTTask {

	private String project;
	
	private String runtime;
	
	public SeamRuntimeChangingTask(String project, String runtime) {
		super();
		this.project = project;
		this.runtime = runtime;
	}

	@Override
	public void perform() {
		showPropertyDialog();
		selectRuntime();
		closePropertyDialog();
	}

	private void selectRuntime() {
		getBot().comboBoxWithLabel("Seam 2 Runtime:").setSelection(runtime);
	}

	private void showPropertyDialog() {
		ProjectPropertyDialogOpenTask openTask = new ProjectPropertyDialogOpenTask();
		openTask.setProject(project);
		openTask.setPropertyPage("Seam 2 Settings");
		performInnerTask(openTask);
	}
	
	private void closePropertyDialog() {
		performInnerTask(new ProjectPropertyDialogCloseTask());
	}
}
