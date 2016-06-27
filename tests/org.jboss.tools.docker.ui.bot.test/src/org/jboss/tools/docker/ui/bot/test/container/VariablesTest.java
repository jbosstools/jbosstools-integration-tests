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

package org.jboss.tools.docker.ui.bot.test.container;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.docker.reddeer.core.ui.wizards.RunADockerImagePageOneWizard;
import org.jboss.tools.docker.reddeer.core.ui.wizards.RunADockerImagePageTwoWizard;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class VariablesTest extends AbstractDockerBotTest {
	private static String imageName = "test_variables";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
	}

	@Test
	public void testVariables() {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		String dockerFilePath = "";
		try {
			dockerFilePath = (new File("resources/test-variables")).getCanonicalPath();
		} catch (IOException ex) {
			fail("Resource file not found!");
		}
		imageTab.buildImage(imageName, dockerFilePath);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		assertFalse("Console has no output!", consoleView.getConsoleText().isEmpty());
		assertTrue("Build has not been successful", consoleView.getConsoleText().contains("Successfully built"));

		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning());
		imageTab.runImage(imageName);
		RunADockerImagePageOneWizard firstPage = new RunADockerImagePageOneWizard();
		firstPage.setName(imageName + "_run");
		firstPage.next();
		RunADockerImagePageTwoWizard secondPage = new RunADockerImagePageTwoWizard();
		secondPage.addEnviromentVariable("FOO", "barbarbar");
		secondPage.finish();
		new WaitWhile(new JobIsRunning());
		new WaitWhile(new ConsoleHasNoChange());
		ConsoleView cview = new ConsoleView();
		cview.open();
		assertTrue("Console do not contain expected output!", cview.getConsoleText().contains("FOO is barbarbar"));
	}

	@After
	public void after() {
		deleteContainer(imageName + "_run");
		deleteImage(imageName);
		deleteImage("busybox");
		deleteConnection();
	}

}
