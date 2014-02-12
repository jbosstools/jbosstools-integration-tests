package org.jboss.ide.eclipse.as.ui.bot.test.condition;

import org.jboss.reddeer.swt.api.Browser;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;

/**
 * Waits until the active browser contains the specified text. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class BrowserContainsTextCondition implements WaitCondition {

	private String text;
	
	private Browser browser;
	
	public BrowserContainsTextCondition(String text) {
		this.text = text;
		browser = new InternalBrowser();
	}
	
	public BrowserContainsTextCondition(String url, String text) {
		this.text = text;
		browser = new InternalBrowser();
		browser.setURL(url);
	}
	
	@Override
	public boolean test() {
		return browser.getText().contains(text);
	}

	@Override
	public String description() {
		return "Browser should contain text: " + text + ", but contains: " + new InternalBrowser().getText();
	}

}
