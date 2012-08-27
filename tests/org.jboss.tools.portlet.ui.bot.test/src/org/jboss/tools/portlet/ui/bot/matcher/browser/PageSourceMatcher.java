package org.jboss.tools.portlet.ui.bot.matcher.browser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;

/**
 * Checks if the given page contains specified text. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class PageSourceMatcher extends AbstractSWTMatcher<String> {

	private String pageText;

	private String url;
	
	private long timeout;
	
	public PageSourceMatcher() {
		super();
		timeout = 0;
	}

	public PageSourceMatcher(TaskDuration timeout) {
		super();
		this.timeout = timeout.getTimeout();
	}
	
	public PageSourceMatcher(String url, TaskDuration duration) {
		this();
		this.url = url;
		this.timeout = duration.getTimeout();
	}
	
	@Override
	public boolean matchesSafely(String item) {
		SWTBotBrowserExt browser = SWTBotFactory.getBot().browserExt();
		if (url != null){
			browser.loadUrlToBrowser(url, SWTBotFactory.getBot());
		}
		
		try {
			SWTBotFactory.getBot().waitUntil(new PageContainsTextCondition(browser, item), timeout);
			return true;
		} catch (TimeoutException e){
			return false;
		}
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is on the page, but there was instead: ");
		description.appendValue(pageText);
	}
	
	private class PageContainsTextCondition extends DefaultCondition {

		private SWTBotBrowserExt browser;
		
		private String expectedText;

		public PageContainsTextCondition(SWTBotBrowserExt browser, String item) {
			this.browser = browser;
			this.expectedText = item;
		}

		@Override
		public boolean test() throws Exception {
			pageText = browser.getText();
			if ("".equals(expectedText)){
				return pageText.equals(expectedText);
			}
			System.out.println(pageText);
			Pattern p = Pattern.compile(expectedText, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	        Matcher m = p.matcher(pageText);
			return m.matches();
		}

		@Override
		public String getFailureMessage() {
			return null;
		}
	}
}
