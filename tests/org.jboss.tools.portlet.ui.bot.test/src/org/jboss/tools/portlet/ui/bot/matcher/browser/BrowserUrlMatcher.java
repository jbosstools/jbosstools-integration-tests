package org.jboss.tools.portlet.ui.bot.matcher.browser;

import java.util.Arrays;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;

/**
 * Checks if the URL of the page laoded in the browser is one of the accepted URLs.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class BrowserUrlMatcher extends AbstractSWTMatcher<String[]> {

	private String realURL;

	private long timeout;

	public BrowserUrlMatcher(TaskDuration duration){
		timeout = duration.getTimeout();
	}

	@Override
	public boolean matchesSafely(String[] acceptedURL) {
		try {
			SWTBotFactory.getBot().waitUntil(new BrowserContainsUrlCondition(acceptedURL, realURL), timeout);
			return true;
		} catch (TimeoutException e){
			return false;
		}
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("are the only allowed loaded URLs but it was:");
		description.appendValue(realURL);
	}

	private class BrowserContainsUrlCondition extends DefaultCondition {

		private String[] acceptedURL;

		private String realURL;

		public BrowserContainsUrlCondition(String[] acceptedURL, String realURL) {
			this.acceptedURL = acceptedURL;
			this.realURL = realURL;
		}

		@Override
		public boolean test() throws Exception {
			try {
				realURL = SWTBotFactory.getBot().browser().getUrl();
				return Arrays.asList(acceptedURL).contains(realURL);
			} catch (WidgetNotFoundException e){
				return false;
			}
		}

		@Override
		public String getFailureMessage() {
			return null;
		}
	}
}
