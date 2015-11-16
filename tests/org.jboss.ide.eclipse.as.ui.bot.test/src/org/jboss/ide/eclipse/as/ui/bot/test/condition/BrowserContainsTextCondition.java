package org.jboss.ide.eclipse.as.ui.bot.test.condition;

import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;

/**
 * Waits until the active browser contains the specified text. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class BrowserContainsTextCondition extends AbstractWaitCondition {

	private String text;

	private boolean refresh;

	private BrowserView browserView;

	public BrowserContainsTextCondition(String text) {
		this(text, false);
	}

	public BrowserContainsTextCondition(String text, boolean refresh) {
		this(null, text, refresh);
	}

	public BrowserContainsTextCondition(String url, String text, boolean refresh) {
		this.text = text;
		this.refresh = refresh;

		browserView = new BrowserView();
		browserView.open();
		AbstractWait.sleep(TimePeriod.LONG);
		if (url != null){
			browserView.openPageURL(url);
		} 
	}

	@Override
	public boolean test() {
		if (refresh){
			browserView.refreshPage();
		}
		return browserView.getText().contains(text);
	}

	@Override
	public String description() {
		return "Browser should contain text: " + text + ", but contains: " + browserView.getText();
	}

}
