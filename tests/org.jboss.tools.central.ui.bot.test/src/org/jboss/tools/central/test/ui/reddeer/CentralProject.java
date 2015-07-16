package org.jboss.tools.central.test.ui.reddeer;

public class CentralProject {
	
	private String name;
	private String description;
	
	public CentralProject(String name, String desc) {
		this.name = name;
		this.description = desc;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
