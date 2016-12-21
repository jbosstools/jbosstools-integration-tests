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
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunResourceVolumesVariablesPage;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunSelectionPage;
import org.jboss.tools.docker.reddeer.ui.DockerContainersTab;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.image.AbstractImageBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class LabelsTest extends AbstractImageBotTest {

	private static final String CONTAINER_LABEL_VALUE = "bar";
	private static final String CONTAINER_LABEL_KEY = "foo";
	private static final String IMAGE_NAME = IMAGE_BUSYBOX;
	private static final String IMAGE_TAG = "latest";
	private static final String CONTAINER_NAME = "test_run_busybox_label";

	@Before
	public void before() {
		pullImage(IMAGE_NAME, IMAGE_TAG);
	}

	@Test
	public void testLabels() {
		DockerImagesTab imagesTab = openDockerImagesTab();
		imagesTab.runImage(IMAGE_NAME + ":" + IMAGE_TAG);

		ImageRunSelectionPage firstPage = new ImageRunSelectionPage();
		firstPage.setName(CONTAINER_NAME);
		firstPage.setAllocatePseudoTTY();
		firstPage.setKeepSTDINOpen();
		firstPage.setGiveExtendedPrivileges();
		firstPage.next();
		ImageRunResourceVolumesVariablesPage secondPage = new ImageRunResourceVolumesVariablesPage();
		secondPage.addLabel(CONTAINER_LABEL_KEY, CONTAINER_LABEL_VALUE);
		secondPage.finish();
		new WaitWhile(new JobIsRunning());


		DockerContainersTab containerTab = new DockerContainersTab();
		containerTab.searchContainer(CONTAINER_NAME);
		containerTab.select(CONTAINER_NAME);
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		getConnection().getContainer(CONTAINER_NAME).select();
		propertiesView.selectTab("Inspect");
		String labelProp = propertiesView.getProperty("Config", "Labels", CONTAINER_LABEL_KEY).getPropertyValue();
		assertTrue("Container does not have label " +  CONTAINER_LABEL_KEY + "!", labelProp.equals(CONTAINER_LABEL_VALUE));
	}

	@After
	public void after() {
		killRunningImageJobs();
		deleteContainerIfExists(CONTAINER_NAME);
	}

}
