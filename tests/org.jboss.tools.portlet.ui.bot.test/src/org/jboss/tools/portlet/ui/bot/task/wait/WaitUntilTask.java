package org.jboss.tools.portlet.ui.bot.task.wait;

import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;

/**
 * Waits for the specified condition with the specified timeout. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class WaitUntilTask extends AbstractSWTTask {

	private TaskDuration taskDuration;
	
	private ICondition condition;
	
	public WaitUntilTask(ICondition condition, TaskDuration taskDuration) {
		this.condition = condition;
		this.taskDuration = taskDuration;
	}
	
	@Override
	public void perform() {
		getBot().waitUntil(condition, taskDuration.getTimeout());
	}
}
