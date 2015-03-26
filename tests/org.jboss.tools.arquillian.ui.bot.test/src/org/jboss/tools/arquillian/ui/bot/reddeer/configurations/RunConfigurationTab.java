package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;

/**
 * Abstract tab in run configuration
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class RunConfigurationTab {

	private String name;
	
	public RunConfigurationTab(String name) {
		this.name = name;
	}
	
	/**
	 * Activates the tab
	 */
	public void activate(){
		new DefaultCTabItem(name).activate();
	}
}
