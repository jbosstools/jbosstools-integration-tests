/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.reddeer.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.util.Display;
import org.jboss.reddeer.core.util.ResultRunnable;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.AbstractTreeItem;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;

public class OpenShiftProject extends AbstractOpenShiftExplorerItem {
	
	public OpenShiftProject(TreeItem projectItem) {
		super(projectItem);
	}
	
	/**
	 * Gets all resources of specific type for project.
	 * 
	 * @param resourceType resource type
	 * @return list of resources of specified type or empty list if there are no resources
	 */
	public List<OpenShiftResource> getOpenShiftResources(Resource resourceType) {
		List<OpenShiftResource> resources = new ArrayList<OpenShiftResource>();
		TreeItem resourceTypeTreeItem = item.getItem(resourceType.toString());
		resourceTypeTreeItem.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();	
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
				
		resourceTypeTreeItem.expand();
		
		new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false, TimePeriod.NONE);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		LinkedList<TreeItem> items = new LinkedList<TreeItem>();
		final org.eclipse.swt.widgets.TreeItem swtItem = resourceTypeTreeItem.getSWTWidget();
		List<org.eclipse.swt.widgets.TreeItem> eclipseItems = Display.syncExec(
				new ResultRunnable<List<org.eclipse.swt.widgets.TreeItem>>() {
					
					@Override
					public List<org.eclipse.swt.widgets.TreeItem> run() {
						org.eclipse.swt.widgets.TreeItem[] items = swtItem.getItems();
						return Arrays.asList(items);
					}
		});
		
		for (org.eclipse.swt.widgets.TreeItem swtTreeItem : eclipseItems) {
			items.addLast(new OpenShiftResourceTreeItem(swtTreeItem));
		}
		
		if (items != null && !items.isEmpty()) {
			for (TreeItem resourceItem: items) {
				resources.add(new OpenShiftResource(resourceItem));
			}
		}
		
		return resources;
	}
	
	public void delete() {
		refresh();
		item.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_OS_PROJECT).select();
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_OS_PROJECT);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_OS_PROJECT), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	class OpenShiftResourceTreeItem extends AbstractTreeItem {

		protected OpenShiftResourceTreeItem(org.eclipse.swt.widgets.TreeItem swtWidget) {
			super(swtWidget);
		}
		
	}
}
