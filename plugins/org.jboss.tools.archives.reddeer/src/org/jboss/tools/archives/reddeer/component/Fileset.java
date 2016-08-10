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

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.archives.reddeer.archives.ui.FilesetDialog;

/**
 * Fileset retrieved from Project Archives view/explorer
 * 
 * @author jjankovi
 *
 */
public class Fileset {

	private TreeItem fileset;

	public Fileset(TreeItem fileset) {
		this.fileset = fileset;
	}
	
	public String getName() {
		return fileset.getText();
	}
	
	public FilesetDialog editFileset() {
		fileset.select();
		new ContextMenu("Edit Fileset").select();
		return new FilesetDialog();
	}
	
	public void deleteFileset(boolean withContextMenu) {
		fileset.select();
		
		new ContextMenu("Delete Fileset").select();
		try {
			Shell s = new DefaultShell("Delete selected nodes?");
			new PushButton("Yes").click();
			new WaitWhile(new ShellIsAvailable(s));
		} catch (RedDeerException e) {
			//do nothing here
		}
		new WaitWhile(new JobIsRunning());
	}
	
}
