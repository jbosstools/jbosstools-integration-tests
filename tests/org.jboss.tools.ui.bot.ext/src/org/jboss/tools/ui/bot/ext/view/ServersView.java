package org.jboss.tools.ui.bot.ext.view;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View.ServerServers;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class ServersView extends SWTBotExt {

	Logger log = Logger.getLogger(ProjectExplorer.class);
	private final SWTOpenExt open;
	public ServersView() {
		open = new SWTOpenExt(this);
	}
	/**
	 * shows Project Explorer view
	 */
	public void show() {
		open.viewOpen(ActionItem.View.ServerServers.LABEL);
	}
	/**
	 * removes all projects from server with given name
	 * @param serverName
	 */
	public void removeAllProjectsFromServer(String serverName) {
		SWTBot bot = open.viewSelect(ServerServers.LABEL).bot();
		SWTBotTree tree = bot.tree();
		SWTBotTreeItem server = findServerByName(tree,serverName);
		if (server!=null) {
			ContextMenuHelper.prepareTreeItemForContextMenu(tree, server);
	        new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.ADD_AND_REMOVE, false)).click();
	        try {
	        	shell(IDELabel.Menu.ADD_AND_REMOVE).activate();
		        SWTBotButton btRemoveAll=button("<< Remove All");		        
		        if (btRemoveAll.isEnabled()) {
		        	btRemoveAll.click();
		        }
		        open.finish(this, IDELabel.Button.FINISH);
	        } catch (WidgetNotFoundException ex) {
	        	ex.printStackTrace();
	        	shell("Server").activate();
	        	button(IDELabel.Button.OK).click();
	        	throw ex;
	        }
	        
		}
	}
	
	private SWTBotTreeItem findServerByName(SWTBotTree tree, String name) {
		
		for (SWTBotTreeItem i : tree.getAllItems()) {
			if (i.getText().startsWith(name)) {
				return i;
			}
		}
		return null;
	}
}
