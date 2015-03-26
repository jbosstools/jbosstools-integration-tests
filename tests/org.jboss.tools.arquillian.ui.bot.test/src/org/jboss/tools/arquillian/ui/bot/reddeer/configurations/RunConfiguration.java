package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

import org.jboss.reddeer.common.logging.Logger;

/**
 * Abstract run configuration. 
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class RunConfiguration {

	private static final Logger log = Logger.getLogger(RunConfiguration.class);

	private String category;
	
	private String name;
	
	public RunConfiguration(String category, String name) {
		checkArguments(category, name);
		this.category = category;
		this.name = name;
	}

	public String getCategory() {
		return category;
	}
	
	public String getName() {
		return name;
	}
	
	private void checkArguments(String category, String name) {
		if (category == null){
			throw new IllegalArgumentException("Category must be specified");
		}
		
		if (name == null){
			throw new IllegalArgumentException("Name must be specified");
		}
	}
}
