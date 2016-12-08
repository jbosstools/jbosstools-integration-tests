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
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.image.AbstractImageBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jkopriva
 * @contributor adietish@redhat.com
 *
 */
public class PropertiesViewTest extends AbstractImageBotTest {

	private static final String IMAGE_NAME = IMAGE_BUSYBOX;
	private static final String CONTAINER_NAME = "test_run_docker_busybox";

	@Before
	public void before() {
		pullImage(IMAGE_NAME);
	}

	@Test
	public void testContainerPropertiesTab() {
		DockerImagesTab imagesTab = openDockerImagesTab();
		imagesTab.runImage(IMAGE_NAME);
		ImageRunSelectionPage firstPage = new ImageRunSelectionPage();
		firstPage.setName(CONTAINER_NAME);
		firstPage.finish();
		new WaitWhile(new JobIsRunning());
		DockerContainersTab containerTab = new DockerContainersTab();
		containerTab.activate();
		containerTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		containerTab.select(CONTAINER_NAME);

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
		DockerImagesTab imagesTab = openDockerImagesTab();
		imagesTab.selectImage(IMAGE_NAME);
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
		deleteContainerIfExists(CONTAINER_NAME);
	}
}
