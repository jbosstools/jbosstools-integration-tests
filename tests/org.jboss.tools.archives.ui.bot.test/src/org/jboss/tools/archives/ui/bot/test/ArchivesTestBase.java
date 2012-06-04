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

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.archives.ui.bot.test.explorer.ProjectArchivesExplorer;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
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
		view.show();
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
		for (SWTBotTreeItem ti : server.getItems()) {
			if (ti.getText().contains(archive)) {
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
	
	public void assertClearErrorLog() {
		assertTrue("Error log contains some records", 
				new ErrorLogView().getRecordCount() == 0);
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
	
}
