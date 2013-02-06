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
package org.jboss.tools.archives.ui.bot.test;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.tools.archives.ui.bot.test.dialog.NewJarDialog;
import org.jboss.tools.archives.ui.bot.test.view.ProjectArchivesView;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.condition.ShellIsActiveCondition;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author jjankovi
 *
 */
public class ArchivesErrorDialogTest extends ArchivesTestBase {

	private static String project = "prj";
	private static final String LOCATION = "/usr/";
	private static final String ARCHIVE_PATH = project + ".jar [" + LOCATION + "]";
	
	@BeforeClass
	public static void setup() {
		showErrorView();
		clearErrorView();
		createJavaProject(project);
	}
	
	@Test
	public void testErrorDialogAppearance() {
		
		ProjectArchivesView view = viewForProject(project);
		NewJarDialog dialog = view.createNewJarArchive(project);
		
		/* location is set to /usr/ which should be not able to accessed to */
		dialog.setDestination(LOCATION);
		dialog.setFileSystemRelative();
		dialog.finish();
		
		/*
		 * building archive error should be invoked, because of 
		 * accessing to /usr/ folder
		 */ 
		view.buildArchiveNode(project, ARCHIVE_PATH);
		handleDialogsIfInvoked();
		assertBuildingArchiveErrorExists(
				LOCATION + project + ".jar" ,project + ".jar");
	}

	private void handleDialogsIfInvoked() {
		
		try {
			bot.waitUntil(new ShellIsActiveCondition("Problem Occurred"), Timing.time3S());
			for (SWTBotShell shell : bot.shells()) {
				if (shell.getText().equals("Problem Occurred")) {
					shell.close();
					continue;
				}
				if (shell.getText().equals("Error building project archives")) {
					shell.close();
					break;
				}
			}
		} catch (TimeoutException te) {
			// do nothing here, no error dialog was invoked
		}
		
	}
	
}
