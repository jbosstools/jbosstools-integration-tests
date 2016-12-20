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

import org.apache.commons.lang.StringUtils;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.tools.docker.reddeer.condition.ImageIsDeployedCondition;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 * @contributor adietish@redhat.com
 *
 */

public class PushImageTest extends AbstractImageBotTest {

	private static final String DOCKERFILE_FOLDER = "resources/test-variables";
	private static final String DOCKER_HUB_PASSWORD = "dockerHubPassword";
	private static final String DOCKER_HUB_EMAIL = "dockerHubEmail";
	private static final String DOCKER_HUB_USERNAME = "dockerHubUsername";

	private static final String IMAGE_NAME = "test_push";
	private static final String REGISTRY_ACCOUNT = System.getProperty(DOCKER_HUB_USERNAME) + "@https://index.docker.io";
	private static final String IMAGE_TAG = System.getProperty(DOCKER_HUB_USERNAME) + "/variables";

	private String imageNewTag = "";

	@Test
	public void pushImage() {
		String dockerHubUsername = System.getProperty(DOCKER_HUB_USERNAME);
		String dockerHubEmail = System.getProperty(DOCKER_HUB_EMAIL);
		String dockerHubPassword = System.getProperty(DOCKER_HUB_PASSWORD);

		assertFalse("At least one of credentials is null or empty! "
				+ "dockerHubUsername:" + dockerHubUsername 
				+ " dockerHubEmail:" + dockerHubEmail 
				+ " dockerHubPassword:" + dockerHubPassword 
				+ " Aborting test...",
				StringUtils.isBlank(dockerHubUsername)
				|| StringUtils.isBlank(dockerHubEmail)  
				|| StringUtils.isBlank(dockerHubPassword));

		DockerImagesTab imagesTab = openDockerImagesTab();
		buildImage(IMAGE_NAME, DOCKERFILE_FOLDER, imagesTab);
		assertConsoleSuccess();

		setUpRegister(REGISTRY_URL, dockerHubEmail, dockerHubUsername, dockerHubPassword);
		setSecureStorage("password");
		String seconds = String.valueOf(new java.util.Date().getTime());
		this.imageNewTag = IMAGE_TAG + ":" + seconds;
		getConnection().getImage(IMAGE_NAME).addTagToImage(imageNewTag);
		new WaitUntil(
				new ImageIsDeployedCondition(IMAGE_TAG, seconds, getConnection()), 
				TimePeriod.VERY_LONG);
		getConnection().getImage(IMAGE_TAG, seconds).pushImage(REGISTRY_ACCOUNT, false, false);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		deleteImage(IMAGE_TAG, seconds);
		getConnection().pullImage(IMAGE_TAG, seconds, REGISTRY_URL);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		assertTrue("Image has not been pushed/pulled!", imageIsDeployed(IMAGE_TAG));
	}

	@After
	public void after() {
		deleteRegister(REGISTRY_URL);
		deleteImageContainerAfter(imageNewTag);
		cleanUpWorkspace();
	}
}
