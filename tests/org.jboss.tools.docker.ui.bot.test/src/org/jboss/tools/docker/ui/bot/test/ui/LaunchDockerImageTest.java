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

import org.jboss.tools.docker.reddeer.ui.DockerExplorerView;
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
	private String imageName = "hello-world";
	private String containerName = "test_variables";
	private String configurationName = "test_configuration";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
	}

	@Test
	public void testLaunchConfiguration() {

		pullImage(imageName);
		String completeImageName = getCompleteImageName(imageName);
		
		new DockerExplorerView().getDockerConnection(getDockerServer()).enableConnection();
		
		RunDockerImageLaunchConfiguration runImageConf = new RunDockerImageLaunchConfiguration();
		runImageConf.open();
		runImageConf.createNewConfiguration(configurationName);
		runImageConf.setContainerName(containerName);
		runImageConf.selectImage(completeImageName + ":latest");
		runImageConf.setPrivilegedMode(true);
		runImageConf.apply();
		runImageConf.runConfiguration(configurationName);

		assertTrue("Container is not deployed!", containerIsDeployed(containerName));
		runImageConf.open();
		runImageConf.deleteRunConfiguration(configurationName);
		runImageConf.close();
	}

	@After
	public void after() {
		deleteImageContainerAfter(containerName,imageName);
		deleteConnection();
	}

}
