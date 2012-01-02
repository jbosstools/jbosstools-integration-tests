package org.jboss.tools.portlet.ui.bot.task.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

/**
 * Runs the file on server. 
 * 
 * @author Lucia Jelinkova
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
		SWTBotFactory.getPackageexplorer().show();
		SWTBotFactory.getPackageexplorer().runOnServer(workspaceFile.getProject(), workspaceFile.getFileName(), getFilePath());
	}
	
	/**
	 * Adds the project to the path and removes file name. 
	 * @return
	 */
	private String[] getFilePath() {
		List<String> path = new ArrayList<String>(Arrays.asList(workspaceFile.getFilePathAsArray()));
		path.add(0, workspaceFile.getProject());
		path.remove(path.size() - 1);
		return path.toArray(new String[path.size()]);
	}
}
