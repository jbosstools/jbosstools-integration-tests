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

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.tools.docker.reddeer.ui.ConnectionItem;
import org.jboss.tools.docker.reddeer.ui.DockerExplorer;
import org.junit.Test;

/**
 * 
 * @author jkopriva
 *
 */


public class DeleteAllTest {

	@Test
	public void DeleteAllTest() {
		String dockerServerURI = System.getProperty("dockerServerURI");
		String imageName = System.getProperty("imageName");
		deleteContainer(dockerServerURI, imageName);
		deleteImage(dockerServerURI, imageName);
		deleteConnection(dockerServerURI);
	}

	private static void deleteConnection(String dockerServer) {
		new DockerExplorer().open();
		new DefaultToolItem("Remove Connection").click();

	}

	private static void deleteContainer(String dockerServer, String containerName) {
		DockerExplorer de = new DockerExplorer();
		de.open();
		ConnectionItem dockerConnection = de.getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		dc.expand();
		TreeItem containersItem = dc.getItem("Containers");
		containersItem.select();
		containersItem.expand();
		for (TreeItem item : containersItem.getItems()) {
			if (item.getText().contains(containerName)) {
				item.select();
				Menu contextMenu = new ContextMenu("Remove");
				if (!contextMenu.isEnabled()) {
					new ContextMenu("Stop").select();
					new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
					item.select();
					contextMenu = new ContextMenu("Remove");
				}
				contextMenu.select();
				new WaitUntil(new ShellWithTextIsActive("Confirm Remove Container"), TimePeriod.NORMAL);
				new PushButton("OK").click();
				new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
			}
		}
	}

	private static void deleteImage(String dockerServer, String imageName) {
		DockerExplorer de = new DockerExplorer();
		de.open();
		ConnectionItem dockerConnection = de.getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		dc.expand();
		TreeItem imagesItem = dc.getItem("Images");
		imagesItem.select();
		imagesItem.expand();
		for (TreeItem item : imagesItem.getItems()) {
			if (item.getText().contains(imageName)) {
				item.select();
				new ContextMenu("Remove").select();
				new WaitUntil(new ShellWithTextIsActive("Confirm Remove Image"), TimePeriod.NORMAL);
				new PushButton("OK").click();
				new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
			}
		}
	}

}
