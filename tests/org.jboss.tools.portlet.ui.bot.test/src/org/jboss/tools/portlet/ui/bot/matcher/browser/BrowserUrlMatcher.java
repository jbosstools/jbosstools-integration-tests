package org.jboss.tools.portlet.ui.bot.matcher.browser;

import java.util.Arrays;

import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;

/**
 * Checks if the URL of the page laoded in the browser is one of the accepted URLs.  
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class BrowserUrlMatcher extends AbstractSWTMatcher<String[]> {

	private String realURL;

	@Override
	public boolean matchesSafely(final String[] acceptedURL) {
		final BrowserView browser = new BrowserView();
		if(!browser.isOpen()){
			throw new AssertionError("Browser is not open (may cause exception while deploying)");
		}
		browser.open();

		new WaitUntil(new WaitCondition() {
			
			@Override
			public boolean test() {
				return Arrays.asList(acceptedURL).contains(browser.getPageURL());
			}
			
			@Override
			public String description() {
				return acceptedURL + "are the only allowed loaded URLs but it was:" + realURL;
			}
		},TimePeriod.NORMAL);
		
		return true;
	}

	@Override
	public void describeTo(Description arg0) {
		// TODO Auto-generated method stub
	}


}
