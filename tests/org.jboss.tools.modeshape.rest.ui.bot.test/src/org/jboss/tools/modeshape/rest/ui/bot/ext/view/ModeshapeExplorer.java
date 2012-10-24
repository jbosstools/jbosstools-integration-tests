package org.jboss.tools.modeshape.rest.ui.bot.ext.view;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.gen.IView;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.view.ExplorerBase;

/**
 * 
 * This class represents an extension of Explorer view. The extension refers to
 * modeshape context menu.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeExplorer extends ExplorerBase {

	public ModeshapeExplorer(IView view) {
		viewObject = view;
	}

	public void publish(String... path) {
		contextMenu("Publish", path);
	}

	public void unpublish(String... path) {
		contextMenu("Unpublish", path);
	}

	public void showPublishedLocations(String... path) {
		contextMenu("Show Published Locations", path);
	}

	private void contextMenu(String selection, String... path) {
		selectTreeItem(path);
		ContextMenuHelper.clickContextMenu(bot().tree(), "Refresh");
		ContextMenuHelper.clickContextMenu(bot().tree(), "ModeShape", selection);
	}

	public void selectTreeItem(String... path) {
		if (path.length < 1) {
			throw new IllegalArgumentException("The path must contain at least one item!");
		}
		SWTBotTree tree = bot().tree();
		SWTBotTreeItem item = tree.getTreeItem(path[0]);
		for (int i = 1; i < path.length; i++) {
			item.expand();
			item = item.getNode(path[i]);
		}
		item.setFocus();
		item.select();
	}
}
