package org.jboss.tools.portlet.ui.bot.matcher.workspace.file;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.matcher.JavaPerspectiveAbstractSWTMatcher;

/**
 * Checks if the files exists in the project(s). 
 * 
 * @author Lucia Jelinkova
 * @author Petr Suchy
 *
 */
public class ExistingFilesMatcher extends JavaPerspectiveAbstractSWTMatcher<List<WorkspaceFile>> {

	private List<WorkspaceFile> missingFiles;
	
	public ExistingFilesMatcher() {
		missingFiles = new ArrayList<WorkspaceFile>();
	}
	
	@Override
	protected boolean matchesSafelyInJavaPerspective(List<WorkspaceFile> files) {
		for (WorkspaceFile file : files){
			boolean isPresent = new PackageExplorer().getProject(file.getProject()).containsItem(file.getFilePathAsArray());
			
			if (!isPresent){
				missingFiles.add(file);
			}
		}
		
		return missingFiles.isEmpty();
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("are existing files, but the following files are missing: " + missingFiles);
	}
}
