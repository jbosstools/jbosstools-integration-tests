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
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.docker.reddeer.ui.DockerExplorer;
import org.jboss.tools.docker.reddeer.ui.DockerImageHierarchyTab;
import org.jboss.tools.docker.reddeer.ui.DockerImagesTab;
import org.jboss.tools.docker.ui.bot.test.AbstractDockerBotTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author jkopriva@redhat.com
 *
 */

public class HierarchyViewTest extends AbstractDockerBotTest {
	private static String imageName = "test_build";

	@Before
	public void before() {
		prepareWorkspace();
	}

	@Test
	public void testHierarchyView() {
		DockerImagesTab imageTab = new DockerImagesTab();
		imageTab.activate();
		imageTab.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
		String dockerFilePath = "";
		try {
			dockerFilePath = (new File("resources/test-wildfly")).getCanonicalPath();
		} catch (IOException ex) {
			fail("Resource file not found!");
		}
		imageTab.buildImage(imageName, dockerFilePath);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		assertFalse("Console has no output!", consoleView.getConsoleText().isEmpty());
		assertTrue("Build has not been successful", consoleView.getConsoleText().contains("Successfully built"));
		DockerExplorer de = new DockerExplorer();
		de.openImageHierarchy(getDockerServer(), imageName);
		new WaitWhile(new ShellWithTextIsAvailable("Docker Image Hierarchy"));
		DockerImageHierarchyTab hierarchyTab = new DockerImageHierarchyTab();
		hierarchyTab.open();
		List<TreeItem> treeItems = hierarchyTab.getTreeItems();
		compareTextInFirstNode(treeItems, "jboss/base-jdk:8");
		List<TreeItem> treeItems2 = treeItems.get(0).getItems();
		compareTextInFirstNode(treeItems2, "<none>:<none>");
		List<TreeItem> treeItems3 = treeItems2.get(0).getItems();
		compareTextInFirstNode(treeItems3, "<none>:<none>");
		List<TreeItem> treeItems4 = treeItems3.get(0).getItems();
		compareTextInFirstNode(treeItems4, "<none>:<none>");
		List<TreeItem> treeItems5 = treeItems4.get(0).getItems();
		compareTextInFirstNode(treeItems5, "<none>:<none>");
		List<TreeItem> treeItems6 = treeItems5.get(0).getItems();
		compareTextInFirstNode(treeItems6, "test_build:latest");
	}

	@After
	public void after() {
		deleteImage(imageName);
		deleteImage("jboss/base-jdk");
		cleanUpWorkspace();
	}

	public void compareTextInFirstNode(List<TreeItem> treeItems, String expectedValue) {
		String nodeText = treeItems.get(0).getText().replaceAll("\\(.*\\)", "").trim();
		assertTrue("Hierarchy view contains string:" + nodeText + ", but it is expected:" + expectedValue,
				nodeText.startsWith(expectedValue));
	}

}
