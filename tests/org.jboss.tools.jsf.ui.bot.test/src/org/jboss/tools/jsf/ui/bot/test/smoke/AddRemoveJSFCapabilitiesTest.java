/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jsf.ui.bot.test.smoke;

import java.io.File;

import org.apache.log4j.Logger;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.core.handler.TreeItemHandler;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.condition.ProjectExists;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.jsf.reddeer.ui.wizard.project.ImportProjectWizard;
import org.jboss.tools.jst.reddeer.web.ui.navigator.WebProjectsNavigator;
import org.jboss.tools.jst.reddeer.web.ui.wizards.project.ImportWebProjectWizardPage;
import org.jboss.tools.ui.bot.ext.SWTJBTExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;
/**
 * Test adding and removing JSF Capabilities from/to JSF Project
 * 
 * @author Vladimir Pakan
 */

public class AddRemoveJSFCapabilitiesTest extends JSFAutoTestCase {

	private Logger log = Logger.getLogger(AddRemoveJSFCapabilitiesTest.class);
	@Test
	public void testAddRemoveJSFCapabilities() {
		boolean jbdsIsRunning = SWTJBTExt.isJBDSRun();
		removeJSFCapabilities(jbdsIsRunning);
		addJSFCapabilities();
		// Test add/remove JSF capabilities after project is closed and reopened
		closeOpenJsfProject();
		removeJSFCapabilities(jbdsIsRunning);
		addJSFCapabilities();
		// Test import of deleted JSF project
		deleteJsfProject();
		importJsfProject();
	}

	/**
	 * Import existing JSF Project to Workspace
	 */
	private void importJsfProject() {

		String[] parts = System.getProperty("eclipse.commands").split("\n");

		int index = 0;

		for (index = 0; parts.length > index + 1 && !parts[index].equals("-data"); index++) {
			// do nothing just go through
		}

		if (parts.length > index + 1) {
			String webXmlFileLocation = SWTUtilExt.getTestPluginLocation(JBT_TEST_PROJECT_NAME) + File.separator
					+ "WebContent" + File.separator + "WEB-INF" + File.separator + "web.xml";

			ImportProjectWizard importProjectWizard = new ImportProjectWizard();
			importProjectWizard.open();

			new ImportWebProjectWizardPage().setWebXmlLocation(webXmlFileLocation);
			importProjectWizard.next();
			importProjectWizard.finish();
			new DefaultShell("Warning");
			new PushButton("Continue").click();
			// Start Application Server
			ServersView serversView = new ServersView();
			serversView.open();
			Server server = serversView.getServer(serverRequirement.getServerNameLabelText(serverRequirement.getConfig()));
			server.start();
			packageExplorer.open();
			packageExplorer.getProject(JBT_TEST_PROJECT_NAME).select();
			new ContextMenu("Run As","1 Run on Server").select();
			new DefaultShell("Run On Server");
			new DefaultTreeItem("localhost",serverRequirement.getRuntimeNameLabelText(serverRequirement.getConfig())).select();
			new PushButton("Next >").click();
			new PushButton("Finish").click();
			new WaitWhile(new ShellWithTextIsActive("Run On Server"));
			new WaitUntil(new ConsoleHasText("Deployed \""+JBT_TEST_PROJECT_NAME+".war\""),TimePeriod.LONG);
			// Check Browser Content
			new DefaultEditor("Input User Name Page");
			String browserText = new InternalBrowser().getText();
			server.delete(true);
			assertTrue("Displayed HTML page has wrong content", (browserText != null)
					&& (browserText.toLowerCase().indexOf("<title>input user name page</title>") > -1));
		} else {
			throw new RuntimeException("eclipse.commands property doesn't contain -data option");
		}

	}

	/**
	 * Delete JSF Project from workspace
	 */
	private void deleteJsfProject() {
		removeJSFTestProjectFromServers();
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).delete(false);
	}

	/**
	 * Remove JSF Capabilities from JSF Project
	 * 
	 * @param jbdsIsRunning
	 */
	private void removeJSFCapabilities(boolean jbdsIsRunning) {
		removeJSFTestProjectFromServers();
		WebProjectsNavigator webProjectsNavigator = new WebProjectsNavigator();
		webProjectsNavigator.open();
		webProjectsNavigator.getProject(JBT_TEST_PROJECT_NAME).select();

		TreeItemHandler.getInstance()
			.click(webProjectsNavigator.getProject(JBT_TEST_PROJECT_NAME).getTreeItem().getSWTWidget());
		
		new ContextMenu(IDELabel.Menu.WEB_PROJECT_JBT_JSF, IDELabel.Menu.JBT_REMOVE_JSF_CAPABILITIES).select();

		new DefaultShell("Confirmation");
		new OkButton().click();

		new WaitWhile(new ProjectExists(JBT_TEST_PROJECT_NAME,webProjectsNavigator),TimePeriod.NORMAL,false);

		assertTrue(
				"Project " + JBT_TEST_PROJECT_NAME
						+ " was not removed from Web Projects view after JSF Capabilities were removed.",
				!webProjectsNavigator.containsProject(JBT_TEST_PROJECT_NAME));
	}
	
	/**
	 * Add JSF Capabilities to JSF Project
	 */
	private void addJSFCapabilities() {

		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).activateWrappingView();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).select();
		
		log.info("Adding JSF Capabilities to project " + JBT_TEST_PROJECT_NAME);
		new ContextMenu(IDELabel.Menu.PACKAGE_EXPLORER_CONFIGURE, IDELabel.Menu.ADD_JSF_CAPABILITIES).select();

		try {
			new WaitUntil(
				new ShellWithTextIsActive(IDELabel.Shell.PROPERTIES_FOR + " " + JBT_TEST_PROJECT_NAME + " (Filtered)"),
				TimePeriod.NORMAL);
			log.info("Properties dialog was opened. Trying to close it");
			new DefaultShell(IDELabel.Shell.PROPERTIES_FOR + " " + JBT_TEST_PROJECT_NAME + " (Filtered)");
			new CancelButton().click();
		} catch (WaitTimeoutExpiredException wtee) {
			log.info("Properties dialog was not opened");
		}
		
		WebProjectsNavigator webProjectsNavigator = new WebProjectsNavigator();
		webProjectsNavigator.open();
		
		assertTrue("JSF Capabilities were not added to project " + JBT_TEST_PROJECT_NAME,
			webProjectsNavigator.containsProject(JBT_TEST_PROJECT_NAME));

	}
	/**
	 * Close and reopen JSF test project
	 */
	private void closeOpenJsfProject() {
		packageExplorer.open();
		packageExplorer.getProject(JBT_TEST_PROJECT_NAME).select();
		new ContextMenu(IDELabel.Menu.CLOSE_PROJECT).select();
		new WaitUntil (new JobIsRunning());
		new ContextMenu(IDELabel.Menu.OPEN_PROJECT).select();
		new WaitUntil (new JobIsRunning());
	}

	/**
	 * Remove JSF Test Project from server
	 */
	private void removeJSFTestProjectFromServers() {
		ServersView serversView = new ServersView();
		serversView.open();
		Server server = serversView.getServer(serverRequirement.getServerNameLabelText(serverRequirement.getConfig()));
		try{
			server.getModule(JBT_TEST_PROJECT_NAME).remove();
		} catch (EclipseLayerException ele){
			// do nothing JBT project was not deployed to server
		}
	}

}
