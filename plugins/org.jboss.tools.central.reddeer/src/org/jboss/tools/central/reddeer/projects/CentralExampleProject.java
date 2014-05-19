package org.jboss.tools.central.reddeer.projects;

/**
 * Class representing example project in JBoss Central in "Start from a sample"
 * section used in JBoss Central tests.
 * 
 * @author rhopp
 *
 */

public class CentralExampleProject extends Project {

	private String category;

	public CentralExampleProject(String name, String projectName,
			String category) {
		super(name, projectName);
		this.category = category;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
