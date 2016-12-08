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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang.StringUtils;
import org.jboss.ide.eclipse.as.reddeer.server.deploy.DeployOnServer;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.jface.preference.PreferenceDialog;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.docker.reddeer.preferences.DockerComposePreferencePage;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.image.AbstractImageBotTest;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jkopriva@redhat.com
 *
 */

public class ComposeTest extends AbstractImageBotTest {

	private static final String FILE_DOCKER_COMPOSE = "docker-compose.yml";
	private static final String SYSPROP_DOCKER_COMPOSE_PATH = "dockerComposePath";
	private static final String PATH_TEST_COMPOSE = "resources/test-compose";	
	private static final String PROJECT_TEST_COMPOSE = "test-compose";
	private static final String IMAGE_NAME = "test_compose";
	private static final String URL = "http://0.0.0.0:5000/";

	@Test
	public void testCompose() {
		String dockerComposePath = System.getProperty(SYSPROP_DOCKER_COMPOSE_PATH);
		assertTrue("Please provide -D" + SYSPROP_DOCKER_COMPOSE_PATH + "=<path to docker-compose binary> in your launch parameters.", 
				!StringUtils.isBlank(dockerComposePath));

		// Set up Docker Compose location
		PreferenceDialog dialog = new WorkbenchPreferenceDialog();
		DockerComposePreferencePage composePreference = new DockerComposePreferencePage();
		dialog.open();
		dialog.select(composePreference);
		composePreference.setPathToDockerCompose(dockerComposePath);
		composePreference.apply();
		new OkButton().click();

		// Build Image
		DockerImagesTab imagesTab = openDockerImagesTab();
		buildImage(IMAGE_NAME, PATH_TEST_COMPOSE, imagesTab);
		assertConsoleSuccess();

		// Import resource folder
		importProject(PATH_TEST_COMPOSE);

		// Run Docker Compose
		runDockerCompose(PROJECT_TEST_COMPOSE, FILE_DOCKER_COMPOSE);

		// Check if application is running
		BrowserView browserView = new BrowserView();
		browserView.open();
		browserView.openPageURL(URL);
		DeployOnServer.checkBrowserForErrorPage(browserView, URL);
	}

	private void runDockerCompose(String project, String projectFile) {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(project).getProjectItem(projectFile).select();
		Menu contextMenu = new ContextMenu("Run As", "2 Docker Compose");
		contextMenu.select();
		new OkButton().click();
		try {
			new DefaultShell("Docker Compose");
			new OkButton().click();
			fail("Docker Compose has not been found! Is it installed and the path is correct?");
		} catch (SWTLayerException ex) {
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		new WaitWhile(new ConsoleHasNoChange());
	}

	private void importProject(String path) {
		new ShellMenu("File", "Open Projects from File System...").select();
		new LabeledCombo("Import source:").setText(path);
		new FinishButton().click();
		new WaitWhile(new JobIsRunning());
	}

	@After
	public void after() {
		deleteImageContainerAfter("testcompose_web_1","testcompose_redis_1","testcompose_web","test_compose", "python:2.7","redis");
		cleanUpWorkspace();
	}

}
