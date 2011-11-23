package org.jboss.tools.portlet.ui.bot.test.task.server;

import org.jboss.tools.portlet.ui.bot.test.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

/**
 * Performs Run on Server on the specified project. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class RunninngProjectOnServerTask extends AbstractSWTTask {

	private String project;
	
	public RunninngProjectOnServerTask(String project) {
		this.project = project;
	}
	
	@Override
	public void perform() {
		SWTBotFactory.getPackageexplorer().show();
		SWTBotFactory.getPackageexplorer().runOnServer(project);
	}
}
