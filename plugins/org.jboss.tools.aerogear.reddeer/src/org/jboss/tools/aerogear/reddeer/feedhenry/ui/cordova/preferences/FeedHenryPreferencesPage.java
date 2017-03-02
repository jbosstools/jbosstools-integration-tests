/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.reddeer.feedhenry.ui.cordova.preferences;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * RedDeer implementation of FeedHenry Preferences page 
 * @author Pavol Srna
 *
 */
public class FeedHenryPreferencesPage extends PreferencePage {

	public FeedHenryPreferencesPage() {
		super("FeedHenry");
	}

	/**
	 * Sets Target URL
	 * @param url
	 */
	public void setTargetUrl(String url){
		findTargetUrl().setText(url);
	}
	
	/**
	 * Sets API Key
	 * @param key
	 */
	public void setApiKey(String key){
		findApiKey().setText(key);
	}
	
	/**
	 * Returns Target URL
	 * @return
	 */
	public String getTargetUrl(){
		return findTargetUrl().getText();
	}
	
	/**
	 * Returns API Key
	 * @return
	 */
	public String getApiKey(){
		return findApiKey().getText();
	}
	
	private Text findTargetUrl(){
		return new LabeledText("Target URL:");
	}
	
	private Text findApiKey(){
		return new LabeledText("API Key:");
	}
}
