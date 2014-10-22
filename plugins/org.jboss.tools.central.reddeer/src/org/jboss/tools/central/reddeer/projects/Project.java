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
	
	protected String packageName;
	
	public Project(String name, String projectName, String packageName) {
		this.name = name;
		this.projectName = projectName;
		this.packageName = packageName;
	}
	
	public Project(String name, String projectName){
		this(name, projectName, "");
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

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getReadmeString(){
		return "README.md";
	}
	
}
