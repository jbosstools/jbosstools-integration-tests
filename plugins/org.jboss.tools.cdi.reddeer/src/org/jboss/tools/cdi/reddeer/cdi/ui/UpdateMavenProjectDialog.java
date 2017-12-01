/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.jface.dialogs.TitleAreaDialog;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.CancelButton;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.OkButton;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

/**
 * Represends {@value #SHELL_TITLE} dialog. Can operate OK and Cancel buttons and "Clean projects" checkbox.
 * 
 * @author lvalach
 *
 */
public class UpdateMavenProjectDialog extends TitleAreaDialog {

	public static final String SHELL_TITLE = "Update Maven Project";
	public static final String CLEAN_PROJECTS = "Clean projects";

	public UpdateMavenProjectDialog() {
		super(SHELL_TITLE);
	}

	/**
	 * Click "OK" button.
	 */
	public void ok() {
		new OkButton(this).click();
		new WaitWhile(new ShellIsAvailable(SHELL_TITLE));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
	}

	/**
	 * Click "Cancel" button.
	 */
	public void cancel() {
		new CancelButton(this).click();
		new WaitWhile(new ShellIsAvailable(SHELL_TITLE));
	}

	/**
	 * Sets {@value #CLEAN_PROJECTS} checkbox to state 'checked'.
	 *
	 * @param checked
	 *            whether check or not
	 */
	public void clean(Boolean checked) {
		new CheckBox(this, CLEAN_PROJECTS).toggle(checked);
	}

	/**
	 * Returns true when Check Box {@value #CLEAN_PROJECTS} is checked.
	 *
	 * @return true, if is checked
	 */
	public boolean isCDISupport() {
		return new CheckBox(this, CLEAN_PROJECTS).isChecked();
	}
}
