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

import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * Base dialog class for EditArchiveDialog and NewJarDialog
 * 
 * @author jjankovi
 *
 */
public abstract class ArchiveDialogBase extends DefaultShell {

	private String dialogTitle;
	
	public ArchiveDialogBase(String dialogTitle) {
		super(dialogTitle);
		this.dialogTitle = dialogTitle;
	}
	
	public ArchiveDialogBase setArchiveName(String archiveName) {
		String newArchiveName = archiveName.contains(".jar")?
				archiveName:archiveName + ".jar";
		new LabeledText("Archive name:").setText(newArchiveName);
		return this;
	}
	
	public ArchiveDialogBase setDestination(String location) {
		new LabeledText("Destination:").setText("");
		new LabeledText("Destination:").setText(location);
		return this;
	}
	
	public ArchiveDialogBase setFileSystemRelative() {
		new RadioButton("Filesystem Relative").click();
		return this;
	}
	
	public ArchiveDialogBase setWorkspaceRelative() {
		new RadioButton("Workspace Relative").click();
		return this;
	}
	
	public ArchiveDialogBase setZipStandardArchiveType() {
		new RadioButton("Standard archive using zip compression").click();
		return this;
	}
	
	public ArchiveDialogBase setNoCompressionArchiveType() {
		new RadioButton("Exploded archive resulting in a folder (no compression)").click();
		return this;
	}
	
	public void cancel() {
		new PushButton("Cancel").click();
	}
	
	public void finish() {
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive(dialogTitle));
		new WaitWhile(new JobIsRunning());
	}

}
