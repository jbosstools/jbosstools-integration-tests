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
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * 
 * @author jjankovi
 *
 */
public abstract class ArchiveCreationDialogBase {

	private SWTBotShell shell = null;
	private SWTBot bot = null;
	
	public ArchiveCreationDialogBase() {
		shell = SWTBotFactory.getBot().shell(getDialogTitle());
		shell.activate();
		bot = shell.bot();
	}
	
	public ArchiveCreationDialogBase setArchiveName(String archiveName) {
		String newArchiveName = archiveName.contains(".jar")?
				archiveName:archiveName + ".jar";
		bot.textWithLabel("Archive name:").setText(newArchiveName);
		return this;
	}
	
	public ArchiveCreationDialogBase setDestination(String location) {
		bot.textWithLabel("Destination:").setText(location);
		return this;
	}
	
	public ArchiveCreationDialogBase setFileSystemRelative() {
		bot.radio(0).click();
		return this;
	}
	
	public ArchiveCreationDialogBase setWorkspaceRelative() {
		bot.radio(1).click();
		return this;
	}
	
	public ArchiveCreationDialogBase setZipStandardArchiveType() {
		bot.radio(2).click();
		return this;
	}
	
	public ArchiveCreationDialogBase setNoCompressionArchiveType() {
		bot.radio(3).click();
		return this;
	}
	
	public void cancel() {
		bot.button(IDELabel.Button.CANCEL).click();
	}
	
	public void finish() {
		bot.button(IDELabel.Button.FINISH).click();
		SWTBotFactory.getUtil().waitForNonIgnoredJobs();
		bot.sleep(Timing.time1S());
	}

	public abstract String getDialogTitle();
	
}
