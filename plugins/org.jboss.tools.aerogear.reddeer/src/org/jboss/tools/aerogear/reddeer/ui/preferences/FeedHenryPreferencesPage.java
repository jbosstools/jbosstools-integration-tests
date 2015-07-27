package org.jboss.tools.aerogear.reddeer.ui.preferences;

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
