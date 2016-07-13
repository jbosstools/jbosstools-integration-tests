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

package org.jboss.tools.docker.reddeer.ui;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * 
 * @author jkopriva
 *
 */

public class DockerExplorer extends WorkbenchView {

	public DockerExplorer() {
		super("Docker Explorer");
	}

	public ConnectionItem getConnection(String connectionName) {
		activate();
		for (ConnectionItem connectionItem : getConnections()) {
			if (connectionItem.getName() != null && connectionItem.getName().contains(connectionName)) {
				return connectionItem;
			}
		}
		throw new EclipseLayerException("There is no connection with name " + connectionName);
	}

	public boolean connectionExist(String connectionName) {
		activate();
		try {
			for (ConnectionItem connectionItem : getConnections()) {
				if (connectionItem.getName().contains(connectionName)) {
					return true;
				}
			}
		} catch (CoreLayerException ex) {
			return false;
		}
		return false;
	}

	private List<ConnectionItem> getConnections() {
		List<ConnectionItem> connections = new ArrayList<ConnectionItem>();

		for (TreeItem item : getTree().getItems()) {
			connections.add(new ConnectionItem(item));
		}
		return connections;
	}

	public void refresh() {
		new DefaultToolItem("Refresh").click();

	}

	private DefaultTree getTree() {
		activate();
		return new DefaultTree();
	}

	public void pullImage(String dockerServerURI, String register, String imageName) {
		DockerExplorer de = this;
		de.open();
		de.getConnection(dockerServerURI);
		new ContextMenu("Pull...").select();
		new WaitUntil(new ShellWithTextIsAvailable("Pull Image"), TimePeriod.NORMAL);

		// select register
		if (register != null) {
			String fullRegisterName = register;
			Combo combo = new DefaultCombo();
			List<String> comboItems = combo.getItems();
			for (String item : comboItems) {
				if (item.contains(register)) {
					fullRegisterName = item;
					break;
				}
			}
			combo.setSelection(fullRegisterName);
		}
		// enter image name in dialog
		new LabeledText("Name:").setFocus();
		new LabeledText("Name:").setText(imageName);

		new PushButton("Finish").click();
		AbstractWait.sleep(TimePeriod.getCustom(5));
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	public void pullImage(String dockerServerURI, String imageName) {
		pullImage(dockerServerURI, null, imageName);
	}
	
	public void openImageSearchDialog(String dockerConnectionName, String register, String imageName){
		DockerExplorer de = this;
		de.open();
		de.getConnection(dockerConnectionName);
		new ContextMenu("Pull...").select();
		new WaitUntil(new ShellWithTextIsAvailable("Pull Image"), TimePeriod.NORMAL);

		// select register
		if (register != null) {
			String fullRegisterName = register;
			Combo combo = new DefaultCombo();
			List<String> comboItems = combo.getItems();
			for (String item : comboItems) {
				if (item.contains(register)) {
					fullRegisterName = item;
					break;
				}
			}
			combo.setSelection(fullRegisterName);
		}
		// enter image name in dialog
		new LabeledText("Name:").setFocus();
		new LabeledText("Name:").setText(imageName);

		new PushButton("Search...").click();
	}

	public void deleteContainer(String dockerServer, String containerName) {
		open();
		ConnectionItem dockerConnection = getConnection(dockerServer);
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
				new WaitUntil(new ShellWithTextIsAvailable("Confirm Remove Container"), TimePeriod.NORMAL);
				new PushButton("OK").click();
				new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			}
		}
	}

	public void deleteImage(String dockerServer, String imageName) {
		open();
		ConnectionItem dockerConnection = getConnection(dockerServer);
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
				new WaitUntil(new ShellWithTextIsAvailable("Confirm Remove Image"), TimePeriod.NORMAL);
				new PushButton("OK").click();
				new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			}
		}
	}

	public boolean imageIsDeployed(String dockerServer, String imageName) {
		open();
		ConnectionItem dockerConnection = getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		dc.expand();
		TreeItem imagesItem = dc.getItem("Images");
		imagesItem.select();
		imagesItem.expand();
		for (TreeItem item : imagesItem.getItems()) {
			if (item.getText().contains(imageName)) {
				return true;
			}
		}
		return false;
	}

	public boolean containerIsDeployed(String dockerServer, String containerName) {
		open();
		ConnectionItem dockerConnection = getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		dc.expand();
		TreeItem containerItem = dc.getItem("Containers");
		containerItem.select();
		containerItem.expand();
		for (TreeItem item : containerItem.getItems()) {
			if (item.getText().contains(containerName)) {
				return true;
			}
		}
		return false;
	}

	public void deleteConnection(String dockerServer) {
		open();
		ConnectionItem dockerConnection = getConnection(dockerServer);
		TreeItem dc = dockerConnection.getTreeItem();
		dc.select();
		new DefaultToolItem("Remove Connection").click();
	}
	
	public void enableConnection(String dockerConnection){
		open();
		new DefaultToolItem("Enable Connection").click();
	}

}
