package org.jboss.tools.portlet.ui.bot.matcher.workspace.file;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.matcher.JavaPerspectiveAbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

public class ExistingFilesMatcher extends JavaPerspectiveAbstractSWTMatcher<List<WorkspaceFile>> {

	private List<WorkspaceFile> missingFiles;
	
	public ExistingFilesMatcher() {
		missingFiles = new ArrayList<WorkspaceFile>();
	}
	
	@Override
	protected boolean matchesSafelyInJavaPerspective(List<WorkspaceFile> files) {
		for (WorkspaceFile file : files){
			boolean isPresent = SWTBotFactory.getPackageexplorer().isFilePresent(file.getProject(), file.getFilePathAsArray());
			
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
