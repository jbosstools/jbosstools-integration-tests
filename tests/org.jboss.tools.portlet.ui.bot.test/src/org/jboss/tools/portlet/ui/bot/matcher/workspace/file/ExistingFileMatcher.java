package org.jboss.tools.portlet.ui.bot.matcher.workspace.file;

import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.matcher.JavaPerspectiveAbstractSWTMatcher;

/**
 * Checks if the file exists in the project. 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class ExistingFileMatcher extends JavaPerspectiveAbstractSWTMatcher<WorkspaceFile> {

	@Override
	protected boolean matchesSafelyInJavaPerspective(WorkspaceFile file) {
		return new PackageExplorer().getProject(file.getProject()).containsItem(file.getFilePathAsArray());
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue("is an existing file");
	}
}
