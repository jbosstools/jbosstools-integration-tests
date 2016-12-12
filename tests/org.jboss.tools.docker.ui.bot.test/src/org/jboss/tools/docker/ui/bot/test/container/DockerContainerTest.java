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
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunSelectionPage;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class DockerContainerTest extends AbstractDockerBotTest {
	private String imageName = "docker/whalesay";
	private String containerName = "test_run";

	@Test
	public void testDockerContainer() {
		ConsoleView cview = new ConsoleView();
		cview.open();
		try {
			cview.clearConsole();
		} catch (CoreLayerException ex) {

		}
		pullImage(imageName);
		assertTrue("Image has not been found!", imageIsDeployed(getCompleteImageName(imageName)));

		getConnection().getImage(getCompleteImageName(imageName)).run();
		ImageRunSelectionPage firstPage = new ImageRunSelectionPage();
		firstPage.setName(this.containerName);
		firstPage.finish();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		new WaitWhile(new ConsoleHasNoChange());

	}

	@After
	public void after() {
		deleteImageContainerAfter(containerName, imageName);
	}

}
