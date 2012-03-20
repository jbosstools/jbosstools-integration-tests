package org.jboss.tools.portlet.ui.bot.matcher.browser;

import java.util.Arrays;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

/**
 * Checks if the URL of the page laoded in the browser is one of the accepted URLs.  
 * 
 * @author Lucia Jelinkova
 *
 */
public class BrowserUrlMatcher extends AbstractSWTMatcher<String[]> {

	private String realURL;
	
	@Override
	public boolean matchesSafely(String[] acceptedURL) {
		realURL = SWTBotFactory.getBot().browser().getUrl();
		return Arrays.asList(acceptedURL).contains(realURL);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("are the only allowed loaded URLs but it was:");
		description.appendValue(realURL);
	}
}
