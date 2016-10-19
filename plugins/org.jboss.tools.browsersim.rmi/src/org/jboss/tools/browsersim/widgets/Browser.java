/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.browsersim.widgets;

import org.eclipse.swt.browser.ProgressListener;
import org.jboss.tools.browsersim.browser.IBrowser;
import org.jboss.tools.browsersim.wait.AbstractWait;
import org.jboss.tools.browsersim.wait.PageIsLoaded;
import org.jboss.tools.browsersim.wait.TimePeriod;
import org.jboss.tools.browsersim.wait.WaitUntil;

public class Browser {
	
	IBrowser browser;
	BrowserProgressListener browserProgressListener;
	
	public Browser() {
		this.browser = WidgetLookup.getBrowsersimBrowser();
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
		boolean result = RDDisplay.syncExec(new ResultRunnable<Boolean>() {
			@Override
			public Boolean run() {
				return browser.setUrl(url);
			}
		});
		
		new WaitUntil(new PageIsLoaded(this), TimePeriod.LONG);
		// Unfortunately Browser needs some time to get ready even when page is fully loaded
		AbstractWait.sleep(TimePeriod.NORMAL);
		resetProgressListener();
		return result;
	}
	
	public String getURL() {
		return RDDisplay.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return browser.getUrl();
			}
		});
	}
	
	public String getText(){
		return RDDisplay.syncExec(new ResultRunnable<String>() {

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
		return RDDisplay.syncExec(new ResultRunnable<Object>() {

			@Override
			public Object run() {
				return browser.evaluate(script);
			}

		});
	}
	
	public boolean execute(final String script) {
		return RDDisplay.syncExec(new ResultRunnable<Boolean>() {

			@Override
			public Boolean run() {
				return browser.execute(script);
			}

		});
	}
	
	public void forward() {
		setUpProgressListener();
		RDDisplay.syncExec(new ResultRunnable<Boolean>() {
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
		RDDisplay.syncExec(new ResultRunnable<Boolean>() {
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
		RDDisplay.syncExec(new Runnable() {
			@Override
			public void run() {
				browser.addProgressListener(progressListener);
			}
		});
	}
	
	public void removeProgressListener(final ProgressListener progressListener) {
		RDDisplay.syncExec(new Runnable() {
			@Override
			public void run() {
				browser.removeProgressListener(progressListener);
			}
		});
	}

}
