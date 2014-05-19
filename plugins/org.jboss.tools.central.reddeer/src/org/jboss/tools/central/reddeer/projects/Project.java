package org.jboss.tools.central.reddeer.projects;

/**
 * Class representing general project used in JBoss Central Tests
 * 
 * @author rhopp
 *
 */


public class Project {

	protected String name;
	
	protected String projectName;
	
	public Project(String name, String projectName) {
		this.name = name;
		this.projectName = projectName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

}
