package org.jboss.tools.portlet.ui.bot.test.testcase;

import static org.jboss.tools.portlet.ui.bot.matcher.WorkspaceAssert.assertThatInWorkspace;

import org.hamcrest.Matcher;
import org.jboss.tools.portlet.ui.bot.matcher.SWTMatcher;
import org.jboss.tools.portlet.ui.bot.task.SWTBotAware;
import org.jboss.tools.portlet.ui.bot.task.SWTTask;
import org.jboss.tools.ui.bot.ext.SWTTestExt;

/**
 * Common ancestor for SWT tests that wish to use {@link SWTTask} and {@link SWTMatcher}
 * 
 * @author Lucia Jelinkova
 *
 */
public class SWTTaskBasedTestCase extends SWTTestExt {

	protected void doPerform(SWTTask task){
		if (task instanceof SWTBotAware){
			((SWTBotAware) task).setBot(bot);			
		}
		task.perform();
	}
	
	protected void doAssertThatInWorkspace(Matcher<Void> matcher){
		if (matcher instanceof SWTBotAware){
			((SWTBotAware) matcher).setBot(bot);			
		}
		assertThatInWorkspace(matcher);
	}
	
	protected void doAssertThatInWorkspace(String reason, Matcher<Void> matcher){
		if (matcher instanceof SWTBotAware){
			((SWTBotAware) matcher).setBot(bot);			
		}
		assertThatInWorkspace(reason, matcher);
	}
	
	protected <T> void doAssertThatInWorkspace(T actual, Matcher<T> matcher){
		if (matcher instanceof SWTBotAware){
			((SWTBotAware) matcher).setBot(bot);			
		}
		assertThatInWorkspace(actual, matcher);
	}
	
	protected <T> void doAssertThatInWorkspace(String description, T actual, Matcher<T> matcher){
		if (matcher instanceof SWTBotAware){
			((SWTBotAware) matcher).setBot(bot);			
		}
		assertThatInWorkspace(description, actual, matcher);
	}
	
}
