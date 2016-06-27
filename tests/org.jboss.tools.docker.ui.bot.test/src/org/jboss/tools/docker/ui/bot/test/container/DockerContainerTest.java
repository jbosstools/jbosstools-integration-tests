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

import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.condition.ConsoleHasNoChange;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.docker.reddeer.core.ui.wizards.RunADockerImagePageOneWizard;
import org.jboss.tools.docker.reddeer.ui.ConnectionItem;
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

public class DockerContainerTest extends AbstractDockerBotTest {
	private String imageName = "docker/whalesay";
	private String containerName = "test_run";

	@Before
	public void before() {
		openDockerPerspective();
		createConnection();
	}

	@Test
	public void testDockerContainer() {
		String dockerServer = getDockerServer();
		ConsoleView cview = new ConsoleView();
		cview.open();
		try {
			cview.clearConsole();
		} catch (CoreLayerException ex) {

		}
		pullImage(this.imageName);
		DockerExplorer de = new DockerExplorer();
		de.open();
		ConnectionItem dockerConnection = de.getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		dc.expand();
		TreeItem imagesItem = dc.getItem("Images");
		imagesItem.select();
		imagesItem.expand();

		TreeItem item = getChildContainsWith(imagesItem.getItems(), imageName);
		if (item == null)
			assertTrue("Image has not been found!", false);
		item.select();

		new ContextMenu("Run...").select();
		RunADockerImagePageOneWizard firstPage = new RunADockerImagePageOneWizard();
		firstPage.setName(this.containerName);
		firstPage.finish();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		new WaitWhile(new ConsoleHasNoChange());

	}

	private TreeItem getChildContainsWith(List<TreeItem> items, String containString) {
		for (TreeItem item : items) {
			if (item.getText().contains(containString)) {
				return item;
			}
		}
		return null;
	}

	@After
	public void after() {
		deleteContainer(this.containerName);
		deleteImage(imageName);
		deleteConnection();
	}

}
