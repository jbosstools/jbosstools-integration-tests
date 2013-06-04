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
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Dialog for creating or modifying fileset
 * 
 * @author jjankovi
 *
 */
public class FilesetDialog extends DefaultShell {

	private static final String DIALOG_TITLE = "Fileset Wizard";
	
	public FilesetDialog() {
		super(DIALOG_TITLE);
	}
	
	public FilesetDialog setFlatten(boolean set) {
		if (set) {
			new RadioButton(2).click();
		} else {
			new RadioButton(3).click();
		}
		return this;
	}
	
	public FilesetDialog setIncludes(String pattern) {
		new LabeledText("Includes:").setText(pattern);
		return this;
	}
	
	public String getIncludes() {
		return new LabeledText("Includes:").getText();
	}
	
	public FilesetDialog setExcludes(String pattern) {
		new LabeledText("Excludes:").setText(pattern);
		return this;
	}
	
	public String getExcludes() {
		return new LabeledText("Excludes:").getText();
	}
	
	public List<String> getPreview() {
		List<String> preview = new ArrayList<String>();
		Table table = new DefaultTable();
		for (int i = 0; i < table.rowCount(); i++) {
			preview.add(table.cell(i, 0));
		}
		return preview;
	}
	
	public void cancel() {
		new PushButton("Cancel").click();
	}
	
	public void finish() {
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning());
	}
	
}
