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
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunSelectionPage;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.image.AbstractImageBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 * @contributor adietish@redhat.com
 *
 */
public class PrivilegedModeTest extends AbstractImageBotTest {

	private static final String IMAGE_NAME = IMAGE_BUSYBOX;
	private static final String IMAGE_TAG = IMAGE_TAG_LATEST;
	private static final String CONTAINER_NAME = "test_run_busybox";

	@Before
	public void before() {
		pullImage(IMAGE_NAME,IMAGE_TAG);
	}
	
	@Test
	public void testPrivilegedMode() {
		DockerImagesTab imagesTab = openDockerImagesTab();
		imagesTab.runImage(IMAGE_NAME + ":" + IMAGE_TAG);
		ImageRunSelectionPage firstPage = new ImageRunSelectionPage();
		firstPage.setName(CONTAINER_NAME);
		firstPage.setAllocatePseudoTTY();
		firstPage.setKeepSTDINOpen();
		firstPage.setGiveExtendedPrivileges();
		firstPage.finish();
		new WaitWhile(new JobIsRunning());
		getConnection().getContainer(CONTAINER_NAME).select();
		PropertiesView propertiesView = openPropertiesTab("Inspect");
		String privilegedProp = propertiesView.getProperty("HostConfig", "Privileged").getPropertyValue();
		assertTrue("Container is not running in privileged mode!", privilegedProp.equals("true"));
	}

	@After
	public void after() {
		deleteContainerIfExists(CONTAINER_NAME);
	}
}
