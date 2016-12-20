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

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.tools.docker.reddeer.ui.RunDockerImageLaunchConfiguration;
import org.jboss.tools.docker.ui.bot.test.image.AbstractImageBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 * @contributor adietish@redhat.com
 */

public class LaunchDockerImageTest extends AbstractImageBotTest {

	private static final String IMAGE_NAME = "hello-world";
	private static final String CONTAINER_NAME = "test_variables";
	private static final String CONFIGURATION_NAME = "test_configuration";

	@Before
	public void before() {
		pullImage(IMAGE_NAME);
	}

	@Test
	public void testLaunchConfiguration() {
		String imageName = getCompleteImageName(IMAGE_NAME);

		RunDockerImageLaunchConfiguration runImageConf = new RunDockerImageLaunchConfiguration();
		runDockerImageLaunchConfiguration(
				imageName + NAME_TAG_SEPARATOR + IMAGE_TAG_LATEST, CONTAINER_NAME, CONFIGURATION_NAME, runImageConf);
		assertTrue("Container is not deployed!", containerIsDeployed(CONTAINER_NAME));
	}

	private void runDockerImageLaunchConfiguration(String imageName, String containerName, String configurationName, RunDockerImageLaunchConfiguration runImageConf) {
		runImageConf.open();
		runImageConf.createNewConfiguration(configurationName);
		runImageConf.setContainerName(containerName);
		runImageConf.selectImage(imageName);
		runImageConf.setPrivilegedMode(true);
		runImageConf.apply();
		runImageConf.runConfiguration(configurationName);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	private void deleteIfExists(String configurationName) {
		RunDockerImageLaunchConfiguration runImageConf = new RunDockerImageLaunchConfiguration();
		try {
			runImageConf.open();
			runImageConf.deleteRunConfiguration(configurationName);
			runImageConf.close();
		} catch (RedDeerException e) {
			// catched intentionally
		}
	}

	@After
	public void after() {
		deleteIfExists(CONFIGURATION_NAME);
		deleteImageContainerAfter(CONTAINER_NAME);
	}

}
