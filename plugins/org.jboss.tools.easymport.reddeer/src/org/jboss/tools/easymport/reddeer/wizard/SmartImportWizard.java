/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.easymport.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
/**
 * 
 * @author rhopp
 *
 */

public class SmartImportWizard extends WizardDialog {

	public void open() {
		log.info("Opening wizard using top menu ");
		new ShellMenu(getMenuPath()).select();
		new DefaultShell(getDialogTitle());
	}

	protected String getDialogTitle() {
		return "Import projects";
	}
	
	protected String[] getMenuPath() {
		return new String[]{"File", "Open Projects..."};
	}
	
}
