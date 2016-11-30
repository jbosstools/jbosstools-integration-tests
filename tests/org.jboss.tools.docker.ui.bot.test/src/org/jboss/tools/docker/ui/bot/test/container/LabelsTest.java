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
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunResourceVolumesVariablesPage;
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

public class LabelsTest extends AbstractDockerBotTest {
	private String imageName = "debian";
	private String imageTag = "jessie";
	private String containerName = "test_run_debian_label";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
	}

	@Test
	public void testLabels() {
		pullImage(imageName, imageTag);
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning());
		imageTab.runImage(imageName + ":" + imageTag);
		ImageRunSelectionPage firstPage = new ImageRunSelectionPage();
		firstPage.setName(this.containerName);
		firstPage.setAllocatePseudoTTY();
		firstPage.setKeepSTDINOpen();
		firstPage.setGiveExtendedPrivileges();
		firstPage.next();
		ImageRunResourceVolumesVariablesPage secondPage = new ImageRunResourceVolumesVariablesPage();
		secondPage.addLabel("foo", "bar");
		secondPage.finish();
		new WaitWhile(new JobIsRunning());
		new DockerExplorerView().getDockerConnection(getDockerServer()).getContainer(containerName).select();
		PropertiesView propertiesView = new PropertiesView();
		propertiesView.open();
		propertiesView.selectTab("Inspect");
		String labelProp = propertiesView.getProperty("Config", "Labels", "foo").getPropertyValue();
		assertTrue("Container do not have label!", labelProp.equals("bar"));
	}

	@After
	public void after() {
		deleteImageContainerAfter(containerName,imageName + ":" + imageTag);
		deleteConnection();
	}

}
