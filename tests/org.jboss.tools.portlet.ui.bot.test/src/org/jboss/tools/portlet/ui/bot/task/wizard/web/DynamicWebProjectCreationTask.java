package org.jboss.tools.portlet.ui.bot.task.wizard.web;

import org.jboss.tools.portlet.ui.bot.task.dialog.AssociatedPerspectiveDialogClosingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardFillingTask;

/**
 * Create a new dynamic web project using the wizard. 
 * 
 * @author ljelinko
 *
 */
public class DynamicWebProjectCreationTask extends WizardFillingTask {

	public DynamicWebProjectCreationTask() {
		super("Dynamic Web Project", "Web");
	}
	
	@Override
	public void perform() {
		super.perform();
		performInnerTask(new AssociatedPerspectiveDialogClosingTask(false));
	}
}