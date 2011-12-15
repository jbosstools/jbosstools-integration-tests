package org.jboss.tools.portlet.ui.bot.task.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.portlet.ui.bot.entity.WorkspaceFile;
import org.jboss.tools.portlet.ui.bot.task.AbstractSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * Runs the file on server. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class RunningFileOnServerTask extends AbstractSWTTask {

	private WorkspaceFile workspaceFile;

	public RunningFileOnServerTask(WorkspaceFile file) {
		this.workspaceFile = file;
	}

	@Override
	public void perform() {
		performInnerTask(new MarkFileAsDeployableTask(workspaceFile));
		SWTBot viewBot = SWTBotFactory.getPackageexplorer().show().bot();
		SWTBotView view = SWTBotFactory.getPackageexplorer().show();
		
		SWTBotTreeItem item = SWTBotFactory.getEclipse().getTreeItemOnPath(viewBot, viewBot.tree(), 0, workspaceFile.getFileName(), getFilePath());
		item.select();
		ContextMenuHelper.prepareTreeItemForContextMenu(viewBot.tree(), item);
		final SWTBotMenu menuRunAs = viewBot.menu(IDELabel.Menu.RUN).menu(IDELabel.Menu.RUN_AS);
		final MenuItem menuItem = UIThreadRunnable
				.syncExec(new WidgetResult<MenuItem>() {
					public MenuItem run() {
						int menuItemIndex = 0;
						MenuItem menuItem = null;
						final MenuItem[] menuItems = menuRunAs.widget.getMenu().getItems();
						while (menuItem == null && menuItemIndex < menuItems.length){
							if (menuItems[menuItemIndex].getText().indexOf("Run on Server") > - 1){
								menuItem = menuItems[menuItemIndex];
							}
							else{
								menuItemIndex++;
							}
						}
						return menuItem;
					}
				});
		if (menuItem != null){
			new SWTBotMenu(menuItem).click();
			SWTBotShell shell = getBot().shell(IDELabel.Shell.RUN_ON_SERVER).activate();
			SWTBotFactory.getOpen().finish(shell.bot());		      
			SWTBotFactory.getUtil().waitForAll(Timing.time3S());
		}
		else{
			throw new WidgetNotFoundException("Unable to find Menu Item with Label 'Run on Server'");
		}
	}
	
	/**
	 * Adds the project to the path and removes file name. 
	 * @return
	 */
	private String[] getFilePath() {
		List<String> path = new ArrayList<String>(Arrays.asList(workspaceFile.getFilePathAsArray()));
		path.add(0, workspaceFile.getProject());
		path.remove(path.size() - 1);
		return path.toArray(new String[path.size()]);
	}
}
