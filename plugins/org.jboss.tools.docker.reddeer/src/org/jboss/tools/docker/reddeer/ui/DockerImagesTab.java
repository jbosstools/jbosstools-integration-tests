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
import org.jboss.reddeer.swt.impl.table.DefaultTable;
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
		for (TableItem item : getTableItems()){
			if (item.getText().equals(dockerImageName)){
				return item;
			}
		}
		throw new EclipseLayerException("There is no connection with name " + dockerImageName);
	}
	
	public void refresh(){
		new DefaultToolItem("Refresh (F5)").click();
		
	}
	
	public List<TableItem> getTableItems(){
		return new DefaultTable().getItems();
		
	}
	

	

}
