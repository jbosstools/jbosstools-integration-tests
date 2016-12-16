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

import org.apache.commons.lang.StringUtils;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.ui.views.properties.PropertiesView;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
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
	private static final String IMAGE_NAME = "busybox";
	private static final String IMAGE_NAME_TO_PULL = "busybox:latest";
	private static final String IMAGE_TAG = "testtag";
	private static String IMAGE_TAG_UPPERCASE = "UPPERCASETAG";

	// docker daemon 1.11 errors with uppercase tags, priors dont
	private static final int DAEMON_MAJOR_VERSION = 1; 
	private static final int DAEMON_MINOR_VERSION = 11;

	@Test
	public void testAddRemoveTagToImage() {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		pullImage(IMAGE_NAME_TO_PULL);
		new WaitWhile(new JobIsRunning());
		assertTrue("Image has not been deployed!", imageIsDeployed(IMAGE_NAME));

		imageTab.activate();
		imageTab.addTagToImage(IMAGE_NAME, IMAGE_TAG);
		new WaitWhile(new JobIsRunning());
		assertTrue("Image tag has not been added", imageTab.getImageTags(IMAGE_NAME).contains(IMAGE_TAG));

		imageTab.activate();
		imageTab.removeTagFromImage(IMAGE_NAME, IMAGE_TAG);
		new WaitWhile(new JobIsRunning());
		assertTrue("ImageTaghasNotBeenRemoved", !imageTab.getImageTags(IMAGE_NAME).contains(IMAGE_TAG));
	}
	
	/**
	 * Tries to add an uppercase tag to an image. This errors in docker daemon
	 * >= 1.11 while it succeeds in older versions.
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=509223
	 */
	@Test
	public void testAddUpperCaseTagToImage() {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		pullImage(IMAGE_NAME_TO_PULL);
		new WaitWhile(new JobIsRunning());
		assertTrue("Image has not been deployed!", imageIsDeployed(IMAGE_NAME));

		imageTab.activate();
		imageTab.addTagToImage(IMAGE_NAME, IMAGE_TAG_UPPERCASE);
		
		if (isDockerDaemon(DAEMON_MAJOR_VERSION, DAEMON_MINOR_VERSION)) {
			// docker daemon >= 1.11
			new DefaultShell("Error tagging image to <" + IMAGE_TAG_UPPERCASE + ">");
			new OkButton().click();
			assertFalse("Image tag has been added!", imageTab.getImageTags(IMAGE_NAME).contains(IMAGE_TAG));
		} else {
			assertTrue("Image tag has not been added!", imageTab.getImageTags(IMAGE_NAME).contains(IMAGE_TAG_UPPERCASE));
		}
	}

	/**
	 * Returns {@code true} if the running docker daemon matches at least the
	 * given major and minor version. Returns {@code false} otherwise.
	 * 
	 * @param majorVersion
	 * @param minorVersion
	 * @return
	 */
	private boolean isDockerDaemon(int majorVersion, int minorVersion) {
		getConnection().select();
		PropertiesView infoTab = openPropertiesTab("Info");
		String daemonVersion = infoTab.getProperty("Version").getPropertyValue();
		assertTrue("Could not retrieve docker daemon version.", !StringUtils.isBlank(daemonVersion));
		String[] versionComponents = daemonVersion.split("\\.");
		assertTrue("Could not evaluate docker daemon version " + daemonVersion, 
				versionComponents == null || versionComponents.length >= 2); 
		int actualMajorVersion = Integer.parseInt(versionComponents[0]); 			
		if (actualMajorVersion > majorVersion) {
			return true;
		}
		int actualMinorVersion = Integer.parseInt(versionComponents[1]);
		return actualMinorVersion >= minorVersion;
	}

	@After
	public void after() {
		deleteImageContainerAfter(IMAGE_NAME);
		cleanUpWorkspace();
	}
	
	protected PropertiesView openPropertiesTab(String tabName) {
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		propertiesView.selectTab(tabName);
		return propertiesView;
	}


}
