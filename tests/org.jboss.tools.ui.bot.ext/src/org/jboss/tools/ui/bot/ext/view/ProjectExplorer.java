/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ui.bot.ext.view;

import static org.jboss.tools.ui.bot.ext.SWTTestExt.eclipse;

import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTOpenExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject.ServerServer;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View.ServerServers;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.ViewType;

/**
 * Eclipse project type enum
 * 
 * @author jpeterka
 * 
 */
public class ProjectExplorer extends SWTBotExt {

	Logger log = Logger.getLogger(ProjectExplorer.class);
	private final SWTOpenExt open;
	public ProjectExplorer() {
		open = new SWTOpenExt(this);
	}
	/**
	 * shows Project Explorer view
	 */
	public void show() {
		open.viewOpen(ActionItem.View.GeneralProjectExplorer.LABEL);
	}
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
	        }  
	      }
	    }
	  }
	/**
	 * runs given project on Server (uses default server, the first one) server MUST be running
	 * @param projectName
	 */
	public void runOnServer(String projectName) {
		SWTBot viewBot = viewByTitle(IDELabel.View.PROJECT_EXPLORER).bot();
		SWTBotTreeItem item = viewBot.tree().expandNode(projectName);
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
		      shell(IDELabel.Shell.RUN_ON_SERVER).activate();
		      new SWTOpenExt(this).finish(this);
		      SWTUtilExt swtUtil = new SWTUtilExt(this);
		      swtUtil.waitForNonIgnoredJobs();
		      swtUtil.waitForAll(Timing.time3S());
		    }
		    else{
		      throw new WidgetNotFoundException("Unable to find Menu Item with Label 'Run on Server'");
		    }
		
	}
	public SWTBotEditor openFile(String projectName, String... path) {
		SWTBot viewBot = viewByTitle(IDELabel.View.PROJECT_EXPLORER).bot();

		viewByTitle(IDELabel.View.PROJECT_EXPLORER).show();
		viewByTitle(IDELabel.View.PROJECT_EXPLORER).setFocus();
		SWTBotTree tree = viewBot.tree();
		SWTBotTreeItem item = tree.expandNode(projectName);
		StringBuilder builder = new StringBuilder(projectName);
		// Go through path
		for (String nodeName : path) {
			item = item.expandNode(nodeName);
			builder.append("/" + nodeName);
		}

		item.select().doubleClick();
		log.info("File Opened:" + builder.toString());

		SWTBotEditor editor = activeEditor();
		return editor;
	}
	public void deleteAllProjects() {
		SWTBot viewBot = viewByTitle(IDELabel.View.PROJECT_EXPLORER).bot();
		    List<String> items = new Vector<String>();
		    for (SWTBotTreeItem ti : viewBot.tree().getAllItems()) {
		    	items.add(ti.getText());
		    }
		    for (String proj : items) {
		    	try {
		    		viewBot.tree().expandNode(proj);
		    		viewBot.tree().select(proj);
		    		// try to select project in tree (in some cases, when one project is deleted, 
		    		// the other item in tree (not being a project) is auto-deleted)
		    		
		    	} catch (WidgetNotFoundException ex) {
		    		log.warn("Attempted to delete non-existing project '"+proj+"'");
		    		continue;
		    	}
		    	deleteProject(proj,true);
		    }
	}
	/**
	 * deletes given project from workspace
	 * @param projectName
	 * @param fileSystem if true, project will be also deleted from filesystem
	 */
	public void deleteProject(String projectName, boolean fileSystem) {
		SWTBot viewBot = viewByTitle(IDELabel.View.PROJECT_EXPLORER).bot();
		SWTBotTreeItem item = viewBot.tree().expandNode(projectName);
		ContextMenuHelper.prepareTreeItemForContextMenu(viewBot.tree(), item);
		new SWTBotMenu(ContextMenuHelper.getContextMenu(viewBot.tree(), IDELabel.Menu.DELETE, false)).click();
	     shell("Delete Resources").activate();
	     if (fileSystem) {
	    	 checkBox().click();
	     }
	     new SWTOpenExt(this).finish(this,IDELabel.Button.OK);
	     new SWTUtilExt(this).waitForNonIgnoredJobs();
	     
	}
	/*
	 * Selects given project in Package Explorer
	 */
	public void selectProject(String projectName) {
		SWTBot viewBot = viewByTitle(IDELabel.View.PROJECT_EXPLORER).bot();
		viewBot.tree().expandNode(projectName).select();
	}
	/**
	 * true if resource described by parameters exists in ProjectExplorer
	 * @param projectName project name
	 * @param resource path (e.g. 'Project' 'src' 'org.jbosstools.test' 'MyClass.java')
	 * @return 
	 */
	public boolean existsResource(String... resource) {
		
		try {
			SWTBot viewBot = viewByTitle(IDELabel.View.PROJECT_EXPLORER).bot();
			SWTBotTreeItem ancestor = viewBot.tree().getTreeItem(resource[0]);
			viewBot.tree().expandNode(resource[0]);
			for (int i=1;i<resource.length;i++) {
				ancestor = getItem(ancestor, resource[i]);
				if (ancestor == null) {
					return false;
				}				
			}
			return true;
			}
			catch (WidgetNotFoundException ex) {
				ex.printStackTrace();
				return false;
			}
	}
	private SWTBotTreeItem getItem(SWTBotTreeItem ancestor, String name) {
		try {
			return ancestor.expandNode(name);
		}
		catch (WidgetNotFoundException ex) {
			return null;
		}
	}
}
