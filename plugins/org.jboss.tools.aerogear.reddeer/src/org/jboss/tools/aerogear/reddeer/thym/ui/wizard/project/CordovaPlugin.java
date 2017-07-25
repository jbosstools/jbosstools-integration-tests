/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project;

import org.jboss.reddeer.swt.api.TreeItem;

public class CordovaPlugin {
	
	private TreeItem item;
	
	public CordovaPlugin(TreeItem item) {
		this.item = item;
	}
	
	public String getPluginId() {
		return item.getCell(1);
	}
	
	public void check() {
		item.setChecked(true);
	}
	
	public void select() {
		item.select();
	}
	

}
