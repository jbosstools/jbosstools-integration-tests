/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.central.test.ui.reddeer;

import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.m2e.core.ui.preferences.MavenSettingsPreferencePage;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.central.reddeer.api.ExamplesOperator;
import org.jboss.tools.central.reddeer.projects.ArchetypeProject;
import org.jboss.tools.central.test.ui.reddeer.projects.AngularJSForge;
import org.jboss.tools.central.test.ui.reddeer.projects.HTML5Project;
import org.jboss.tools.central.test.ui.reddeer.projects.JavaEEWebProject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author rhopp
 * @contributor jkopriva@redhat.com
 *
 */

public class ArchetypesTest {

	private static final String CENTRAL_LABEL = "Red Hat Central";
	private static final String MAVEN_SETTINGS_PATH = System.getProperty("maven.config.file");
	private static Map<org.jboss.tools.central.reddeer.projects.Project, List<String>> projectWarnings = new HashMap<org.jboss.tools.central.reddeer.projects.Project, List<String>>();
	
	@BeforeClass
	public static void setup() {
		String mvnConfigFileName = new File(MAVEN_SETTINGS_PATH).getAbsolutePath();
		WorkbenchPreferenceDialog preferenceDialog = new WorkbenchPreferenceDialog();
		preferenceDialog.open();
		MavenSettingsPreferencePage prefPage = new MavenSettingsPreferencePage(preferenceDialog);
		preferenceDialog.select(prefPage);
		prefPage.setUserSettingsLocation(mvnConfigFileName);
		preferenceDialog.ok();
		new DefaultToolItem(new WorkbenchShell(), CENTRAL_LABEL).click();
		// activate central editor
		new DefaultEditor(CENTRAL_LABEL);
	}

	@After
	public void teardown() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		for (Project p : new ProjectExplorer().getProjects()) {
			p.delete(true);
		}
		new DefaultToolItem(new WorkbenchShell(), CENTRAL_LABEL).click();
		// activate central editor
		new DefaultEditor(CENTRAL_LABEL);
	}
	
	@AfterClass
	public static void teardownClass() {
		StringBuilder sb = new StringBuilder();
		boolean fail = false;
		for (Entry<org.jboss.tools.central.reddeer.projects.Project, List<String>> projectWarning : projectWarnings
				.entrySet()) {
			sb.append(projectWarning.getKey().getName() + "\n\r");
			if (!projectWarning.getValue().isEmpty()) fail = true;
			for (String warning : projectWarning.getValue()) {
				sb.append("\t" + warning + "\n\r");
			}
		}
		projectWarnings.clear();
		assertFalse(sb.toString(), fail);
	}
	
	@Test
	public void HTML5ProjectTest() {
		ArchetypeProject project = new HTML5Project();
		importArchetypeProject(project);
	}
	
	@Test
	public void AngularJSForgeTest(){
		ArchetypeProject project = new AngularJSForge();
		importArchetypeProject(project);
	}

	@Test
	public void JavaEEWebProjectTest() {
		importArchetypeProject(new JavaEEWebProject(false));
	}

	@Test
	public void JavaEEWebProjectBlankTest() {
		importArchetypeProject(new JavaEEWebProject(true));
	}

//	@Test
//	public void HybridMobileTest(){
//		try{
//			new DefaultHyperlink("Hybrid Mobile Project").activate();
//			new WaitUntil(new ShellWithTextIsActive("Information"));
//			//HMT is not installed
//			new PushButton("No").click();
//		}catch(WaitTimeoutExpiredException ex){
//			//TODO check whether this is OK.
//			new DefaultShell().close();
//		}
//	}
	
	private void importArchetypeProject(ArchetypeProject project) {
		ExamplesOperator.getInstance().importArchetypeProject(project);
		projectWarnings.put(project, ExamplesOperator.getInstance().getAllWarnings());
	}
}
