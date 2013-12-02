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
import org.jboss.tools.archives.reddeer.archives.jdt.integration.LibFilesetDialog;

/**
 * User library fileset retrieved from Project Archives view/explorer
 * 
 * @author jjankovi
 *
 */
public class UserLibraryFileset {

	private TreeItem userLibrary;
	private ArchiveContextMenuAction menuAction;

	public UserLibraryFileset(TreeItem userLibrary) {
		this.userLibrary = userLibrary;
		menuAction = new ArchiveContextMenuAction();
	}
	
	public String getName() {
		return userLibrary.getText();
	}
	
	public LibFilesetDialog editUserLibraryFileset() {
		userLibrary.select();
		return menuAction.editUserLibraryFileset();
	}
	
	public void deleteUserLibraryFileset(boolean withContextMenu) {
		userLibrary.select();
		menuAction.deleteUserLibraryFileset(withContextMenu);
	}
	
}
