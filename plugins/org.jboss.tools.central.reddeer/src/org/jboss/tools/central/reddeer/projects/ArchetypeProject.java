package org.jboss.tools.central.reddeer.projects;

/**
 * Class representing archetype project in JBoss Central used in JBoss Central
 * tests.
 * 
 * @author rhopp
 *
 */

public class ArchetypeProject extends Project {

	boolean blank;

	public ArchetypeProject(String name, String projectName, boolean blank) {
		super(name, projectName);
		this.blank = blank;
	}

	public void setBlank(boolean blank) {
		this.blank = blank;
	}

	public boolean isBlank() {
		return blank;
	}

}
