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
package org.jboss.tools.openshift.ui.bot.test.application.advanced;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter.Version;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID414CreateApplicationFromExistingProjectTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of deploying already built application. War archive is added to git
 * index and pushed to OpenShift where mvn build is disabled.
 *  
 * @author mlabuda@redhat.com
 *
 */
public class ID904DeployApplicationWARArchiveTest {

	private String applicationName = "eap" + System.currentTimeMillis();
	
	private String eapApplicationXhtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
			+ " <ui:composition xmlns=\"http://www.w3.org/1999/xhtml\""
			+ " xmlns:ui=\"http://java.sun.com/jsf/facelets\""
			+ "xmlns:f=\"http://java.sun.com/jsf/core\""
			+ "xmlns:h=\"http://java.sun.com/jsf/html\""
			+ "template=\"/WEB-INF/templates/default.xhtml\">"
			+ "<ui:define name=\"content\">"
			+ "<h1>Welcome to OpSh</h1>"
			+ "</ui:define></ui:composition>";
	
	@Before
	public void createJavaEEApp() {
		ID414CreateApplicationFromExistingProjectTest.createJavaEEProject(applicationName);
	}
	
	@Test
	public void testDeployApplicationWarArchive() {
		deployJavaEEApplication();
		
		addAndPushDisableMvnBuildMarker();
		modifyApplication();
		createArchive();
		publishApplication();
	}
	
	private void deployJavaEEApplication() {
		NewOpenShift2ApplicationWizard wizard = new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.JBOSS_EAP,
				applicationName, false, true, false, false, null, null, true, applicationName, 
				null, "openshift2", (String[]) null);
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD),
				TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD);
		new OkButton().click();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.ACCEPT_HOST_KEY), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.ACCEPT_HOST_KEY);
		new YesButton().click();

		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	private void addAndPushDisableMvnBuildMarker() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		projectExplorer.getProject(applicationName).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.CONFIGURE_MARKERS).select();
		
		new WaitUntil(new ShellIsAvailable("Configure OpenShift Markers for project " + applicationName),
				TimePeriod.LONG);
		
		new DefaultShell("Configure OpenShift Markers for project " + applicationName);

		// Also hot deploy marker for faster workarounds
		new DefaultTable().getItem("Skip Maven Build").setChecked(true);
		new DefaultTable().getItem("Hot Deploy").setChecked(true);
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable("Configure OpenShift Markers for project " + applicationName),
				TimePeriod.LONG);
		
		// Publish
		new ServerAdapter(Version.OPENSHIFT2, applicationName).select();
		new ContextMenu("Publish").select();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.PUBLISH_CHANGES), 
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.PUBLISH_CHANGES);
		
		assertTrue("If there is no commit message, button should be Push Only without "
				+ "pushing uncommited changes.",
				new ControlIsEnabled(new PushButton("Publish Only")).test());
		
		new DefaultStyledText(0).setText("Commit message");
		
		try {
			new WaitUntil(new ControlIsEnabled(new PushButton(
					OpenShiftLabel.Button.COMMIT_PUBLISH)), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Button to push commited changes has not been enabled.");
		}
		
		new PushButton(OpenShiftLabel.Button.COMMIT_PUBLISH).click();

		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);		
	}
	
	private void modifyApplication() {
		Project project = new ProjectExplorer().getProject(applicationName);
		project.select();
		project.getProjectItem("src", "main", "webapp", "index.html").open();
		
		TextEditor editor = new TextEditor("index.html");
		editor.setText(eapApplicationXhtml);
		editor.save();
		editor.close();
	}
	
	// return true if context menu was opened successfully, number is prefix
	private boolean openMavenBuildContextMenu(int number) {
		try {
			new ContextMenu("Run As", number + " Maven build...").select();
			return true;
		} catch (RedDeerException ex) {
			return false;
		}
	}
	
	private void createArchive() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		Project project = projectExplorer.getProject(applicationName);
		project.select();
		
		int number = 1;
		while (!openMavenBuildContextMenu(number)) {
			number++;
		}
		
		new WaitUntil(new ShellIsAvailable("Edit Configuration"), TimePeriod.LONG);
		
		new DefaultShell("Edit Configuration");
		
		new LabeledText("Goals:").setText("clean package");
		new LabeledText("Profiles:").setText("openshift");
		new CheckBox("Skip Tests").click();
		
		new PushButton("Apply").click();
		new PushButton("Run").click();	
		
		new WaitWhile(new ShellIsAvailable("Edit Configuration"), TimePeriod.LONG);
		
		ConsoleView console = new ConsoleView();
		console.open();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ConsoleHasText("[INFO] BUILD SUCCESS"), TimePeriod.LONG);
	}
	
	private void publishApplication() {
		new ProjectExplorer().getProject(applicationName).select();
		new ContextMenu("Refresh").select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new ServerAdapter(Version.OPENSHIFT2, applicationName).select();
		new ContextMenu("Publish").select();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.PUBLISH_CHANGES), 
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.PUBLISH_CHANGES);
		
		new DefaultStyledText().setText("Commit 2");
		for (TreeItem item: new DefaultTree().getItems()) {
			item.setChecked(true);
		}
		
		new PushButton(OpenShiftLabel.Button.COMMIT_PUBLISH).click();

		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		// It takes some time to sucessfully deploy application
		AbstractWait.sleep(TimePeriod.getCustom(15));
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN, applicationName, "OpSh"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been successfully.");
		}
	}

	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName,
				applicationName).perform();
	}
}
