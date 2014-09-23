package org.jboss.tools.portlet.ui.bot.task.server;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

/**
 * Performs Run on Server on the specified project. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class RunninngProjectOnServerTask extends AbstractSWTTask {

	private String project;
	
	public RunninngProjectOnServerTask(String project) {
		this.project = project;
	}
	
	@Override
	public void perform() {
		new ProjectExplorer().getProject(project).select();
		new ContextMenu("Run As","1 Run on Server").select();
		new WizardDialog().finish();
	}
}
