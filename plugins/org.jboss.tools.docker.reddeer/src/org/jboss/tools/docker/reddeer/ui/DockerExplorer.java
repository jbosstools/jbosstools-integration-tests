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

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.swt.api.TreeItem;
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
		for (ConnectionItem connectionItem : getConnections()){
			if (connectionItem.getName().contains(connectionName)){
				return connectionItem;
			}
		}
		throw new EclipseLayerException("There is no connection with name " + connectionName);
	}
	
	private List<ConnectionItem>  getConnections() {
		List<ConnectionItem> connections = new ArrayList<ConnectionItem>();
		
		for (TreeItem item : getTree().getItems()){			
				connections.add(new ConnectionItem(item));
		}
		return connections;
	}

	public void refresh(){
		new DefaultToolItem("Refresh").click();
		
	}
	
	private DefaultTree getTree(){
		activate();
		return new DefaultTree();
	}

	

}
