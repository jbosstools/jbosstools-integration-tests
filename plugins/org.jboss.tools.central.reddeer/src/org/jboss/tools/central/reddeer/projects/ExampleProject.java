package org.jboss.tools.central.reddeer.projects;

/**
 * Represents example project which can be imported using File->New->Project Examples
 * 
 * @author rhopp
 *
 */

public class ExampleProject extends Project {

	private String[] path;
	
	public ExampleProject(String name, String projectName, String... path) {
		super(name, projectName);
		this.path = path;
	}

	public String[] getPath() {
		return path;
	}

	public void setPath(String[] path) {
		this.path = path;
	}

}
