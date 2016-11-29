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

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunSelectionPage;
import org.jboss.tools.docker.reddeer.ui.DockerContainersTab;
import org.jboss.tools.docker.reddeer.ui.DockerExplorerView;
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

public class PropertiesViewTest extends AbstractDockerBotTest {

	private String imageName = "docker/whalesay";
	private String containerName = "test_run_docker_whalesay";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
		pullImage(this.imageName);
	}

	@Test
	public void testContainerPropertiesTab() {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning());
		imageTab.runImage(this.imageName);
		ImageRunSelectionPage firstPage = new ImageRunSelectionPage();
		firstPage.setName(this.containerName);
		firstPage.finish();
		new WaitWhile(new JobIsRunning());
		DockerContainersTab containerTab = new DockerContainersTab();
		containerTab.activate();
		containerTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		containerTab.select(containerName);

		// get values from Properties view
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		try {
			propertiesView.selectTab("Info");
		} catch (SWTLayerException ex) {
			fail("Properties tab Info is not opened, is image selected in Images Tab?");
		}
	}

	@Test
	public void testImagePropertiesTab() {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		imageTab.selectImage(imageName);
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		try {
			propertiesView.selectTab("Info");
		} catch (SWTLayerException ex) {
			fail("Properties tab Info is not opened, is image selected in Images Tab?");
		}
	}

	@After
	public void after() {
		if (new DockerExplorerView().getDockerConnection(getDockerServer()).getContainer(containerName) != null) {
			deleteContainer(this.containerName);
		}
		deleteImage("docker/whalesay");
		deleteConnection();
	}

}
