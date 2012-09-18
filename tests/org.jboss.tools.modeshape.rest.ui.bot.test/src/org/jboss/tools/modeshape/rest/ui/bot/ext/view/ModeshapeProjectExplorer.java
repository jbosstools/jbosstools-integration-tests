package org.jboss.tools.modeshape.rest.ui.bot.ext.view;

import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;

/**
 * 
 * This class represents an extension of Project Explorer. The extension refers
 * to modeshape context menu.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeProjectExplorer extends ProjectExplorer {

	public void publish(String file, String... path) {
		contextMenu(file, "Publish", path);
	}

	public void unpublish(String file, String... path) {
		contextMenu(file, "Unpublish", path);
	}

	public void showPublishedLocations(String file, String... path) {
		contextMenu(file, "Show Published Locations", path);
	}

	private void contextMenu(String file, String selection, String... path) {
		selectTreeItem(file, path);
		ContextMenuHelper.clickContextMenu(bot().tree(), "ModeShape", selection);
	}
}
