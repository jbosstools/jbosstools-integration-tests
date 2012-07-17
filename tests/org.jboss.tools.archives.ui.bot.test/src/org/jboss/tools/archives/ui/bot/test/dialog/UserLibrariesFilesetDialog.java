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
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * 
 * @author jjankovi
 *
 */
public class UserLibrariesFilesetDialog {

	private SWTBotShell shell = null;
	private SWTBot bot = null;
	private static final String DIALOG_TITLE = "User Library Fileset Wizard";
	
	public UserLibrariesFilesetDialog() {
		shell = SWTBotFactory.getBot().shell(getDialogTitle());
		bot = shell.bot();
	}

	private String getDialogTitle() {
		return DIALOG_TITLE;
	}
	
	public List<String> getUserLibraries() {
		List<String> userLibraries = new ArrayList<String>();
		for (SWTBotTreeItem ti : bot.tree().getAllItems()) {
			userLibraries.add(ti.getText());
		}
		return userLibraries;
	}
	
	public UserLibrariesFilesetDialog selectUserLibrary(String library) {
		bot.tree().select(library);
		return this;
	}
	
	public void cancel() {
		bot.button(IDELabel.Button.CANCEL).click();
	}
	
	public void finish() {
		bot.button(IDELabel.Button.FINISH).click();
		SWTBotFactory.getUtil().waitForNonIgnoredJobs();
	}
	
}
