/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.archives.ui.bot.test.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * 
 * @author jjankovi
 *
 */
public class ArchivePublishSettingsDialog {

	private SWTBotShell shell = null;
	private SWTBot bot = null;
	private final String DIALOG_TITLE = "Archive Publish Settings";
	
	
	public ArchivePublishSettingsDialog() {
		shell = SWTBotFactory.getBot().shell(getDialogTitle());
		bot = shell.bot();
	}

	private String getDialogTitle() {
		return DIALOG_TITLE;
	}
	
	public List<String> getAllServersInDialog() {
		List<String> serversInDialog = new ArrayList<String>();
		for (int i = 0; i < table().rowCount(); i++) {
			serversInDialog.add(table().getTableItem(i).getText());
		}
		return serversInDialog;
	}
	
	public List<String> getAllSelectedServersInDialog() {
		/* TODO */
		List<String> selectedServers = new ArrayList<String>();
		return selectedServers;
	}
	
	public ArchivePublishSettingsDialog selectServers(String... serversToSelect) {
		table().select(serversToSelect);
		return this;
	}
	
	public ArchivePublishSettingsDialog unselectServers(String... serversToUnselect) {
		/* get all selected servers - for future use */
		List<String> selectedServers = getAllSelectedServersInDialog();
		
		/* unselect all servers */
		table().unselect();
		
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
	
	public ArchivePublishSettingsDialog unselectAllServers() {
		table().unselect();
		return this;
	}
	
	public ArchivePublishSettingsDialog checkAlwaysPublish() {
		this.bot.checkBox(0).select();
		return this; 
	}
	
	public ArchivePublishSettingsDialog uncheckAlwaysPublish() {
		this.bot.checkBox(0).deselect();
		return this; 
	}
	
	public ArchivePublishSettingsDialog checkAutoDeploy() {
		this.bot.checkBox(1).select();
		return this; 
	}
	
	public ArchivePublishSettingsDialog uncheckAutoDeploy() {
		this.bot.checkBox(1).deselect();
		return this; 
	}
	
	private SWTBotTable table() {
		return this.bot.table();
	}
	
	public void cancel() {
		bot.button(IDELabel.Button.CANCEL).click();
	}
	
	public void finish() {
		bot.button(IDELabel.Button.FINISH).click();
		SWTBotFactory.getUtil().waitForNonIgnoredJobs();
	}
	
}
