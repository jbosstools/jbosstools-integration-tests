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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class ImageTagTest extends AbstractDockerBotTest {
	private static String imageName = "busybox";
	private static String imageNameToPull = "busybox:latest";
	private static String imageTag = "testtag";
	private static String imageTagUppercase = "UPPERCASETAG";

	@Test
	public void testAddRemoveTagToImage() {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		pullImage(imageNameToPull);
		new WaitWhile(new JobIsRunning());
		assertTrue("Image has not been deployed!", imageIsDeployed(imageName));

		imageTab.activate();
		imageTab.addTagToImage(imageName, imageTag);
		new WaitWhile(new JobIsRunning());
		assertTrue("Image tag has not been added", imageTab.getImageTags(imageName).contains(imageTag));

		imageTab.activate();
		imageTab.removeTagFromImage(imageName, imageTag);
		new WaitWhile(new JobIsRunning());
		assertTrue("ImageTaghasNotBeenRemoved", !imageTab.getImageTags(imageName).contains(imageTag));
	}
	
	@Test
	public void testAddRemoveUpperCaseTagToImage() {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		pullImage(imageNameToPull);
		new WaitWhile(new JobIsRunning());
		assertTrue("Image has not been deployed!", imageIsDeployed(imageName));

		imageTab.activate();
		imageTab.addTagToImage(imageName, imageTagUppercase);
		new ShellWithTextIsAvailable("Error tagging image to <" + imageTagUppercase + ">");
		new OkButton().click();
		assertFalse("Image tag has been added!", imageTab.getImageTags(imageName).contains(imageTag));
	}

	@After
	public void after() {
		deleteImageContainerAfter(imageName);
		cleanUpWorkspace();
	}

}
