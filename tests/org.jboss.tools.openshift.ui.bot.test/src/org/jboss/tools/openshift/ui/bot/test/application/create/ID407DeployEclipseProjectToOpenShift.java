/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.condition.TreeHasChildren;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.condition.EditorWithTitleIsActive;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of deploying existing project to OpenShift.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID407DeployEclipseProjectToOpenShift {

	private String applicationName = "diy" + System.currentTimeMillis();
	private String secondApplicationName = "doit" + System.currentTimeMillis();
	
	public static final String HTML_TEXT = "<!doctype html> <html lang=\"en\"> <head> <meta charset=\"utf-8\"> " +
			"<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge,chrome=1\">  <title>Welcome to OpSh</title>" +
			" </head> <body>OpSh</body> </html>";
	
	@Before
	public void createProject() {
		ApplicationCreator newApplicationTemplate = new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN, false);
		newApplicationTemplate.createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, true);
		
		DeleteUtils deleteApplicaiton = 
				new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName,
						applicationName);
		deleteApplicaiton.deleteOpenShiftApplication();
		deleteApplicaiton.deleteServerAdapter();
	}
	
	@Test
	public void testCreateApplicationFromExistingProjectAndTestRemoteName() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).getDomain(DatastoreOS2.DOMAIN).select();
		
		modifyAndCommitProject();
		
		NewOpenShift2ApplicationWizard wizard = new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME, 
				DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.DIY,
				secondApplicationName, false, true, false, false, null, null, true, applicationName, 
				null, "openshift2", (String[]) null);
		
		postCreateSteps();
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN, secondApplicationName, "OpSh"), TimePeriod.VERY_LONG);
			// PASS
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been deployed successfully. Browser does not "
					+ "contain text of existing project which has been deployed.");
		}
		
		verifyRemoteName();
	}
	
	private void modifyAndCommitProject() {
		// Modify
		ProjectExplorer explorer = new ProjectExplorer();
		explorer.getProject(applicationName).getProjectItem("diy", "index.html").open();
		
		TextEditor editor = new TextEditor("index.html");
		editor.activate();
		
		new WaitUntil(new EditorWithTitleIsActive("index.html"));
		editor.setText(HTML_TEXT);
		editor.save();
		editor.close();
	
		// Commit
		explorer.open();
		explorer.getProject(applicationName).select();
		new ContextMenu("Team", "Commit...").select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Identify Yourself"), TimePeriod.NORMAL);
			new DefaultShell("Identify Yourself").setFocus();
			new PushButton("OK").click();
		} catch (WaitTimeoutExpiredException ex) {}
		
		new WorkbenchView("Git Staging").activate();
		new DefaultStyledText().setText("Commit");
		new DefaultTreeItem(new DefaultTree(), "> index.html - diy").select();
		new ContextMenu("Add to Index").select();
		new DefaultTreeItem(new DefaultTree(), ".gitignore").select();
		new ContextMenu("Add to Index").select(); 
		
		new WaitUntil(new TreeHasChildren(new DefaultTree(1)));
		
		new PushButton(OpenShiftLabel.Button.COMMIT).click();
		
		new WaitWhile(new JobIsRunning());		
	}
	
	private void postCreateSteps() {
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE), 
				TimePeriod.VERY_LONG);
			
		new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE);
		new OkButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD),
				TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD);
		new OkButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ACCEPT_HOST_KEY), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.ACCEPT_HOST_KEY);
		new YesButton().click();

		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		// PUBLISH
		ServersView servers = new ServersView();
		servers.open();

		TreeViewerHandler.getInstance().getTreeItem(new DefaultTree(),
				secondApplicationName + " at OpenShift 2").select();
		new ContextMenu("Publish").select();
		
		new DefaultShell("Publish " + applicationName + "?");
		new YesButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Attempt push force ?"), TimePeriod.LONG);
		Shell forceShell = new DefaultShell("Attempt push force ?");
		new YesButton().click();
		
		new WaitWhile(new ShellIsAvailable(forceShell), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

		// Browser need time
		AbstractWait.sleep(TimePeriod.NORMAL);
	}
	
	private void verifyRemoteName() {
		WorkbenchView gitRepoView = new WorkbenchView("Git", "Git Repositories");
		gitRepoView.open();
		
		final TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		// Workaround because of strange git tree items
		TreeItem item = null;
		for (TreeItem tmpItem: new DefaultTree().getItems()) {
			if (tmpItem.getText().contains(applicationName)) {
				item = tmpItem;
				break;
			}
		}
		final TreeItem finalItem = item;
		
		assertFalse("Git view does not containt item for application", item == null);
		
		try {
			gitRepoView.activate();
			Display.syncExec(new ResultRunnable<Boolean>() {
				
				@Override
				public Boolean run() {
				treeViewerHandler.getTreeItem(finalItem, "Remotes", "openshift2").select();
				return true;
				}
			});
			// PASS
		} catch (JFaceLayerException ex) {
			fail("There is no remote with name openshift2 for application " + applicationName);
		}
			
		gitRepoView.close();
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, secondApplicationName,
				applicationName).perform();
	}
}
