package org.jboss.tools.openshift.reddeer.condition;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;

public class BrowserContainsText extends AbstractWaitCondition {

	private InternalBrowser browser;
	private String text;
	private String url;
	
	public BrowserContainsText(String url, String text) {
		browser = new InternalBrowser();
		this.url = url;
		this.text = text;
	}
	
	public BrowserContainsText(String text) {
		this(null, text);
	}
	
	@Override
	public boolean test() {
		if (url != null) {
			browser.setURL(url);
			browser.forward();
		} else {
			browser.forward();
		}
		return browser.getText().contains(text);
	}

	@Override
	public String description() {
		return "browser contains text";
	}

}
