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

package org.jboss.tools.docker.ui.bot.test.image;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.docker.reddeer.ui.DockerExplorer;
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


public class PushImageTest extends AbstractDockerBotTest {
	private static String imageName = "test_push";
	private static String registryAccount = System.getProperty("dockerHubUsername") + "@https://index.docker.io";
	private static String registryAddress = "https://index.docker.io";
	private static String imageTag = System.getProperty("dockerHubUsername") + "/variables";
	

	@Before
	public void before() {
		prepareWorkspace();
	}

	@Test
	public void pushImage() {
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
		String dockerHubUsername = System.getProperty("dockerHubUsername");
		String dockerHubEmail = System.getProperty("dockerHubEmail");
		String dockerHubPassword = System.getProperty("dockerHubPassword");
		setUpRegister(registryAddress, dockerHubEmail, dockerHubUsername, dockerHubPassword);
		setSecureStorage("password");
		DockerExplorer explorer = new DockerExplorer();
		explorer.activate();
		java.util.Date date= new java.util.Date();
		long seconds = date.getTime();
		String imageNewTag = imageTag + ":" + seconds;
		explorer.addTagToImage(getDockerServer(), imageName, imageNewTag);
		explorer.pushImage(getDockerServer(), imageNewTag, registryAccount, false, false);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		deleteImage(imageTag);
		explorer.activate();
		explorer.selectConnection(getDockerServer());
		explorer.pullImage(getDockerServer(), registryAddress, imageNewTag);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		assertTrue("Image has not been pushed/pulled!",imageIsDeployed(imageNewTag));
	}

	@After
	public void after() {
		deleteRegister(registryAddress);
		deleteImage(imageName);
		deleteImage("jboss/base-jdk");
		cleanUpWorkspace();
	}

}
