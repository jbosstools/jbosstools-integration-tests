package org.jboss.tools.browsersim.browser;

import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.common.wait.AbstractWait;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.swt.browser.ProgressListener;
import org.jboss.tools.browsersim.browser.IBrowser;
import org.jboss.tools.browsersim.rmi.BrowsersimWidgetLookup;

public class Browser {
	
	IBrowser browser;
	BrowserProgressListener browserProgressListener;
	
	public Browser() {
		this.browser = BrowsersimWidgetLookup.getBrowsersimBrowser();
		this.browserProgressListener = new BrowserProgressListener(this);
	}
	
	/**
	 * Sets given URL in specified {@link org.eclipse.swt.browser.Browser}.
	 * 
	 * @param browser to handle
	 * @param url URL to set
	 * @return true if the operation was successful, false otherwise. 
	 */
	public boolean setURL(final String url) {
		setUpProgressListener();
		boolean result = Display.syncExec(new ResultRunnable<Boolean>() {
			@Override
			public Boolean run() {
				return browser.setUrl(url);
			}
		});
		
		new WaitUntil(new PageIsLoaded(this), TimePeriod.LONG);
		// Unfortunately Browser needs some time to get ready even when page is fully loaded
		AbstractWait.sleep(TimePeriod.MEDIUM);
		resetProgressListener();
		return result;
	}
	
	public String getURL() {
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return browser.getUrl();
			}
		});
	}
	
	public String getText(){
		return Display.syncExec(new ResultRunnable<String>() {

			@Override
			public String run() {
				return browser.getText();
			}
		});
	}
	
	public IBrowser getSWTWidget(){
		return browser;
	}
	
	public boolean isPageLoaded() {
		return browserProgressListener.isDone();
	}
	
	public Object evaluate(final String script) {
		return Display.syncExec(new ResultRunnable<Object>() {

			@Override
			public Object run() {
				return browser.evaluate(script);
			}

		});
	}
	
	public boolean execute(final String script) {
		return Display.syncExec(new ResultRunnable<Boolean>() {

			@Override
			public Boolean run() {
				return browser.execute(script);
			}

		});
	}
	
	public void forward() {
		setUpProgressListener();
		Display.syncExec(new ResultRunnable<Boolean>() {
			@Override
			public Boolean run() {
				return browser.forward();
			}
		});
		new WaitUntil(new PageIsLoaded(this), TimePeriod.LONG);
		// Unfortunately Browser needs some time to get ready even when page is fully loaded
		AbstractWait.sleep(TimePeriod.SHORT);
		resetProgressListener();
	}
	
	public void back() {
		setUpProgressListener();
		Display.syncExec(new ResultRunnable<Boolean>() {
			@Override
			public Boolean run() {
				return browser.back();
			}
		});
		new WaitUntil(new PageIsLoaded(this), TimePeriod.LONG);
		// Unfortunately Browser needs some time to get ready even when page is fully loaded
		AbstractWait.sleep(TimePeriod.SHORT);
		resetProgressListener();
	}
	
	private void setUpProgressListener (){
		addProgressListener(browserProgressListener);
		browserProgressListener.setDone(false);
	}
	
	private void resetProgressListener (){
		removeProgressListener(browserProgressListener);
		browserProgressListener.setDone(true);
	}
	
	public void addProgressListener(final ProgressListener progressListener) {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				browser.addProgressListener(progressListener);
			}
		});
	}
	
	public void removeProgressListener(final ProgressListener progressListener) {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				browser.removeProgressListener(progressListener);
			}
		});
	}

}