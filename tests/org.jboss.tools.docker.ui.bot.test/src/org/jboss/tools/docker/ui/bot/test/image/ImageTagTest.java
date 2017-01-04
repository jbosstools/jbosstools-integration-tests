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

package org.jboss.tools.docker.ui.bot.test.image;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 * @contributor adietish@redhat.com
 *
 */

public class ImageTagTest extends AbstractImageBotTest {

	private static final String IMAGE_NAME = IMAGE_BUSYBOX;
	private static final String IMAGE_NAME_TO_PULL = IMAGE_BUSYBOX_LATEST;
	private static final String IMAGE_TAG = "testtag";
	private static final String IMAGE_TAG_UPPERCASE = "UPPERCASETAG";

	@Before
	public void before() {
		deleteImageIfExists(IMAGE_NAME);
		pullImage(IMAGE_NAME_TO_PULL);
		new WaitWhile(new JobIsRunning());
		assertTrue("Image has not been deployed!", imageIsDeployed(IMAGE_NAME));
	}

	@Test
	public void testAddRemoveTagToImage() {
		DockerImagesTab imagesTab = openDockerImagesTab();
		imagesTab.addTagToImage(IMAGE_NAME, IMAGE_TAG);
		new WaitWhile(new JobIsRunning());
		assertTrue("Image tag has not been added", imagesTab.getImageTags(IMAGE_NAME).contains(IMAGE_TAG));

		imagesTab.activate();
		imagesTab.removeTagFromImage(IMAGE_NAME, IMAGE_TAG);
		new WaitWhile(new JobIsRunning());
		assertTrue("ImageTaghasNotBeenRemoved", !imagesTab.getImageTags(IMAGE_NAME).contains(IMAGE_TAG));
	}

	/**
	 * Tries to add an uppercase tag to an image. This errors in docker daemon
	 * >= 1.11 while it succeeds in older versions.
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=509223
	 */
	@Test
	public void testAddUpperCaseTagToImage() {
		DockerImagesTab imagesTab = openDockerImagesTab();
		try {
			imagesTab.addTagToImage(IMAGE_NAME, IMAGE_TAG_UPPERCASE);
		} catch (WaitTimeoutExpiredException ex) {
			new CancelButton().click();
			// swallowing, it is not possible to tag image with upper case
		}
	}

	@After
	public void after() {
		deleteImageContainerAfter(IMAGE_NAME);
		cleanUpWorkspace();
	}
}
