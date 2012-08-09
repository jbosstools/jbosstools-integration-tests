package org.jboss.ide.eclipse.as.ui.bot.test.web;

import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;

public class PageSourceMatcher extends TypeSafeMatcher<String> {

	private String pageText;

	private String url;
	
	private long timeout;
	
	public PageSourceMatcher() {
		super();
		timeout = 0;
	}

	public PageSourceMatcher(long timeout) {
		super();
		this.timeout = timeout;
	}
	
	public PageSourceMatcher(TaskDuration timeout) {
		super();
		this.timeout = timeout.getTimeout();
	}
	
	public PageSourceMatcher(String url) {
		this();
		this.url = url;
		this.timeout = 0;
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
			return pageText.contains(expectedText);
		}

		@Override
		public String getFailureMessage() {
			return null;
		}
	}
}
