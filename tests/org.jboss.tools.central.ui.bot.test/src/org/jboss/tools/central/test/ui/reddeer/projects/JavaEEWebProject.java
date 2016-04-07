package org.jboss.tools.central.test.ui.reddeer.projects;

import org.jboss.tools.central.reddeer.projects.ArchetypeProject;

public class JavaEEWebProject extends ArchetypeProject {

	public JavaEEWebProject(boolean blank) {
		super("Java EE Web Project", "jboss-javaee-webapp", blank);
	}
	
	@Override
	public String getReadmeString(){
		return ".cheatsheet.xml";
	}

}
