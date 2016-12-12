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
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.docker.reddeer.condition.ImageIsDeployedCondition;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class PushImageTest extends AbstractDockerBotTest {

	private static final String DOCKER_HUB_PASSWORD = "dockerHubPassword";
	private static final String DOCKER_HUB_EMAIL = "dockerHubEmail";
	private static final String DOCKER_HUB_USERNAME = "dockerHubUsername";

	private static String imageName = "test_push";
	private static String registryAccount = System.getProperty(DOCKER_HUB_USERNAME) + "@https://index.docker.io";
	private static String registryAddress = "https://index.docker.io";
	private static String imageTag = System.getProperty(DOCKER_HUB_USERNAME) + "/variables";
	private String seconds = "";
	private String imageNewTag = "";

	@Test
	public void pushImage() {
		String dockerHubUsername = System.getProperty(DOCKER_HUB_USERNAME);
		String dockerHubEmail = System.getProperty(DOCKER_HUB_EMAIL);
		String dockerHubPassword = System.getProperty(DOCKER_HUB_PASSWORD);
		if (dockerHubUsername == null || dockerHubUsername.isEmpty() || dockerHubEmail == null
				|| dockerHubEmail.isEmpty() || dockerHubPassword == null || dockerHubPassword.isEmpty()) {
			fail("At least one of credentials is null or empty! dockerHubUsername:" + dockerHubUsername + " dockerHubEmail:"
					+ dockerHubEmail + " dockerHubPassword:" + dockerHubPassword + " Aborting test...");
		}
		getConnection();
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
		setUpRegister(registryAddress, dockerHubEmail, dockerHubUsername, dockerHubPassword);
		setSecureStorage("password");
		java.util.Date date = new java.util.Date();
		seconds = String.valueOf(date.getTime());
		imageNewTag = imageTag + ":" + seconds;
		getConnection().getImage(imageName).addTagToImage(imageNewTag);
		new WaitUntil(
				new ImageIsDeployedCondition(imageTag, seconds, getConnection()), 
				TimePeriod.VERY_LONG);
		getConnection().getImage(imageTag, seconds)
				.pushImage(registryAccount, false, false);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		deleteImage(imageTag, seconds);
		getConnection().pullImage(imageTag, seconds, registryAddress);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		assertTrue("Image has not been pushed/pulled!", imageIsDeployed(imageTag));
	}

	@After
	public void after() {
		deleteRegister(registryAddress);
		deleteImageContainerAfter(imageNewTag);
		cleanUpWorkspace();
	}
}
