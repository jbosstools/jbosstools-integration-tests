package org.jboss.tools.portlet.ui.bot.matcher.workspace.file;

import java.util.List;

import org.hamcrest.Description;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.matcher.AbstractSWTMatcher;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;

public class ExistingFilesMatcher extends AbstractSWTMatcher<List<WorkspaceFile>> {

	@Override
	public boolean matchesSafely(List<WorkspaceFile> files) {
		SWTBotFactory.getOpen().perspective(ActionItem.Perspective.JAVA.LABEL);
		
		for (WorkspaceFile file : files){
			boolean isPresent = SWTBotFactory.getPackageexplorer().isFilePresent(file.getProject(), file.getFilePathAsArray());
			
			if (!isPresent){
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("existing files");
	}
}
