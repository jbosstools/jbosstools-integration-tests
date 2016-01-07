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
package org.jboss.tools.openshift.ui.bot.test.application.handle;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID414CreateApplicationFromExistingProjectTest;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of adding OpenShift profile into pom.xml after creating an
 * application.
 *
 * @author mlabuda@redhat.com
 *
 */
public class ID702AddMavenProfileTest {

	private String applicationName = "eap" + System.currentTimeMillis();
	private String projectName = "eapproject" + System.currentTimeMillis();

	@Test
	public void testAdditionOfOpenShiftProfile() {
		ID414CreateApplicationFromExistingProjectTest
				.createJavaEEProject(projectName);

		NewOpenShift2ApplicationWizard wizard = new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME, 
				DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(
				OpenShiftLabel.Cartridge.JBOSS_EAP, applicationName, false, true, false, false, null,
				null, true,	projectName, null, null, (String[]) null);

		new WaitUntil(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD),
				TimePeriod.VERY_LONG);

		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD);
		new OkButton().click();

		new WaitUntil(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.ACCEPT_HOST_KEY), TimePeriod.VERY_LONG);

		new DefaultShell(OpenShiftLabel.Shell.ACCEPT_HOST_KEY);
		new YesButton().click();

		new WaitWhile(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

		Project project = new ProjectExplorer().getProject(projectName);

		project.getProjectItem("pom.xml").open();

		new DefaultCTabItem("pom.xml").activate();

		String pomText = new DefaultStyledText().getText();

		assertTrue("Maven profile has not been added into pom.xml",
				pomText.contains("<id>openshift</id>"));
		
		new DefaultEditor().close();
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName, 
				projectName).perform();	
	}
}
