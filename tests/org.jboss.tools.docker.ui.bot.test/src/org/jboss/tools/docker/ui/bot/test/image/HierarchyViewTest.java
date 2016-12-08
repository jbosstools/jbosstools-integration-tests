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

import java.util.List;

import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.docker.reddeer.ui.DockerImageHierarchyTab;
import org.junit.Test;

/**
 * 
 * @author jkopriva@redhat.com
 * @cotributor adietish@redhat.com
 *
 */

public class HierarchyViewTest extends AbstractImageBotTest {

	private static final String IMAGE_CIRROS_VERSION = "0.3.4";
	private static final String IMAGE_CIRROS = "docker.io/cirros";
	
	@Test
	public void testHierarchyView() {
		pullImage(IMAGE_CIRROS, IMAGE_CIRROS_VERSION, null);
		
		DockerImageHierarchyTab hierarchyTab = openDockerImageHierarchyTab();
		List<TreeItem> treeItems = hierarchyTab.getTreeItems();
		compareTextInFirstNode(treeItems, "<none>:<none>");
		List<TreeItem> treeItems2 = treeItems.get(0).getItems();
		compareTextInFirstNode(treeItems2, "<none>:<none>");
		List<TreeItem> treeItems3 = treeItems2.get(0).getItems();
		compareTextInFirstNode(treeItems3, "<none>:<none>");
		List<TreeItem> treeItems4 = treeItems3.get(0).getItems();
		compareTextInFirstNode(treeItems4, "<none>:<none>");
		List<TreeItem> treeItems5 = treeItems4.get(0).getItems();
		compareTextInFirstNode(treeItems5, IMAGE_CIRROS + NAME_TAG_SEPARATOR + IMAGE_CIRROS_VERSION);
	}

	public void compareTextInFirstNode(List<TreeItem> treeItems, String expectedValue) {
		String nodeText = treeItems.get(0).getText().replaceAll("\\(.*\\)", "").trim();
		assertTrue("Hierarchy view contains string: " + nodeText + ", but it is expected: " + expectedValue,
				nodeText.startsWith(expectedValue));
	}

	private DockerImageHierarchyTab openDockerImageHierarchyTab() {
		getConnection().getImage(IMAGE_CIRROS, IMAGE_CIRROS_VERSION).openImageHierarchy();
		new WaitWhile(new ShellWithTextIsAvailable("Docker Image Hierarchy"));
		DockerImageHierarchyTab hierarchyTab = new DockerImageHierarchyTab();
		hierarchyTab.open();
		return hierarchyTab;
	}
}
