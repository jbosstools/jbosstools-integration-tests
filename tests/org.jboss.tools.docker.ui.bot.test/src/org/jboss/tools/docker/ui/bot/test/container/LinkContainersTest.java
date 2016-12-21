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

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunSelectionPage;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.reddeer.ui.DockerTerminal;
import org.jboss.tools.docker.ui.bot.test.image.AbstractImageBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class LinkContainersTest extends AbstractImageBotTest {

	private static final String IMAGE_ALPINE_CURL = "byrnedo/alpine-curl";
	private static final String CONTAINER_NAME_HTTP_SERVER = "test_run_httpd";
	private static final String CONTAINER_NAME_CLIENT_ALPINE = "test_connect_httpd";

	@Before
	public void before() {
		pullImage(IMAGE_ALPINE_CURL);
		pullImage(IMAGE_UHTTPD);
	}

	private ImageRunSelectionPage openImageRunSelectionPage(String containerName, boolean publishAllExposedPorts) {
		ImageRunSelectionPage page = new ImageRunSelectionPage();
		page.setName(containerName);
		page.setPublishAllExposedPorts(publishAllExposedPorts);
		return page;
	}

	@Test
	public void testLinkContainers() {
		runUhttpServer(IMAGE_UHTTPD, CONTAINER_NAME_HTTP_SERVER);
		runAlpineLinux(IMAGE_ALPINE_CURL, CONTAINER_NAME_CLIENT_ALPINE);

	}

	public void runUhttpServer(String imageName, String containerName) {
		DockerImagesTab imagesTab = openDockerImagesTab();
		imagesTab.runImage(imageName);
		ImageRunSelectionPage firstPage = openImageRunSelectionPage(containerName, false);
		firstPage.setName(containerName);
		firstPage.setPublishAllExposedPorts(false);
		firstPage.finish();
		new WaitWhile(new JobIsRunning());
		new WaitWhile(new ConsoleHasNoChange());
	}

	public void runAlpineLinux(String imageName, String containerName) {
		String serverAddress = getHttpServerAddress(CONTAINER_NAME_HTTP_SERVER);
		DockerImagesTab imagesTab = openDockerImagesTab();
		imagesTab.runImage(imageName);
		ImageRunSelectionPage firstPage = openImageRunSelectionPage(containerName, false);
		firstPage.setName(containerName);
		firstPage.setCommand(serverAddress + ":80");
		firstPage.addLinkToContainer(CONTAINER_NAME_HTTP_SERVER, "http_server");
		firstPage.setPublishAllExposedPorts(false);
		firstPage.setAllocatePseudoTTY();
		firstPage.setKeepSTDINOpen();
		firstPage.finish();
		new WaitWhile(new JobIsRunning());
		DockerTerminal dt = new DockerTerminal();
		dt.open();
		String terminalText = dt.getTextFromPage("/" + containerName);
		assertTrue("No output from terminal!", !terminalText.isEmpty());
		assertTrue("Containers are not linked!", !terminalText.contains("Connection refused"));
	}

	private String getHttpServerAddress(String containerName) {
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		getConnection().getContainer(containerName).select();
		propertiesView.selectTab("Inspect");
		return propertiesView.getProperty("NetworkSettings", "IPAddress").getPropertyValue();
	}

	@After
	public void after() {
		deleteContainerIfExists(CONTAINER_NAME_CLIENT_ALPINE);
		deleteContainerIfExists(CONTAINER_NAME_HTTP_SERVER);
		deleteImageIfExists(IMAGE_ALPINE_CURL);
	}
}
