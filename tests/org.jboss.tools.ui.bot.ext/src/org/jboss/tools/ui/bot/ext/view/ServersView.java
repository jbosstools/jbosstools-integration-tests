package org.jboss.tools.ui.bot.ext.view;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View;
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
		        	log.info("Removing all projects from server '"+serverName+"'");
		        }
		        open.finish(this, IDELabel.Button.FINISH);
		        new SWTUtilExt(this).waitForNonIgnoredJobs();
		        new SWTUtilExt(this).waitForAll(Timing.time3S());
	        } catch (WidgetNotFoundException ex) {
	        	ex.printStackTrace();
	        	shell("Server").activate();
	        	button(IDELabel.Button.OK).click();
	        }
	        
		}
	}
	/**
	 * stops application server of given name
	 * @param serverName
	 */
	public void stopServer(String serverName) {
		SWTBot bot = open.viewSelect(ServerServers.LABEL).bot();
		SWTBotTree tree = bot.tree();
		SWTBotTreeItem server = findServerByName(tree,serverName);
		if (server!=null) {
			ContextMenuHelper.prepareTreeItemForContextMenu(tree, server);
	        new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.STOP, false)).click();
		    new SWTUtilExt(this).waitForNonIgnoredJobs();
		    new SWTUtilExt(this).waitForAll(Timing.time3S());
	        
		}
	}
	/**
	 * starts application server by given name
	 * @param serverName
	 */
	public void startServer(String serverName) {
		show();
		SWTBot bot = open.viewSelect(ServerServers.LABEL).bot();
		SWTBotTree tree = bot.tree();
		SWTBotTreeItem server = findServerByName(tree,serverName);
		if (server!=null) {
			ContextMenuHelper.prepareTreeItemForContextMenu(tree, server);
	        new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.START, false)).click();
		    new SWTUtilExt(this).waitForNonIgnoredJobs(Timing.time100S());
		    new SWTUtilExt(this).waitForAll(Timing.time3S());
	        
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
	/**
	 * removes project with given name from all servers
	 * @param projectName
	 */
	public void removeProjectFromServers(String projectName){
		
		SWTBot bot = open.viewOpen(View.ServerServers.LABEL).bot();	    	    
	    SWTBotTree serverTree = bot.tree();
	    // Expand All
	    for (SWTBotTreeItem serverTreeItem : serverTree.getAllItems()){
	      serverTreeItem.expand();
	      // if JSF Test Project is deployed to server remove it
	      SWTBotTreeItem[] serverTreeItemChildren = serverTreeItem.getItems();
	      if (serverTreeItemChildren != null && serverTreeItemChildren.length > 0){
	        int itemIndex = 0;
	        boolean found = false;
	        do{
	          String treeItemlabel = serverTreeItemChildren[itemIndex].getText();
	          found = treeItemlabel.startsWith(projectName);
	        } while (!found && ++itemIndex < serverTreeItemChildren.length);
	        // Server Tree Item has Child with Text equal to JSF TEst Project
	        if (found){
	          log.info("Found project to be removed from server: " + serverTreeItemChildren[itemIndex].getText());
	          ContextMenuHelper.prepareTreeItemForContextMenu(serverTree,serverTreeItemChildren[itemIndex]);
	          new SWTBotMenu(ContextMenuHelper.getContextMenu(serverTree, IDELabel.Menu.REMOVE, false)).click();
	          bot.shell("Server").activate();
	          open.finish(this, IDELabel.Button.OK);
	          log.info("Removed project from server: " + serverTreeItemChildren[itemIndex].getText());
	          new SWTUtilExt(this).waitForNonIgnoredJobs();	          
	        } else {
	        	log.info("Project '"+projectName+"' not found on any server");
	        }
	      }
	    }
	  }
	/**
	 * removes project with given name from all servers
	 * @param projectName
	 */
}
