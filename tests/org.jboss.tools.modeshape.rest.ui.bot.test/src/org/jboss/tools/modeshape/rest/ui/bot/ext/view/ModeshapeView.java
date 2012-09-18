package org.jboss.tools.modeshape.rest.ui.bot.ext.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.modeshape.rest.ui.bot.ext.dialog.ModeshapeServerDialog;
import org.jboss.tools.ui.bot.ext.gen.IView;
import org.jboss.tools.ui.bot.ext.view.ViewBase;

/**
 * 
 * This class represents the ModeShape view.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeView extends ViewBase {

	public static final String TOOLBAR_ADD_SERVER = "Create a new server";
	public static final String TOOLBAR_EDIT_SERVER = "Edit server properties";
	public static final String TOOLBAR_DELETE_SERVER = "Delete server from the server registry";
	public static final String TOOLBAR_RECONNECT_SERVER = "Reconnect to the selected server";

	public ModeshapeView() {
		viewObject = new IView() {
			public String getName() {
				return "ModeShape";
			}

			public List<String> getGroupPath() {
				List<String> list = new Vector<String>();
				list.add("ModeShape");
				return list;
			}
		};
	}

	public void toolbarButtonClick(String toolTip) {
		getToolbarButtonWitTooltip(toolTip).click();
	}

	public ModeshapeServerDialog addServer() {
		getToolbarButtonWitTooltip("Create a new server").click();
		return new ModeshapeServerDialog(bot.shell("New Server"));
	}

	public List<String> getServers() {
		List<String> servers = new ArrayList<String>();
		for (SWTBotTreeItem server : bot().tree().getAllItems()) {
			if (server.isVisible()) {
				servers.add(server.getText());
			}
		}
		return servers;
	}

	public boolean containsServer(String serverName) {
		return getServers().contains(serverName);
	}

	public void selectServer(String serverName) {
		bot().tree().getTreeItem(serverName).select();
	}

	public void deleteServer(String serverName) {
		selectServer(serverName);
		toolbarButtonClick(TOOLBAR_DELETE_SERVER);
		bot.shell("Confirm Delete Server").bot().button("OK").click();
	}
}
