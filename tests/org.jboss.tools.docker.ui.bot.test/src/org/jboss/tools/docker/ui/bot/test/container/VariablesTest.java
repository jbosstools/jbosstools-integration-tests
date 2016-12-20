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

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunResourceVolumesVariablesPage;
import org.jboss.tools.docker.reddeer.core.ui.wizards.ImageRunSelectionPage;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.image.AbstractImageBotTest;
import org.junit.After;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 * @contributor adietish@redhat.com
 */

public class VariablesTest extends AbstractImageBotTest {
	
	private static final String IMAGE_NAME = "test_variables";
	private static final String CONTAINER_NAME = "run_" + IMAGE_NAME;

	@Test
	public void testVariables() {
		DockerImagesTab imagesTab = openDockerImagesTab();
		buildImage(IMAGE_NAME, "resources/test-variables", imagesTab);
		assertConsoleSuccess();
		
		imagesTab.activate();
		imagesTab.refresh();
		new WaitWhile(new JobIsRunning());
		imagesTab.runImage(IMAGE_NAME);
		ImageRunSelectionPage firstPage = new ImageRunSelectionPage();
		firstPage.setName(CONTAINER_NAME);
		firstPage.next();
		ImageRunResourceVolumesVariablesPage secondPage = new ImageRunResourceVolumesVariablesPage();
		secondPage.addEnviromentVariable("FOO", "barbarbar");
		secondPage.finish();
		new WaitWhile(new JobIsRunning());
		assertConsoleContains("FOO is barbarbar");
	}

	@After
	public void after() {
		deleteContainer(CONTAINER_NAME);
		deleteImage(IMAGE_NAME);
	}

}
