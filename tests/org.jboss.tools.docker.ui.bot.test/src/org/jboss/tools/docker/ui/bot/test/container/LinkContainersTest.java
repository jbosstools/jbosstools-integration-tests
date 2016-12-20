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

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunResourceVolumesVariablesPage;
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

	private static final String IMAGE_NAME = "mariadb";
	private static final String IMAGE_TAG = "latest";
	private static final String CONTAINER_NAME_DB = "test_run_mariadb";
	private static final String CONTAINER_NAME_CLIENT = "test_connect_mariadb";

	@Before
	public void before() {
		pullImage(IMAGE_NAME, IMAGE_TAG);
	}

	@Test
	public void testLinkContainers() {
		runDatabase(IMAGE_NAME + ":" + IMAGE_TAG, CONTAINER_NAME_DB);
		runClient(IMAGE_NAME + ":" + IMAGE_TAG, CONTAINER_NAME_CLIENT, CONTAINER_NAME_DB, getDBAddress(CONTAINER_NAME_DB));
	}

	public void runDatabase(String image, String containerName) {
		DockerImagesTab imagesTab = openDockerImagesTab();
		imagesTab.runImage(image);
		ImageRunSelectionPage firstPage = openImageRunSelectionPage(containerName, false);
		firstPage.setEntrypoint("docker-entrypoint.sh");
		firstPage.setCommand("mysqld");
		firstPage.next();
		ImageRunResourceVolumesVariablesPage secondPage = new ImageRunResourceVolumesVariablesPage();
		secondPage.addEnviromentVariable("MYSQL_ROOT_PASSWORD", "password");
		secondPage.finish();
		new WaitWhile(new JobIsRunning());
		new WaitWhile(new ConsoleHasNoChange());
	}

	public String getDBAddress(String containerName) {
		getConnection().getContainer(containerName).select();
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		propertiesView.selectTab("Inspect");
		return propertiesView.getProperty("NetworkSettings", "IPAddress").getPropertyValue();
	}

	public void runClient(String image, String containerNameClient, String containerNameDb, String dbAddress) {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning());
		imageTab.runImage(image);
		ImageRunSelectionPage firstPage = openImageRunSelectionPage(containerNameClient, false);
		firstPage.setEntrypoint("docker-entrypoint.sh");
		firstPage.setCommand("mysql -h" + dbAddress + " -P3306 -uroot -ppassword");
		firstPage.addLinkToContainer(containerNameDb, "mysql");
		firstPage.setPublishAllExposedPorts();
		firstPage.setKeepSTDINOpen();
		firstPage.setAllocatePseudoTTY();
		try {
			firstPage.finish();
		} catch (WaitTimeoutExpiredException ex) {
			// swallow intentionall
			// TODO: add logging
		}
		DockerTerminal dt = new DockerTerminal();
		dt.activate();
		assertTrue("No output from terminal!", !dt.getTextFromPage("/" + containerNameClient).isEmpty());
	}

	private ImageRunSelectionPage openImageRunSelectionPage(String containerName, boolean publishAllExposedPorts) {
		ImageRunSelectionPage page = new ImageRunSelectionPage();
		page.setName(containerName);
		page.setPublishAllExposedPorts(publishAllExposedPorts);
		return page;
	}

	@After
	public void after() {
		deleteContainerIfExists(CONTAINER_NAME_DB);
		deleteContainerIfExists(CONTAINER_NAME_CLIENT);
	}
}
