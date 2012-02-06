package org.jboss.tools.portlet.ui.bot.task.wait;

import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;

/**
 * Waits while the specified condition is valid with the specified timeout. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class WaitWhileTask extends AbstractSWTTask {

	private TaskDuration taskDuration;
	
	private ICondition condition;
	
	public WaitWhileTask(ICondition condition, TaskDuration taskDuration) {
		this.condition = condition;
		this.taskDuration = taskDuration;
	}
	
	@Override
	public void perform() {
		getBot().waitWhile(condition, taskDuration.getTimeout());
	}
}
