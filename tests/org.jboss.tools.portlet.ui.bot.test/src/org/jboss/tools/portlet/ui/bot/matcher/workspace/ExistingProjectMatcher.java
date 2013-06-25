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
		return new ProjectExplorer().containsProject(project);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is an existing project");
	}
}


