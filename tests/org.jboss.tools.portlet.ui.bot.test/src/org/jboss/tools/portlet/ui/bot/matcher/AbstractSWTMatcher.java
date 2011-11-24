package org.jboss.tools.portlet.ui.bot.matcher;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.tools.portlet.ui.bot.task.SWTBotAware;
import org.jboss.tools.portlet.ui.bot.task.SWTTask;

/**
 * Common ancestor of all SWT based matchers. 
 *  
 * @author Lucia Jelinkova
 *
 * @param <T>
 */
public abstract class AbstractSWTMatcher<T> extends TypeSafeMatcher<T> implements SWTMatcher<T>, SWTBotAware {

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
