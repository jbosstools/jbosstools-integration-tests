package org.jboss.tools.portlet.ui.bot.matcher.workspace;

import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.tools.portlet.ui.bot.matcher.JavaPerspectiveAbstractSWTMatcher;

/**
 * Checks if the project exists in the workspace. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class ExistingProjectMatcher extends JavaPerspectiveAbstractSWTMatcher<String> {

	
	@Override
	protected boolean matchesSafelyInJavaPerspective(String project) {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		return projectExplorer.containsProject(project);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is an existing project");
	}
}


