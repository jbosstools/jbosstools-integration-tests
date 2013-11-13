package org.jboss.tools.portlet.ui.bot.matcher.browser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;

/**
 * Checks if the given page contains specified text. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class PageSourceMatcher extends AbstractSWTMatcher<String> {

	private String pageText;

	private String url;
	
	public PageSourceMatcher() {
		super();
	}

	public PageSourceMatcher(String url) {
		this();
		this.url = url;
	}
	
	@Override
	public boolean matchesSafely(String item) {
		BrowserView browser = new BrowserView();
		browser.open();
		if (url != null){
			browser.openPageURL(url);
		}
		new WaitUntil(new PageContainsTextCondition(browser, item), TimePeriod.NORMAL);
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is on the page, but there was instead: ");
		description.appendValue(pageText);
	}
	
	private class PageContainsTextCondition implements WaitCondition {

		private BrowserView browser;
		
		private String expectedText;

		public PageContainsTextCondition(BrowserView browser, String item) {
			this.browser = browser;
			this.expectedText = item;
		}

		@Override
		public boolean test() {
			pageText = browser.getText();
			if ("".equals(expectedText)){
				return pageText.equals(expectedText);
			}
			Pattern p = Pattern.compile(expectedText, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	        Matcher m = p.matcher(pageText);
			return m.matches();
		}

		@Override
		public String description() {
			return "Page in browser view doesn't contain text \""+expectedText+"\"";
		}
	}
}
