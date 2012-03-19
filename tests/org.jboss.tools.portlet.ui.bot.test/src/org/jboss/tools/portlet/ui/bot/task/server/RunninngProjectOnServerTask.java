package org.jboss.tools.portlet.ui.bot.task.server;

import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;

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
		getBot().waitWhile(new NonSystemJobRunsCondition(), TaskDuration.NORMAL.getTimeout());
	}
}
