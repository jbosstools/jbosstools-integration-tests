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
package org.jboss.tools.archives.reddeer.archives.ui;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Dialog for deploying archive on servers 
 * 
 * @author jjankovi
 *
 */
public class ArchivePublishDialog extends DefaultShell {

	private static final String DIALOG_TITLE = "Archive Publish Settings";
	
	public ArchivePublishDialog() {
		super(DIALOG_TITLE);
	}

	public List<String> getAllServersInDialog() {
		List<String> serversInDialog = new ArrayList<String>();
		for (int i = 0; i < table().rowCount(); i++) {
			serversInDialog.add(table().getItem(i).getText(0));
		}
		return serversInDialog;
	}
	
	public List<String> getAllSelectedServersInDialog() {
		/* TODO */
		List<String> selectedServers = new ArrayList<String>();
		return selectedServers;
	}
	
	public ArchivePublishDialog selectServers(String... serversToSelect) {
		table().select(serversToSelect);
		return this;
	}
	
	public ArchivePublishDialog unselectServers(String... serversToUnselect) {
		/* get all selected servers - for future use */
		List<String> selectedServers = getAllSelectedServersInDialog();
		
		/* unselect all servers */
//		table().unselect();
		
		/* from all selected servers remove the ones that should be unselected */
		for (int i = 0; i < serversToUnselect.length; i++) {
			selectedServers.remove(serversToUnselect[i]);
		}
		
		/* select rest of the servers in selectedServers */
		String[] strArray = new String[selectedServers.size()];
		selectedServers.toArray(strArray);
		table().select(strArray);
		
		return this;
	}
	
	public ArchivePublishDialog unselectAllServers() {
//		table().unselect();
		return this;
	}
	
	public ArchivePublishDialog checkAlwaysPublish() {
		new CheckBox(0).toggle(true);
		return this; 
	}
	
	public ArchivePublishDialog uncheckAlwaysPublish() {
		new CheckBox(0).toggle(false);
		return this; 
	}
	
	public ArchivePublishDialog checkAutoDeploy() {
		new CheckBox(1).toggle(true);
		return this; 
	}
	
	public ArchivePublishDialog uncheckAutoDeploy() {
		new CheckBox(1).toggle(false);
		return this; 
	}
	
	private Table table() {
		return new DefaultTable();
	}
	
	public void cancel() {
		new PushButton("Cancel").click();
	}
	
	public void finish() {
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning());
	}
	
}
