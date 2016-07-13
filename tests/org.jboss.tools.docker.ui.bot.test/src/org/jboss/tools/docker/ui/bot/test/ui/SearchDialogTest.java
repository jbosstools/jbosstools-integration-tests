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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ProgressInformationShellIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.docker.reddeer.core.ui.wizards.SearchDockerImagePageOneWizard;
import org.jboss.tools.docker.reddeer.core.ui.wizards.SearchDockerImagePageTwoWizard;
import org.jboss.tools.docker.reddeer.ui.DockerExplorer;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */

public class SearchDialogTest extends AbstractDockerBotTest {
	private String imageName = "busybox";
	private String tag = "1.24.2";
	private String expectedImageName = "busybox";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
	}

	@Test
	public void testSearchDialog() {
		new DockerExplorer().openImageSearchDialog(getDockerServer(), "Docker", this.imageName);
		SearchDockerImagePageOneWizard pageOne = new SearchDockerImagePageOneWizard();
		pageOne.searchImage();
		assertFalse("Search result is empty!", pageOne.getSearchResults().isEmpty());
		assertTrue("Search result do not contains image:" + expectedImageName + "!",
				pageOne.searchResultsContains(expectedImageName));
		pageOne.next();

		new WaitWhile(new ProgressInformationShellIsActive(), TimePeriod.NORMAL);
		AbstractWait.sleep(TimePeriod.getCustom(5));
		SearchDockerImagePageTwoWizard pageTwo = new SearchDockerImagePageTwoWizard();
		assertFalse("Search tags are empty!", pageTwo.getTags().isEmpty());
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		assertTrue("Search result do not contains tag:" + tag + "!", pageTwo.tagsContains(tag));
		pageTwo.selectTag(tag);
		pageTwo.finish();
		new DefaultShell("Pull Image");
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	@After
	public void after() {
		deleteImage(this.imageName);
		deleteConnection();
	}

}
