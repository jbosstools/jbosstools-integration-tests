/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.microprofiile.template;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.menu.ShellMenuItem;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.jboss.tools.maven.ui.bot.test.AbstractMavenSWTBotTest;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author zcervink@redhat.com
 * 
 */
public class ConfigPropertyTestTemplate extends CDITestBase {

	@Before
	public void prepareProject() {
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(PROJECT_NAME).select();

		beansHelper.createClass(CDI_BEAN_1_JAVA_FILE_NAME, PACKAGE_NAME);
		editResourceUtil.replaceClassContentByResource(CDI_BEAN_1_JAVA_FILE_NAME + JAVA_FILE_EXTENSION,
				readFile("resources/configProperty/CdiBean1.jav_"), false, false);
		TextEditor te = new TextEditor(CDI_BEAN_1_JAVA_FILE_NAME + JAVA_FILE_EXTENSION);
		new WaitWhile(new EditorHasValidationMarkers(te));
		te.save();
		new WaitWhile(new EditorHasValidationMarkers(te));

		new AbstractMavenSWTBotTest() {
		}.convertToMavenProject(PROJECT_NAME, "war", false);
		addDependency(PROJECT_NAME, "org.eclipse.microprofile.config", "microprofile-config-api", "2.0");
		createApplicationPropertiesFile(PROJECT_NAME);
	}

	@Test
	public void isConfigPropertyValidationAllowedTest() {
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(PROJECT_NAME)
				.getProjectItem("src/main/java", PACKAGE_NAME, CDI_BEAN_1_JAVA_FILE_NAME + JAVA_FILE_EXTENSION).open();

		ProblemsView pw = new ProblemsView();
		pw.open();
		int error_count = pw.getProblems(ProblemType.ERROR).size();
		assertEquals("There are errors after including the Microprofile @ConfigProperty annotation into the project - "
				+ pw.getProblems(ProblemType.ERROR).toString(), error_count, 0);
	}

	private void addDependency(String projectName, String groupId, String artifactId, String version) {
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenuItem("Maven", "Add Dependency").select();
		new WaitUntil(new ShellIsAvailable("Add Dependency"), TimePeriod.DEFAULT);
		new LabeledText("Group Id:").setText(groupId);
		new LabeledText("Artifact Id:").setText(artifactId);
		new LabeledText("Version: ").setText(version);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

	}

	private void createApplicationPropertiesFile(String projectName) {
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenuItem("New", "Folder").select();
		new LabeledText("Folder name:").setText("src/main/resources");
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsAvailable("New Folder"), TimePeriod.DEFAULT);

		new ShellMenuItem("File", "New", "Other...").select();
		new WaitUntil(new ShellIsAvailable("Select a wizard"), TimePeriod.DEFAULT);
		new DefaultTreeItem("JBoss Tools Web", "Properties File").select();
		new PushButton("Next >").click();
		new LabeledText("Folder:*").setText(projectName + "/src/main/resources");
		new LabeledText("Name:*").setText("application.properties");
		new PushButton("Finish").click();
		new WaitWhile(new ShellIsAvailable("New File Properties"), TimePeriod.DEFAULT);

		List<String[]> entries = new ArrayList<String[]>();
		String[] entry = { "property.key", "data" };
		entries.add(entry);
		addEntryToApplicationProperties(entries);
	}

	private void addEntryToApplicationProperties(List<String[]> entries) {
		new DefaultEditor("application.properties");
		for (String[] entry : entries) {
			new DefaultCTabItem("Properties").activate();
			new PushButton("Add").click();
			new LabeledText("Name:*").setText(entry[0]);
			new LabeledText("Value:").setText(entry[1]);
			new PushButton("Finish").click();
			new WaitWhile(new ShellIsAvailable("Add Property"), TimePeriod.DEFAULT);
		}
		new ShellMenuItem("File", "Save").select();
	}
}
