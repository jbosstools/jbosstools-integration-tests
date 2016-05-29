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

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
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

public class BuildImageTest extends AbstractDockerBotTest {
	private static String imageName = "test_build";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
	}

	@Test
	public void BuildImageTest() {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		String dockerFilePath = "";
		try {
			dockerFilePath = (new File("resources/test-wildfly")).getCanonicalPath();
		} catch (IOException ex) {
			fail("Resource file not found!");
		}
		imageTab.buildImage(imageName, dockerFilePath);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	

	@After
	public void after() {
		deleteImage(imageName);
		deleteConnection();
	}

}
