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
package org.jboss.tools.arquillian.ui.bot.reddeer.profile;

import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.ShellIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTableItem;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * Dialog for adding Arquillian profiles.
 * 
 * @author Lucia Jelinkova
 *
 */
public class AddArquillianProfilesDialog {
	
	private static final String NAME = "Add Arquillian Profiles";
	
	public void open(Project project){
		project.select();
		new ContextMenu("Configure","Add Arquillian Profiles...").select();
	}
	
	public void ok(){
		activate();
		String shellText = new DefaultShell().getText();
		Button button = new PushButton("OK");
		button.click();

		new WaitWhile(new ShellIsActive(shellText), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	public void selectProfile(String profile){
		activate();
		new DefaultTableItem(new WithTextMatcher(profile)).setChecked(true);
	}
	
	private void activate(){
		// sets focus
		new DefaultShell(NAME);
	}
}
