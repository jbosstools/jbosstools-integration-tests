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

import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.common.reddeer.label.IDELabel;

/**
 * 
 * @author jjankovi
 * @author Radoslav Rabara
 */
public class RunOnServerDialog extends DefaultShell {

	private static final String DIALOG_TITLE = "Run On Server";

	public RunOnServerDialog() {
		super(DIALOG_TITLE);
	}

	public void chooseExistingServer() {
		new RadioButton().click();
	}

	public List<TreeItem> getServers() {
		for(TreeItem ti : new DefaultTree().getItems()) {
			if(ti.getCell(0).equals("localhost")) {
				return ti.getItems();
			}
		}
		return null;
	}

	public void selectServer(String server) {
		for (TreeItem ti : getServers()) {
			if(ti.getCell(0).equals(server)) {
				ti.select();
				break;
			}
		}
	}

	public void finish() {
		new PushButton(IDELabel.Button.FINISH).click();
	}

	public void cancel() {
		new PushButton(IDELabel.Button.CANCEL).click();
	}
}