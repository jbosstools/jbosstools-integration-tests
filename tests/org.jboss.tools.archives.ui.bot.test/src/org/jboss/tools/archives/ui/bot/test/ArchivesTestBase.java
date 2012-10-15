/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.archives.ui.bot.test.explorer.ProjectArchivesExplorer;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.ProgressInformationShellIsActiveCondition;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.jboss.tools.ui.bot.ext.helper.TreeHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ErrorLogView;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author jjankovi
 *
 */
@Require(clearProjects = true, perspective = "Java")
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({ ArchivesAllBotTests.class })
public class ArchivesTestBase extends SWTTestExt {

	protected ProjectArchivesView openProjectArchivesView() {
		ProjectArchivesView view = new ProjectArchivesView();
		return view;
	}
	
	protected ProjectArchivesView viewForProject(String projectName) {
		ProjectArchivesView view = openProjectArchivesView();
		projectExplorer.selectProject(projectName);
		return view;
	}
	
	protected ProjectArchivesExplorer explorerForProject(String projectName) {
		return new ProjectArchivesExplorer(projectName);
	}
	
	protected void assertItemExistsInView(ProjectArchivesView view, String... path) {
		assertTrue("Item " + path[path.length-1] + 
				" does not exist in Project Archives View", view.itemExists(path));
	}
	
	protected void assertItemExistsInExplorer(ProjectArchivesExplorer explorer, String... path) {		
		assertTrue("Item " + path[path.length-1] + 
				" does not exist in Project Archives explorer", explorer.itemExists(path));
	}
	
	protected void assertItemNotExistsInView(ProjectArchivesView view, String... path) {
		assertFalse("Item " + path[path.length-1] + 
				" should not exist in Project Archives View", view.itemExists(path));
	}
	
	protected void assertItemNotExistsInExplorer(ProjectArchivesExplorer explorer, String... path) {
		assertFalse("Item " + path[path.length-1] + 
				" should not exist in Project Archives explorer", explorer.itemExists(path));
	}
	
	protected void assertArchiveIsDeployed(String archive) {
		ServersView serversView = showServersView();
		SWTBotTreeItem server = findConfiguredServer(serversView);
		server.collapse();
		server.expand();
		boolean found = false;
		for (String node : server.getNodes()) {
			if (node.contains(archive)) {
				found = true;
				break;
			}
		}
		assertTrue(archive + " was not deployed", found);
	}
	
	protected void removeArchiveFromServer(String archive) {
		ServersView serversView = showServersView();
		serversView.removeProjectFromServers(archive);
	}
	
	protected SWTBotTreeItem findConfiguredServer(ServersView serversView) {
		SWTBotTreeItem server = null;
		try {
			server = serversView. findServerByName(serversView.bot().tree(), 
				configuredState.getServer().name);			
		} catch (WidgetNotFoundException exc) {
			fail("Server is not configured - missing in servers view");
		}
		return server;
	}
	
	private ServersView showServersView() {
		ServersView serversView = new ServersView();
		serversView.show();
		return serversView;
	}
	
	public void assertClearArchivesErrorLog() {
		
		assertTrue("Error log contains archive errors", 
				countOfArchivesErrors() == 0);
	}
	
	public static void showErrorView() {
		new ErrorLogView().show();
	}
	
	public static void clearErrorView() {
		new ErrorLogView().clear();
	}
	
	protected static void importProject(String projectName) {
		
		String location = "/resources/prj/" + projectName;
		importProject(projectName, location, projectName);
	}
	
	protected static void importProject(String projectName, 
			String projectLocation, String dir) {
		
		ImportHelper.importProject(projectLocation, dir, Activator.PLUGIN_ID);
		eclipse.addConfiguredRuntimeIntoProject(projectName, 
				configuredState.getServer().name);
	}
	
	protected static void importProjectWithoutRuntime(String projectName) {
		
		String location = "/resources/prj/" + projectName;
		importProjectWithoutRuntime(projectName, location, projectName);
	}
	
	protected static void importProjectWithoutRuntime(String projectName, 
			String projectLocation, String dir) {
		ImportHelper.importProject(projectLocation, dir, Activator.PLUGIN_ID);
	}
	
	protected void addArchivesSupport(String projectName) {
		addRemoveArchivesSupport(projectName, true);
	}
	
	protected void removeArchivesSupport(String projectName) {
		addRemoveArchivesSupport(projectName, false);
	}
	
	protected boolean archiveExplorerExists(String projectName) {
		SWTBot bot = projectExplorer.bot();
		try {
			TreeHelper.expandNode(bot, projectName, "Project Archives");
			return true;
		} catch (WidgetNotFoundException exc) {
			return false;
		}
	}
	
	private void addRemoveArchivesSupport(String projectName, boolean add) {
		projectExplorer.selectProject(projectName);
		SWTBotTree tree = projectExplorer.bot().tree();
		SWTBotTreeItem item = tree.getTreeItem(projectName);
		item.expand();
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, item);
		SWTBotMenu menu = new SWTBotMenu(
				ContextMenuHelper.getContextMenu(
				tree, IDELabel.Menu.PACKAGE_EXPLORER_CONFIGURE, false));
		if (add) {
			menu.menu(IDELabel.Menu.ADD_ARCHIVE_SUPPORT).click();
		} else {
			menu.menu(IDELabel.Menu.REMOVE_ARCHIVE_SUPPORT).click();
		}
		bot.waitWhile(new ProgressInformationShellIsActiveCondition(), TaskDuration.LONG.getTimeout());
	}
	
	private int countOfArchivesErrors() {
		ErrorLogView errorLog = new ErrorLogView();
		int archivesErrorsCount = 0;
		for (SWTBotTreeItem ti : errorLog.getMessages()) {
			if (ti.getText().contains("org.jboss.ide.eclipse.archives")) {
				archivesErrorsCount++;
			}
		}
		return archivesErrorsCount;
	}
	
}
