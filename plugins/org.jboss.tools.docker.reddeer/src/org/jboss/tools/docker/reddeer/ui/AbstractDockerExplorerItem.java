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

import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.core.resources.AbstractExplorerItem;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.swt.api.TreeItem;

/**
 * 
 * @author jkopriva
 *
 */

public abstract class AbstractDockerExplorerItem extends AbstractExplorerItem{

	public AbstractDockerExplorerItem(TreeItem treeItem) {
		super(treeItem);
	}
	
	public List<ConnectionItem> getChildrenConnection() {
		activateWrappingView();
		List<ConnectionItem> children = new ArrayList<ConnectionItem>();

		for (TreeItem item : treeItem.getItems()) {
			String name = item.getText();
			String[] childPath = new String[treeItem.getPath().length + 1];
			System.arraycopy(treeItem.getPath(), 0, childPath, 0,
					treeItem.getPath().length);
			childPath[childPath.length - 1] = name;
			children.add(new ConnectionItem(item));
		}

		return children;
	}
	
	public ConnectionItem getChildConnection(String text) {
		activateWrappingView();
		String[] childPath = new String[treeItem.getPath().length + 1];
		System.arraycopy(treeItem.getPath(), 0, childPath, 0,
				treeItem.getPath().length);
		childPath[childPath.length - 1] = text;
		return getConnectionItem(text);
	}
	
	
	public ConnectionItem getConnectionItem(String... path) {
		activateWrappingView();
		TreeItem item = treeItem;
		for (int i = 0; i < path.length; i++) {
			String pathSegment = path[i];
			try {
				item = item.getItem(pathSegment);
			} catch (CoreLayerException ex) {
				// there is no item with specific path segment, time to use name
				// without decorators
				try {
					item = treeViewerHandler.getTreeItem(item, pathSegment);
				} catch (JFaceLayerException exception) {
					// non existing item
					logger.debug("Obtaining direct children on the current level");
					List<TreeItem> items = item.getItems();
					logger.debug("Item \"" + pathSegment + "\" was not found. Available items on the current level:");
					for (TreeItem treeItem: items) {
						logger.debug("\"" + treeItem.getText() + "\"");
					}
					throw new EclipseLayerException(
							"Cannot get connection item specified by path."
									+ "Connection item either does not exist or solution is ambiguous because "
									+ "of existence of more items on the path with same name without decorators");
				}
			}
		}
		return new ConnectionItem(item);
	}
	
	
	
	

}
