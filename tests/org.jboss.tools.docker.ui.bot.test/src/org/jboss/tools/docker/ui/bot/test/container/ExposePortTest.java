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

import java.io.IOException;

import org.jboss.ide.eclipse.as.reddeer.server.deploy.DeployOnServer;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunSelectionPage;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.image.AbstractImageBotTest;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class ExposePortTest extends AbstractImageBotTest {

	private static final String CONTAINER_NAME = "test_run_uhttpd";
	private static final String EXPOSED_PORT = "80";

	@Test
	public void testExposePort() throws IOException {
		pullImage(IMAGE_UHTTPD, IMAGE_TAG_LATEST);
		DockerImagesTab imagesTab = openDockerImagesTab();
		runContainer(IMAGE_UHTTPD, IMAGE_TAG_LATEST, CONTAINER_NAME, imagesTab);

		assertPortIsAccessible(EXPOSED_PORT);
	}

	private void assertPortIsAccessible(String exposedPort) {
		BrowserView browserView = new BrowserView();
		browserView.open();
		String url = createURL(":" + exposedPort);
		DeployOnServer.checkBrowserForErrorPage(browserView, url);
	}

	private void runContainer(String imageName, String imageTag, String containerName, DockerImagesTab imagesTab) {
		imagesTab.runImage(imageName + ":" + imageTag);
		ImageRunSelectionPage firstPage = new ImageRunSelectionPage();
		firstPage.setName(containerName);
		firstPage.setPublishAllExposedPorts(false);
		firstPage.finish();
		new WaitWhile(new JobIsRunning());
		new WaitWhile(new ConsoleHasNoChange());
	}

	@After
	public void after() {
		deleteContainerIfExists(CONTAINER_NAME);
	}

}
