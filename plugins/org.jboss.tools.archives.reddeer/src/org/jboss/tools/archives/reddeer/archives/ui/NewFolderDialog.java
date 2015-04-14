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
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * Dialog for creating or modifying a folder
 * @author jjankovi
 *
 */
public class NewFolderDialog extends DefaultShell {

	private static final String DIALOG_TITLE = "Create a folder";
	
	public NewFolderDialog() {
		super(DIALOG_TITLE);
	}

	public NewFolderDialog setNameOfFolder(String fileName) {
		new DefaultText().setText(fileName);
		return this;
	}
	
	public void cancel() {
		new PushButton("Cancel").click();
	}
	
	public void ok() {
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning());
	}
	
}
