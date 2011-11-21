package org.jboss.tools.portlet.ui.bot.test.matcher.workspace;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.test.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;

/**
 * Checks if the file exists in the project. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ExistingFileMatcher extends AbstractSWTMatcher<String> {

	private static final String FILE_SEPARATOR = "/";

	private String project;

	public ExistingFileMatcher(String project) {
		this.project = project;
	}

	@Override
	public boolean matchesSafely(String path) {
		SWTBotFactory.getOpen().perspective(ActionItem.Perspective.JAVA.LABEL);
		return SWTBotFactory.getPackageexplorer().isFilePresent(project, path.split(FILE_SEPARATOR));
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue("existing file");		
	}
}
