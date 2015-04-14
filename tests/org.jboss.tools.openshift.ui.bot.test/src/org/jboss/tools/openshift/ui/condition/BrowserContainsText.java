package org.jboss.tools.openshift.ui.condition;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;

public class BrowserContainsText implements WaitCondition {

	private InternalBrowser browser;
	private String text;
	
	public BrowserContainsText(String text) {
		browser = new InternalBrowser();
		this.text = text;
	}
	
	@Override
	public boolean test() {
		return browser.getText().contains(text);
	}

	@Override
	public String description() {
		return "browser contains text";
	}

}
