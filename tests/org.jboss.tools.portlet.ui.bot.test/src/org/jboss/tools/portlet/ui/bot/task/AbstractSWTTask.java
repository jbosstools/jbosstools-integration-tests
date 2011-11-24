package org.jboss.tools.portlet.ui.bot.task;

import org.eclipse.swtbot.swt.finder.SWTBot;

/**
 * Common ancestor of all UI tasks. 
 * 
 * @author ljelinko
 *
 */
public abstract class AbstractSWTTask implements SWTTask, SWTBotAware {

	private SWTBot bot;
	
	protected void performInnerTask(SWTTask task){
		if (task instanceof SWTBotAware){
			((SWTBotAware) task).setBot(getBot());			
		}
		task.perform();
	}
	
	public SWTBot getBot() {
		return bot;
	}

	public void setBot(SWTBot bot) {
		this.bot = bot;
	}
}
