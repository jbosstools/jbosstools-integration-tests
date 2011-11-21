package org.jboss.tools.portlet.ui.bot.test.matcher.workspace;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.test.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;

/**
 * Checks if the project exists in the workspace. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ExistingProjectMatcher extends AbstractSWTMatcher<String> {

	@Override
	public boolean matchesSafely(String project) {
		SWTBotFactory.getOpen().perspective(ActionItem.Perspective.JAVA.LABEL);
		return SWTBotFactory.getEclipse().isProjectInPackageExplorer(project);
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue("existing project");
	}
}


