package org.jboss.tools.ui.bot.ext.view;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * Class provides bot routines related to Package Explorer View
 * @author jpeterka
 *
 */
public class PackageExplorer extends SWTBotExt {
	
	/*
	 * Selects project in Package Explorer
	 */
	public void selectProject(String projectName) {
		SWTBot viewBot = viewByTitle(IDELabel.View.PACKAGE_EXPLORER).bot();
		viewBot.tree().expandNode(projectName).select();
	}

  /**
   * Selects Tree Item within Package Explorer
   * @param treeItemText
   * @param path
   * @return
   */
  public SWTBotTreeItem selectTreeItem(String treeItemText, String[] path) {
    return SWTEclipseExt.getTreeItemOnPath(viewByTitle(IDELabel.View.PACKAGE_EXPLORER).bot().tree(),
      treeItemText, path)
      .select();
  }
	
}
