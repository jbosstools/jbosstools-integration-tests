package org.jboss.tools.central.test.ui.reddeer.projects;

import org.jboss.tools.central.reddeer.projects.ArchetypeProject;

public class AngularJSForge extends ArchetypeProject{

	public AngularJSForge() {
		super("AngularJS Forge", "jboss-forge-html5", false);
	}

	@Override
	public String getReadmeString(){
		return "cheatsheet.xml";
	}
	
}
