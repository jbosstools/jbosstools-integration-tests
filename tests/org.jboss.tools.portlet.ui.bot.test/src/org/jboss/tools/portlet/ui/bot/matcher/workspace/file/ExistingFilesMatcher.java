package org.jboss.tools.portlet.ui.bot.matcher.workspace.file;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;

public class ExistingFilesMatcher extends AbstractSWTMatcher<List<WorkspaceFile>> {

	private List<WorkspaceFile> missingFiles;
	
	public ExistingFilesMatcher() {
		missingFiles = new ArrayList<WorkspaceFile>();
	}
	
	@Override
	public boolean matchesSafely(List<WorkspaceFile> files) {
		SWTBotFactory.getOpen().perspective(ActionItem.Perspective.JAVA.LABEL);
		
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
		description.appendText("existing files " + missingFiles);
	}
}
