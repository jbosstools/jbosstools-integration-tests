/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.preferences;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.hamcrest.core.Is;
import org.jboss.ide.eclipse.as.reddeer.server.family.JBossFamily;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.eclipse.reddeer.junit.requirement.inject.InjectRequirement;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.server.ServerRequirementState;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.jboss.tools.ws.reddeer.ui.preferences.JBossWSRuntimeItem;
import org.jboss.tools.ws.reddeer.ui.preferences.JBossWSRuntimeListFieldEditor;
import org.jboss.tools.ws.reddeer.ui.preferences.JBossWSRuntimePreferencePage;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * JBossWS Preference Page is tested with following approaches:
 * 1. try to automatically generate all relevant information 
 *    about jbossws from configured runtime
 * 2. try to edit jbossws information and test it is reflected in page
 * 3. try to remove jbossws from list
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 */
@RunWith(RedDeerSuite.class)
@JBossServer(state=ServerRequirementState.PRESENT)
public class JBossWSPreferencesTest {

	@InjectRequirement
    private ServerRequirement serverReq;

	private static JBossWSRuntimePreferencePage jbossWSRuntimePreferencePage;
	private JBossWSRuntimeListFieldEditor jbossWsRuntimeDialog;

	private static final String NEW_JBOSS_WS_RUNTIME_DIALOG_TITLE =  "New JBossWS Runtime";
	private static final String EDIT_JBOSS_WS_RUNTIME_DIALOG_TITLE = "Edit JBossWS Runtime";

	private String runtimeName;
	private String runtimeVersion;
	private String runtimePath;

	private String runtimeEditedName = "modifiedRuntimeName";

	@BeforeClass
	public static void preparePrerequisites() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		jbossWSRuntimePreferencePage = new JBossWSRuntimePreferencePage(dialog);
		new WorkbenchPreferenceDialog().select(jbossWSRuntimePreferencePage);
	}

	@After
	public void cleanUpWorkspace() {
		new WorkbenchPreferenceDialog().cancel();
	}

	@Test
	public void testJBossWSPreferencePage() {
		testJBossWSGenerating();
		testJBossWSRuntimeEdition();
		testJBossWSRuntimeDeletion();
	}

	private void testJBossWSGenerating() {
		jbossWSRuntimePreferencePage.add();
		jbossWsRuntimeDialog = new JBossWSRuntimeListFieldEditor(false);

		setRuntimeHomeFolderAccordingToRuntime();
		assertRuntimeProperlyConfiguredInDialog();
		assertRuntimeConfiguredAccordingToRuntime();
	}

	private void testJBossWSRuntimeEdition() {
		jbossWSRuntimePreferencePage.select(0);
		jbossWSRuntimePreferencePage.edit();

		jbossWsRuntimeDialog = new JBossWSRuntimeListFieldEditor(true);

		jbossWsRuntimeDialog.setName(runtimeEditedName);
		jbossWsRuntimeDialog.finish();

		new WaitWhile(new ShellIsAvailable(EDIT_JBOSS_WS_RUNTIME_DIALOG_TITLE));

		assertRuntimeName(runtimeEditedName);
	}

	private void testJBossWSRuntimeDeletion() {
		jbossWSRuntimePreferencePage.select(0);
		jbossWSRuntimePreferencePage.remove();

		new DefaultShell("Confirm Runtime Delete");
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable("Confirm Runtime Delete"));

		assertThat(jbossWSRuntimePreferencePage.getAllJBossWSRuntimes().size(),
				Is.is(0));
	}

	private void setRuntimeHomeFolderAccordingToRuntime() {
		jbossWsRuntimeDialog.setHomeFolder(serverReq.getConfiguration().getRuntime());

		runtimeName = jbossWsRuntimeDialog.getName();
		runtimeVersion = jbossWsRuntimeDialog.getVersion();
		runtimePath = jbossWsRuntimeDialog.getHomeFolder();
	}

	private void assertRuntimeProperlyConfiguredInDialog() {
		assertTrue("JBoss WS Runtime name was not automatically generated",
					jbossWsRuntimeDialog.getName() != null &&
					!jbossWsRuntimeDialog.getName().isEmpty());

		assertThat(jbossWsRuntimeDialog.getRuntimeImplementation(),
				Is.is("JBoss Web Services - Stack CXF Runtime Client"));
			
		String runtimeVersion = jbossWsRuntimeDialog.getRuntimeVersion();

		String expectedVersion;
		JBossFamily serverFamily = serverReq.getConfiguration().getFamily();
		switch(serverFamily.getLabel()) {
		/*
		 * Depending on wf versions, Stack CFX client version differs:
		 * WF 19, 20 -> 5.4.1
		 * WF 17, 18 -> 5.3.0
		 * WF 15,16 -> 5.2.4
		 * WF 13 -> 5.2.1
		 */
		case "WildFly":
			String version = serverReq.getConfiguration().getVersion();
			if (version.startsWith("10")) {
				expectedVersion = "5.1.3.Final";
			} else if (version.startsWith("13")) {
				expectedVersion = "5.2.1.Final";
			} else if (version.startsWith("15") || version.startsWith("16")) {
				expectedVersion = "5.2.4.Final";
			} else if (version.startsWith("17") || version.startsWith("18")) {
				expectedVersion = "5.3.0.Final";
			} else if (version.startsWith("19") || version.startsWith("20")) {
				expectedVersion = "5.4.1.Final";
			} else if (version.startsWith("21") || version.startsWith("22") || version.startsWith("23.0.0")) {
				expectedVersion = "5.4.2.Final";
			} else if (version.startsWith("23")) {
				expectedVersion = "5.4.3.Final";
			} else if (version.startsWith("24")) {
				expectedVersion = "5.4.4.Final"; 
			} else {
				expectedVersion = "5.0.0.Final";
			}
			break;
		case "JBoss Enterprise Application Platform":
			expectedVersion = "4.2.3.Final-redhat-1";
			break;
		case "JBoss AS":
			expectedVersion = "4.0.2.GA";
			break;
		default:
			fail("Server was not recognized");
			expectedVersion = "";
		}
		assertTrue("Unknown runtime version: " + runtimeVersion, 
				Is.is(expectedVersion).matches(runtimeVersion));
	}

	private void assertRuntimeConfiguredAccordingToRuntime() {
		jbossWsRuntimeDialog.finish();

		new WaitWhile(new ShellIsAvailable(
				NEW_JBOSS_WS_RUNTIME_DIALOG_TITLE));

		assertThat(jbossWSRuntimePreferencePage.
				getAllJBossWSRuntimes().size(), Is.is(1));

		JBossWSRuntimeItem item = jbossWSRuntimePreferencePage.
				getAllJBossWSRuntimes().iterator().next();
		assertRuntimeName(item, runtimeName);
		assertRuntimeVersion(item, runtimeVersion);
		assertRuntimePath(item, runtimePath);
		
	}

	private void assertRuntimeName(String expectedName) {
		assertRuntimeName(jbossWSRuntimePreferencePage.getAllJBossWSRuntimes().get(0), 
				expectedName);
	}

	private void assertRuntimeName(JBossWSRuntimeItem item, String expectedName) {
		assertThat(item.getName(), Is.is(expectedName));
	}

	private void assertRuntimeVersion(JBossWSRuntimeItem item, String expectedVersion) {
		assertThat(item.getVersion(), Is.is(expectedVersion));
	}

	private void assertRuntimePath(JBossWSRuntimeItem item, String expectedPath) {
		assertThat(item.getPath(), Is.is(expectedPath));
	}
}
