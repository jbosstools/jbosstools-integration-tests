package org.jboss.tools.portlet.ui.bot.task.server;

import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

/**
 * Runs the file on server. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class RunningFileOnServerTask extends AbstractSWTTask {

	private WorkspaceFile workspaceFile;

	public RunningFileOnServerTask(WorkspaceFile file) {
		this.workspaceFile = file;
	}

	@Override
	public void perform() {
		performInnerTask(new MarkFileAsDeployableTask(workspaceFile));
		performInnerTask(new RunninngProjectOnServerTask(workspaceFile.getProject()));
	}
	
}
