/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation    
 ******************************************************************************/
package org.jboss.tools.arquillian.ui.bot.reddeer.support;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.swt.api.Button;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

/**
 * Dialog shown after user selects on Maven project Configure -> Add Arquillian Support 
 * 
 * @author Lucia Jelinkova
 *
 */
public class AddArquillianSupportDialog {
	
	private static final String NAME = "Add Arquillian support";
	
	public void open(Project project){
		project.select();
		new ContextMenuItem("Configure","Add Arquillian Support...").select();
	}

	public void ok(){
		activate();
		String shellText = new DefaultShell().getText();
		Button button = new PushButton("OK");
		button.click();

		new WaitWhile(new ShellIsAvailable(shellText), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	private void activate(){
		// sets focus
		new DefaultShell(NAME);
	}
}
