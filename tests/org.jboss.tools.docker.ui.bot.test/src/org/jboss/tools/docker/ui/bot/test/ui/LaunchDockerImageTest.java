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

import org.jboss.tools.docker.reddeer.ui.DockerExplorer;
import org.jboss.tools.docker.reddeer.ui.RunDockerImageLaunchConfiguration;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class LaunchDockerImageTest extends AbstractDockerBotTest {
	private static final String IMAGE_NAME = "hello-world:latest";
	private static final String CONTAINER_NAME = "test_variables";
	private static final String CONFIGURATION_NAME = "test_configuration";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
	}

	@Test
	public void testLaunchConfiguration() {

		pullImage(IMAGE_NAME);
		RunDockerImageLaunchConfiguration runImageConf = new RunDockerImageLaunchConfiguration();
		runImageConf.open();
		runImageConf.createNewConfiguration(CONFIGURATION_NAME);
		runImageConf.setContainerName(CONTAINER_NAME);
		runImageConf.selectImage(IMAGE_NAME);
		runImageConf.setPrivilegedMode(true);
		runImageConf.apply();
		runImageConf.runConfiguration(CONFIGURATION_NAME);


		DockerExplorer de = new DockerExplorer();
		assertTrue("Container is not deployed!",de.containerIsDeployed(getDockerServer(), CONTAINER_NAME));
		runImageConf.open();
		runImageConf.deleteRunConfiguration(CONFIGURATION_NAME);
		runImageConf.close();
	}

	@After
	public void after() {
		deleteContainer(CONTAINER_NAME);
		deleteImage(IMAGE_NAME);
		deleteConnection();
	}

}
