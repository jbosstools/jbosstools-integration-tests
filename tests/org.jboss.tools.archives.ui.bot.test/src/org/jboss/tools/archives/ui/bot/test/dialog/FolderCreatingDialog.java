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

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * 
 * @author jjankovi
 *
 */
public class FolderCreatingDialog {

	private SWTBotShell shell = null;
	private SWTBot bot = null;
	private static final String DIALOG_TITLE = "Create a folder";
	
	public FolderCreatingDialog() {
		shell = SWTBotFactory.getBot().shell(getDialogTitle());
		bot = shell.bot();
	}

	private String getDialogTitle() {
		return DIALOG_TITLE;
	}
	
	public FolderCreatingDialog setNameOfFolder(String fileName) {
		bot.text().setText(fileName);
		return this;
	}
	
	public void cancel() {
		bot.button(IDELabel.Button.CANCEL).click();
	}
	
	public void ok() {
		bot.button(IDELabel.Button.OK).click();
		SWTBotFactory.getUtil().waitForNonIgnoredJobs();
	}
	
}
