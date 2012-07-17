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
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * 
 * @author jjankovi
 *
 */
public class FilesetDialog {

	private SWTBotShell shell = null;
	private SWTBot bot = null;
	private static final String DIALOG_TITLE = "Fileset Wizard";
	
	public FilesetDialog() {
		shell = SWTBotFactory.getBot().shell(getDialogTitle());
		bot = shell.bot();
	}

	private String getDialogTitle() {
		return DIALOG_TITLE;
	}
	
	public FilesetDialog setFlatten(boolean set) {
		if (set) {
			bot.radio(2).click();
		} else {
			bot.radio(3).click();
		}
		return this;
	}
	
	public FilesetDialog setIncludes(String pattern) {
		bot.textWithLabel("Includes:").setText(pattern);
		return this;
	}
	
	public String getIncludes() {
		return bot.textWithLabel("Includes:").getText();
	}
	
	public FilesetDialog setExcludes(String pattern) {
		bot.textWithLabel("Excludes:").setText(pattern);
		return this;
	}
	
	public String getExcludes() {
		return bot.textWithLabel("Excludes:").getText();
	}
	
	public List<String> getPreview() {
		List<String> preview = new ArrayList<String>();
		for (int i = 0; i < bot.table().rowCount(); i++) {
			preview.add(bot.table().getTableItem(i).getText());
		}
		return preview;
	}
	
	public void cancel() {
		bot.button(IDELabel.Button.CANCEL).click();
	}
	
	public void finish() {
		bot.button(IDELabel.Button.FINISH).click();
		SWTBotFactory.getUtil().waitForNonIgnoredJobs();
	}
	
}
