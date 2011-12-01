package org.jboss.tools.portlet.ui.bot.task.workspace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;

/**
 * Invokes specified right-click menu on the given file. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class FileContextMenuSelectingTask extends AbstractSWTTask {

	private WorkspaceFile file;
	
	private String menuItem;
	
	public FileContextMenuSelectingTask(WorkspaceFile file, String menuItem) {
		super();
		this.file = file;
		this.menuItem = menuItem;
	}

	@Override
	public void perform() {
		PackageExplorer explorer = SWTBotFactory.getPackageexplorer();
		explorer.show();
		SWTEclipseExt.getTreeItemOnPath(explorer.bot(), explorer.bot().tree(), 0, file.getFileName(), getFilePath()).select();
		ContextMenuHelper.clickContextMenu(SWTBotFactory.getPackageexplorer().bot().tree(), menuItem);
	}

	/**
	 * Adds the project to the path and removes file name. 
	 * @return
	 */
	private String[] getFilePath() {
		List<String> path = new ArrayList<String>(Arrays.asList(file.getFilePathAsArray()));
		path.add(0, file.getProject());
		path.remove(path.size() - 1);
		return path.toArray(new String[path.size()]);
	}
}
