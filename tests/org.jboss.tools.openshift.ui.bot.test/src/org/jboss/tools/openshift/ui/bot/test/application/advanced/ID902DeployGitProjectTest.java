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

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.eclipse.condition.ProjectExists;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.SystemProperties;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of deploying a github based project.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID902DeployGitProjectTest {

	private String projectName = "jboss-javaee6-webapp";
	private String applicationName = "jbosseapapp" + System.currentTimeMillis();
	private String gitProjectURI = "git@github.com:mlabuda/jboss-eap-application.git";
	
	@Test
	public void testDeployGitBasedProject() {
		TestUtils.cleanupGitFolder("jboss-eap-application");
		new WorkbenchShell().setFocus();
		
		importGitProject();
		
		NewOpenShift2ApplicationWizard wizard = new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME, 
				DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		wizard.openWizardFromExplorer();
		
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.JBOSS_EAP, applicationName, 
				false, true, false, false, null, null, true, projectName, null, "openshift22", (String[]) null);
		
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
		
		wizard.verifyApplication(applicationName, projectName);
		wizard.verifyServerAdapter(applicationName, projectName);
	}
	
	private void importGitProject() {
		new ShellMenu("File", "Import...").select();
		
		new WaitUntil(new ShellIsAvailable("Import"), TimePeriod.LONG);
		
		new DefaultShell("Import").setFocus();
		new DefaultTreeItem("Git", "Projects from Git (with smart import)").select();
		
		new WaitUntil(new ControlIsEnabled(new NextButton()));
		new NextButton().click();
		
		new DefaultTree().selectItems(new DefaultTreeItem("Clone URI")); 
		
		new WaitUntil(new ControlIsEnabled(new NextButton()));
		new NextButton().click();
		
		new LabeledText("URI:").setText(gitProjectURI);
		
		try {
			new DefaultShell(OpenShiftLabel.Shell.SECURE_STORAGE_PASSWORD);
			new DefaultText(0).setText(SystemProperties.SECURE_STORAGE_PASSWORD);
			
			new WaitUntil(new ControlIsEnabled(new OkButton()));
			
			new OkButton().click();
			
			new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.SECURE_STORAGE_PASSWORD));
		} catch (RedDeerException ex) {
			// do nothing, if failed, there was no secure storage shell opened
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new WaitUntil(new ControlIsEnabled(new NextButton()));
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		new WaitUntil(new ControlIsEnabled(new NextButton()));
		new NextButton().click();
		
		new WaitUntil(new ControlIsEnabled(new NextButton()));
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new WaitUntil(new ControlIsEnabled(new FinishButton()), TimePeriod.LONG);
		new FinishButton().click();
		
		new WaitUntil(new ProjectExists(projectName), TimePeriod.VERY_LONG);
		
		new ProjectExplorer().getProject(projectName).refresh();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName, projectName).perform();
	}
}
