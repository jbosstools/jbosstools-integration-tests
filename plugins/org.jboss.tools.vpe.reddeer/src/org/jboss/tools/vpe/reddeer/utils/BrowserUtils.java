/******************************************************************************* 
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.reddeer.utils;

import org.eclipse.reddeer.common.util.Display;
import org.eclipse.reddeer.common.util.ResultRunnable;
import org.eclipse.reddeer.swt.api.Browser;
/**
 * Utils to handle RedDeer browser
 * @author vlado pakan
 *
 */
public class BrowserUtils {
	/**
	 * Returns true when browser contains node with searchText value
	 * @param browser
	 * @param searchText
	 * @return
	 */
	public static boolean containsNodeWithValue(final Browser browser, final String searchText) {
		return getBrowserTextContent(browser).contains(searchText);
	}
	/**
	 * Returns true when browser DOM model contains searchText
	 * @param browser
	 * @param searchText
	 * @return
	 */
	public static boolean containsStringValue(final Browser browser, final String searchText) {
		return getBrowserDomContent(browser).contains(searchText);
	}
	/**
	 * Returns String representing DOM model of browser
	 * @param browser
	 * @return
	 */
	public static String getBrowserDomContent (final Browser browser){
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
				public String run() {
					return browser.getSWTWidget().evaluate("return document.documentElement.textContent").toString();
				}
		});
	}
	/**
	 * Returns String representing text values of browser content
	 * @param browser
	 * @return
	 */
	public static String getBrowserTextContent (final Browser browser){
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
				public String run() {
					return browser.getSWTWidget().evaluate("return document.documentElement.innerHTML").toString();
				}
		});
	}
}
