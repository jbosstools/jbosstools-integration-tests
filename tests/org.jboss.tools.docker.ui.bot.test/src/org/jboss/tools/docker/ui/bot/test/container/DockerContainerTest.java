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

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunSelectionPage;
import org.jboss.tools.docker.ui.bot.test.image.AbstractImageBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jkopriva
 * @contributor adietish@redhat.com
 *
 */

public class DockerContainerTest extends AbstractImageBotTest {

	private static final String IMAGE_NAME = IMAGE_BUSYBOX;
	private static final String CONTAINER_NAME = "test_run";

	@Before
	public void before() {
		clearConsole();
		pullImage(IMAGE_NAME);
	}
	
	@Test
	public void testRunDockerContainer() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		assertTrue("Image has not been found!", imageIsDeployed(getCompleteImageName(IMAGE_NAME)));
		getConnection().getImage(getCompleteImageName(IMAGE_NAME)).run();
		ImageRunSelectionPage firstPage = new ImageRunSelectionPage();
		firstPage.setName(CONTAINER_NAME);
		firstPage.finish();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		new WaitWhile(new ConsoleHasNoChange());

	}

	@After
	public void after() {
		deleteImageContainerAfter(CONTAINER_NAME);
	}

}
