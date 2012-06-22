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
package org.jboss.tools.ws.ui.bot.test.uiutils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * 
 * @author jjankovi
 *
 */
public class RunOnServerDialog {

	private SWTBotShell shell = null;
	private SWTBot bot = null;
	private final String DIALOG_TITLE = "Run On Server";
	
	public RunOnServerDialog() {
		shell = SWTBotFactory.getBot().shell(getDialogTitle());
		bot = shell.bot();
	}

	private String getDialogTitle() {
		return DIALOG_TITLE;
	}
	
	public RunOnServerDialog chooseExistingServer() {
		bot.checkBox(0).select();
		return this;
	}
	
	public List<String> getServers() {
		List<String> servers = new ArrayList<String>();
		for (SWTBotTreeItem server : bot.tree().getTreeItem("localhost").getItems()) {
			servers.add(server.getText());
		}
		return servers;
	}
	
	public RunOnServerDialog selectServer(String server) {
		for (String serverInDialog : getServers()) {
			if (serverInDialog.contains(server)) {
				bot.tree().getTreeItem("localhost")
				.getNode(serverInDialog).select();
				break;
			}
		}
		return this;
	}
	
	public void finish() {
		bot.button(IDELabel.Button.FINISH).click();
	}
	
	public void cancel() {
		bot.button(IDELabel.Button.CANCEL).click();
	}
	
}