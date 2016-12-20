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
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement.JBossServer;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.requirements.server.IServerFamily;
import org.jboss.reddeer.requirements.server.ServerReqState;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
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
@JBossServer(state=ServerReqState.PRESENT)
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
		jbossWSRuntimePreferencePage = new JBossWSRuntimePreferencePage();
		new WorkbenchPreferenceDialog().open();
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

		new WaitWhile(new ShellWithTextIsActive(EDIT_JBOSS_WS_RUNTIME_DIALOG_TITLE));

		assertRuntimeName(runtimeEditedName);
	}

	private void testJBossWSRuntimeDeletion() {
		jbossWSRuntimePreferencePage.select(0);
		jbossWSRuntimePreferencePage.remove();

		new WaitUntil(new ShellWithTextIsActive("Confirm Runtime Delete"));
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Confirm Runtime Delete"));

		assertThat(jbossWSRuntimePreferencePage.getAllJBossWSRuntimes().size(),
				Is.is(0));
	}

	private void setRuntimeHomeFolderAccordingToRuntime() {
		jbossWsRuntimeDialog.setHomeFolder(serverReq.getConfig().getRuntime());

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
		IServerFamily serverFamily = serverReq.getConfig().getServerFamily();
		switch(serverFamily.getLabel()) {
		case "WildFly":
			if ("10.x".equals(serverFamily.getVersion())) {
				expectedVersion = "5.1.3.Final";
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

		new WaitWhile(new ShellWithTextIsActive(
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
