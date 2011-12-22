package org.jboss.tools.portlet.ui.bot.matcher.browser;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.portlet.ui.bot.task.browser.LoadBrowserPageTask;

/**
 * Checks if the given page contains specified text. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class PageSourceMatcher extends AbstractSWTMatcher<String> {

	private String url;
	
	private String pageText;
	
	public PageSourceMatcher(String url) {
		super();
		this.url = url;
	}

	@Override
	public boolean matchesSafely(String item) {
		performInnerTask(new LoadBrowserPageTask(url));
		pageText = getBot().browser().getText();
		return pageText.contains(item);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is on the page, but there was instead: ");
		description.appendValue(pageText);
	}
}
