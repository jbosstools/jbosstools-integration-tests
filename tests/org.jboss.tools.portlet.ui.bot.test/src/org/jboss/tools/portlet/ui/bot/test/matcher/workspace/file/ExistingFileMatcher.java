package org.jboss.tools.portlet.ui.bot.test.matcher.workspace.file;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.test.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.test.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;

/**
 * Checks if the file exists in the project. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class ExistingFileMatcher extends AbstractSWTMatcher<WorkspaceFile> {

	@Override
	public boolean matchesSafely(WorkspaceFile file) {
		SWTBotFactory.getOpen().perspective(ActionItem.Perspective.JAVA.LABEL);
		return SWTBotFactory.getPackageexplorer().isFilePresent(file.getProject(), file.getFilePathAsArray());
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue("existing file");		
	}
}
