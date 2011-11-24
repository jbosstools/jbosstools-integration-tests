package org.jboss.tools.portlet.ui.bot.test.testcase;

import static org.junit.Assert.assertThat;

import org.jboss.tools.portlet.ui.bot.task.SWTBotAware;
import org.jboss.tools.portlet.ui.bot.task.SWTTask;
import org.jboss.tools.portlet.ui.bot.test.matcher.SWTMatcher;
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
	
	protected <T> void doAssertThat(T actual, SWTMatcher<T> matcher){
		if (matcher instanceof SWTBotAware){
			((SWTBotAware) matcher).setBot(bot);			
		}
		assertThat(actual, matcher);
	}
	
}
