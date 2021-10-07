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
package org.jboss.tools.maven.ui.bot.test.utils;

import static org.junit.Assert.assertEquals;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.core.exception.CoreLayerException;
import org.eclipse.reddeer.eclipse.condition.ProjectExists;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.swt.condition.ControlIsEnabled;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.combo.LabeledCombo;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.swt.impl.text.LabeledText;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

/**
 * 
 * @author zcervink@redhat.com
 * 
 */
public class MavenProjectHelper {

	public static void addDependency(String projectName, String groupId, String artifactId, String version) {
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

	public static void convertToMavenProject(String projectName, String defaultPackaging, boolean withDependencies) {
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		new WaitUntil(new ProjectExists(projectName));
		pexplorer.getProject(projectName).select();
		new ContextMenuItem("Configure", "Convert to Maven Project").select();
		new DefaultShell("Create new POM");
		assertEquals("Project " + projectName + " packaging should be set to " + defaultPackaging, defaultPackaging,
				new LabeledCombo(new DefaultGroup("Artifact"), "Packaging:").getText());
		new PushButton("Finish").click();
		try {
			new DefaultShell("Convert to Maven Dependencies");
			new WaitUntil(new ControlIsEnabled(new PushButton("Finish")), TimePeriod.LONG);
			if (withDependencies) {
				new PushButton("Finish").click();
			} else {
				new PushButton("Skip Dependency Conversion").click();
			}
			new WaitWhile(new ShellIsAvailable("Convert to Maven Dependencies"));
		} catch (CoreLayerException ex) {

		} finally {
			new WaitWhile(new ShellIsAvailable("Create new POM"));
			new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		}
	}

	public static void updateConf(String projectName) {
		updateConf(projectName, false);
	}

	public static void updateConf(String projectName, boolean forceDependencies) {
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(projectName).select();
		new ContextMenuItem("Maven", "Update Project...").select();
		new WaitUntil(new ShellIsAvailable("Update Maven Project"), TimePeriod.LONG);
		new CheckBox("Force Update of Snapshots/Releases").toggle(forceDependencies);
		new PushButton("OK").click();
		new WaitWhile(new ShellIsAvailable("Update Maven Project"), TimePeriod.DEFAULT);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
}
