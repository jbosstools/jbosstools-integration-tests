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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunResourceVolumesVariablesPage;
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
 */
public class VolumeMountTest extends AbstractImageBotTest {

	private static final String CONTAINER_NAME = "test_mount_volumes";
	private static final String VOLUME_PATH = "resources/test-volumes";
	private static final String CONTAINER_PATH = "/www";
	private static final String INDEX_PAGE = "index.html";
	private static final String INDEX_PAGE_PATH = VOLUME_PATH + "/" + INDEX_PAGE;
	private static final String HOST_PORT = "80";
	
	@Before
	public void before() {
		pullImage(IMAGE_UHTTPD, IMAGE_TAG_LATEST);
	}
	
	@Test
	public void testVolumeMount() throws IOException {
		DockerImagesTab imagesTab = openDockerImagesTab();
		imagesTab.runImage(IMAGE_UHTTPD + ":" + IMAGE_TAG_LATEST);

		ImageRunSelectionPage firstPage = new ImageRunSelectionPage();
		firstPage.setName(CONTAINER_NAME);
		firstPage.setPublishAllExposedPorts(true);
		firstPage.next();
		
		ImageRunResourceVolumesVariablesPage secondPage = new ImageRunResourceVolumesVariablesPage();
		String volumePath = (new File(VOLUME_PATH)).getCanonicalPath();
		secondPage.addDataVolumeToHost(CONTAINER_PATH, volumePath);
		secondPage.finish();
		new WaitWhile(new JobIsRunning());
		new WaitWhile(new ConsoleHasNoChange());

		String indexPage = getIndexPageContent();
		String indexPageResource = getResourceAsString(INDEX_PAGE_PATH);
		assertEquals(INDEX_PAGE_PATH + " wasnt mounted/displayed properly.", indexPage, indexPageResource);
	}

	private String getIndexPageContent() {
		String containerIP = getContainerIP(CONTAINER_NAME);
		String url = "http://" + containerIP + ":" + HOST_PORT + "/" + INDEX_PAGE;
		BrowserView browserView = new BrowserView();
		browserView.open();
		browserView.openPageURL(url);
		return browserView.getText();
	}

	@After
	public void after() {
		deleteContainerIfExists(CONTAINER_NAME);
	}

}
