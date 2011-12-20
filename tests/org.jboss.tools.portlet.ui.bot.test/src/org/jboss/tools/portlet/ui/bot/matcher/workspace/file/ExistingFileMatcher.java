package org.jboss.tools.portlet.ui.bot.matcher.workspace.file;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.matcher.JavaPerspectiveAbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

/**
 * Checks if the file exists in the project. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ExistingFileMatcher extends JavaPerspectiveAbstractSWTMatcher<WorkspaceFile> {

	@Override
	protected boolean matchesSafelyInJavaPerspective(WorkspaceFile file) {
		return SWTBotFactory.getPackageexplorer().isFilePresent(file.getProject(), file.getFilePathAsArray());
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue("is an existing file");
	}
}
