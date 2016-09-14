/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.docker.ui.bot.test.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.jface.preference.PreferenceDialog;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.docker.reddeer.preferences.DockerComposePreferencePage;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva@redhat.com
 *
 */

public class ComposeTest extends AbstractDockerBotTest {
	private static String imageName = "test_compose";
	private static final String URL = "http://0.0.0.0:5000/";

	@Before
	public void before() {
		prepareWorkspace();
	}

	@Test
	public void testCompose() {
		String dockerComposePath = System.getProperty("dockerComposePath");
		
		//Set up Docker Compose location
		PreferenceDialog dialog = new WorkbenchPreferenceDialog();
		DockerComposePreferencePage composePreference = new DockerComposePreferencePage();
		dialog.open();
		dialog.select(composePreference);
		composePreference.setPathToDockerCompose(dockerComposePath);
		composePreference.apply();
		new OkButton().click();

		//Build Image
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		String dockerFilePath = "";
		try {
			dockerFilePath = (new File("resources/test-compose")).getCanonicalPath();
		} catch (IOException ex) {
			fail("Resource file not found!");
		}
		imageTab.buildImage(imageName, dockerFilePath);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		assertFalse("Console has no output!", consoleView.getConsoleText().isEmpty());
		assertTrue("Build has not been successful", consoleView.getConsoleText().contains("Successfully built"));

		//Import resource folder
		new ShellMenu("File", "Open Projects from File System...").select();
		new LabeledCombo("Import source:").setText("resources/test-compose");
		new FinishButton().click();
		new WaitWhile(new JobIsRunning());

		//Run Docker Compose
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject("test-compose").getProjectItem("docker-compose.yml").select();
		Menu contextMenu = new ContextMenu("Run As", "2 Docker Compose");
		contextMenu.select();
		new OkButton().click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		new WaitWhile(new ConsoleHasNoChange());
		
		//Check if application is running
		BrowserView browserView = new BrowserView();
		browserView.open();
		browserView.activate();
		browserView.openPageURL(URL);
		checkBrowserForErrorPage(browserView, URL);
	}

	@After
	public void after() {
		deleteContainer("testcompose_web");
		deleteContainer("testcompose_redis");
		deleteImage("testcompose_web");
		deleteImage("test_compose");
		deleteImage("python");
		deleteImage("redis");
		cleanUpWorkspace();
	}

}
