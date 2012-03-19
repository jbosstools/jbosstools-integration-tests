package org.jboss.tools.portlet.ui.bot.matcher.browser;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

/**
 * Checks the URL of the page laoded in the browser. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class BrowserUrlMatcher extends AbstractSWTMatcher<String> {

	private String realURL;
	
	@Override
	public boolean matchesSafely(String expectedURL) {
		realURL = SWTBotFactory.getBot().browser().getUrl();
		return expectedURL.equals(realURL);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is loaded in browser but it was: ");
		description.appendValue(realURL);
	}
}
