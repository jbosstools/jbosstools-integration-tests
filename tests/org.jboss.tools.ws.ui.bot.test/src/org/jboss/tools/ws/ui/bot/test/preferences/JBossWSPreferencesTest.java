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

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.core.Is;
import org.hamcrest.core.AnyOf;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ws.reddeer.ws.ui.JBossWSRuntimeItem;
import org.jboss.tools.ws.reddeer.ws.ui.JBossWSRuntimeListFieldEditor;
import org.jboss.tools.ws.reddeer.ws.ui.JBossWSRuntimePreferencePage;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * JBossWS Preference Page is tested with following approaches:
 * 1. try to automatically generate all relevant information 
 *    about jbossws from configured runtime
 * 2. try to edit jbossws information and test it is reflected in page
 * 3. try to remove jbossws from list    
 * 
 * @author jjankovi
 *
 */
@Require(server=@Server(state=ServerState.NotRunning))
public class JBossWSPreferencesTest extends WSTestBase {

	private static JBossWSRuntimePreferencePage jbossWSRuntimePreferencePage;
	private JBossWSRuntimeListFieldEditor jbossWsRuntimeDialog;
	
	private static final String NEW_JBOSS_WS_RUNTIME_DIALOG_TITLE = 
			"New JBossWS Runtime";
	private static final String EDIT_JBOSS_WS_RUNTIME_DIALOG_TITLE = 
			"Edit JBossWS Runtime";
	
	
	private String runtimeName;
	private String runtimeVersion;
	private String runtimePath;
	
	private String runtimeEditedName = "modifiedRuntimeName";
	
	@Before
	public void setup() {
		
	}
	
	@After
	public void cleanup() {
		
	}
	
	@BeforeClass
	public static void preparePrerequisites() {
		jbossWSRuntimePreferencePage = new JBossWSRuntimePreferencePage();
		jbossWSRuntimePreferencePage.open();
	}
	
	@AfterClass
	public static void cleanUpWorkspace() {
		jbossWSRuntimePreferencePage.cancel();
	}
	
	@Test
	public void testJBossWSPreferencePage() {
		testJBossWSGenerating();
		testJBossWSRuntimeEdition();
		testJBossWSRuntimeDeletion();
	}
	
	private void testJBossWSGenerating() {
		jbossWSRuntimePreferencePage.add();
		new WaitUntil(new ShellWithTextIsActive(
				NEW_JBOSS_WS_RUNTIME_DIALOG_TITLE));
		jbossWsRuntimeDialog = 
				new JBossWSRuntimeListFieldEditor(false);
		
		setRuntimeHomeFolderAccordingToRuntime();
		assertRuntimeProperlyConfiguredInDialog();
		assertRuntimeConfiguredAccordingToRuntime();
	}
	
	private void testJBossWSRuntimeEdition() {
		jbossWSRuntimePreferencePage.select(0);
		jbossWSRuntimePreferencePage.edit();
		
		new WaitUntil(new ShellWithTextIsActive(
				EDIT_JBOSS_WS_RUNTIME_DIALOG_TITLE));
		jbossWsRuntimeDialog = 
				new JBossWSRuntimeListFieldEditor(true);
		
		jbossWsRuntimeDialog.setName(runtimeEditedName);
		jbossWsRuntimeDialog.finish();
		
		new WaitWhile(new ShellWithTextIsActive(
				EDIT_JBOSS_WS_RUNTIME_DIALOG_TITLE));
		
		assertRuntimeName(runtimeEditedName);
	}
	
	private void testJBossWSRuntimeDeletion() {
		jbossWSRuntimePreferencePage.select(0);
		jbossWSRuntimePreferencePage.remove();
		
		new WaitUntil(new ShellWithTextIsActive("Confirm Runtime Delete"));
		new PushButton(IDELabel.Button.OK).click();
		new WaitWhile(new ShellWithTextIsActive("Confirm Runtime Delete"));
		
		assertThat(jbossWSRuntimePreferencePage.
				getAllJBossWSRuntimes().size(), Is.is(0));
	}

	private void setRuntimeHomeFolderAccordingToRuntime() {
		jbossWsRuntimeDialog.setHomeFolder(
				configuredState.getServer().runtimeLocation);
		
		runtimeName = jbossWsRuntimeDialog.getName();
		runtimeVersion = jbossWsRuntimeDialog.getVersion();
		runtimePath = jbossWsRuntimeDialog.getHomeFolder();
	}
	
	/**
	 * TODO create better runtime version assertion that will assert only with expected version defined by used server
	 * (table/map) 
	 */
	private void assertRuntimeProperlyConfiguredInDialog() {
		assertTrue("JBoss WS Runtime name was not automatically generated",
					jbossWsRuntimeDialog.getName() != null &&
					!jbossWsRuntimeDialog.getName().isEmpty());
		
		assertThat(jbossWsRuntimeDialog.getRuntimeImplementation(),
				Is.is("JBoss Web Services - Stack CXF Runtime Client"));
			
		String runtimeVersion = jbossWsRuntimeDialog.getRuntimeVersion();
		assertTrue("Unknown runtime version: " + runtimeVersion, AnyOf
				.anyOf(Is.is("4.1.3.Final-redhat-3"), // EAP
						Is.is("4.0.2.GA") // AS
						)
				.matches(runtimeVersion));
				
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
		assertRuntimeName(jbossWSRuntimePreferencePage.
				getAllJBossWSRuntimes().iterator().next(), 
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
