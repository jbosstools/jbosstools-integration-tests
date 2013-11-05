/*******************************************************************************
 * Copyright (c) 2010-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.reddeer.component;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.tools.archives.reddeer.archives.ui.FilesetDialog;

/**
 * Fileset retrieved from Project Archives view/explorer
 * 
 * @author jjankovi
 *
 */
public class Fileset {

	private TreeItem fileset;
	private ArchiveContextMenuAction menuAction;

	public Fileset(TreeItem fileset) {
		this.fileset = fileset;
		menuAction = new ArchiveContextMenuAction();
	}
	
	public String getName() {
		return fileset.getText();
	}
	
	public FilesetDialog editFileset() {
		fileset.select();
		return menuAction.editFileset();
	}
	
	public void deleteFileset(boolean withContextMenu) {
		fileset.select();
		menuAction.deleteFileset(withContextMenu);
	}
	
}
