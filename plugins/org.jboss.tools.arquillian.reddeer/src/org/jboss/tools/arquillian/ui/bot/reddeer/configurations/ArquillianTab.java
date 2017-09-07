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

package org.jboss.tools.arquillian.ui.bot.reddeer.configurations;

import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.jboss.tools.maven.reddeer.profiles.SelectProfilesDialog;

/**
 * Arquillian tab in JUnit configuration
 * 
 * @author Lucia Jelinkova
 *
 */
public class ArquillianTab extends RunConfigurationTab {

	private static final String MAVEN_DIALOG_TITLE = "Select Maven profiles";
	
	public ArquillianTab() {
		super("Arquillian");
	}

	public void selectMavenProfile(String profile){
		activate();
		new PushButton("Select Maven Profiles").click();
		new DefaultShell(MAVEN_DIALOG_TITLE);
		
		SelectProfilesDialog dialog = new SelectProfilesDialog(null);
		dialog.activateProfile(profile);
		new PushButton("OK").click();
		
		new WaitWhile(new ShellIsAvailable(MAVEN_DIALOG_TITLE), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
}
