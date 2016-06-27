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

import java.util.List;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;

import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;

import org.jboss.reddeer.workbench.impl.view.WorkbenchView;

/**
 * 
 * @author jkopriva
 *
 */

public class DockerImagesTab extends WorkbenchView {

	public DockerImagesTab() {
		super("Docker Images");
	}

	public TableItem getDockerImage(String dockerImageName) {
		activate();
		for (TableItem item : getTableItems()) {
			if (item.getText(1).contains(dockerImageName)) {
				return item;
			}
		}
		throw new EclipseLayerException("There is no Docker image with name " + dockerImageName);
	}

	public void refresh() {
		new DefaultToolItem("Refresh (F5)").click();

	}

	public List<TableItem> getTableItems() {
		return new DefaultTable().getItems();

	}

	public void buildImage(String name, String directory) {
		new DefaultToolItem("Build Image").click();
		new LabeledText("Name:").setText(name);
		new LabeledText("Directory:").setText(directory);
		new FinishButton().click();
	}

	public void runImage(String imageName) {
		TableItem image = getDockerImage(imageName);
		image.select();
		new ContextMenu("Run...").select();
	}

}
