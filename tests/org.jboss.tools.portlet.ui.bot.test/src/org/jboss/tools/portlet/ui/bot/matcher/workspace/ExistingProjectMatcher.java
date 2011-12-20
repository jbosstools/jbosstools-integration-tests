package org.jboss.tools.portlet.ui.bot.matcher.workspace;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.matcher.JavaPerspectiveAbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

/**
 * Checks if the project exists in the workspace. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ExistingProjectMatcher extends JavaPerspectiveAbstractSWTMatcher<String> {

	
	@Override
	protected boolean matchesSafelyInJavaPerspective(String project) {
		return SWTBotFactory.getEclipse().isProjectInPackageExplorer(project);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("is an existing project");
	}
}


