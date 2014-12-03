package org.jboss.tools.examples.ui.bot.test.integration;

import java.io.File;

/**
 * Pojo representing one quickstart.
 * 
 * @author rhopp
 *
 */

public class Quickstart {

	private String name;
	private File path;
	
	
	public Quickstart(String name, String path) {
		this.name = name;
		this.path = new File(path);
		if (!this.path.isDirectory()){
			throw new IllegalArgumentException("Parameter path does not point to directory:"+path);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public File getPath() {
		return path;
	}
	
	public void setPath(File path) {
		this.path = path;
	}
	
}
