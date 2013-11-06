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

import static org.junit.Assert.assertThat;

import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.hamcrest.core.Is;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.archives.reddeer.archives.ui.NewJarDialog;
import org.jboss.tools.archives.reddeer.component.ArchiveProject;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Checks if archive error dialog is invoked when there is an issue
 * when manipulating with the invalid archive 
 * 
 * @author jjankovi
 *
 */
public class ArchivesErrorDialogTest extends ArchivesTestBase {

	private static String project = "prj";
	private static final String LOCATION = "/usr/";
	private static final String ARCHIVE = project + ".jar [" + LOCATION + "]";
	
	@BeforeClass
	public static void setup() {
		new LogView().open();
		clearErrorView();
		createJavaProject(project);
	}
	
	@Test
	public void testErrorDialogAppearance() {
		
		view = viewForProject(project);
		ArchiveProject projectInView = view.getProject();
		NewJarDialog dialog = projectInView.newJarArchive();
		
		/* location is set to /usr/ which should be not able to accessed to */
		dialog.setFileSystemRelative();
		/**
		 * will be replaced by dialog.setDestination(LOCATION) once
		 * issue https://github.com/jboss-reddeer/reddeer/issues/169 
		 * is resolved
		 */
		new SWTBot().text(1).setText("");
		new SWTBot().text(1).typeText(LOCATION);
		dialog.finish();
		
		/*
		 * building archive error should be invoked, because of 
		 * accessing to /usr/ folder
		 */ 
		
		view.open();
		projectInView.getArchive(ARCHIVE).buildArchiveFull();
		
		handleDialogsIfInvoked();
		assertBuildingArchiveErrorExists(
				LOCATION + project + ".jar" ,project + ".jar");
	}

	private void handleDialogsIfInvoked() {
		
		try {
			new DefaultShell("Problem Occurred").close();
			new DefaultShell("Error building project archives").close();
		} catch (SWTLayerException sle) {
			// do nothing here, no error dialog was opened
		}
		
	}
	
	private void assertBuildingArchiveErrorExists(String location, String archive) {
		
		LogView errorLog = new LogView();
		errorLog.open();
		
		List<LogMessage> buildingError = null; 
		
		for (LogMessage msg : errorLog.getErrorMessages()) {
			String pluginId = msg.getPlugin();
			if (pluginId.contains("org.jboss.ide.eclipse.archives") && 
				msg.getMessage().equals("An error occurred while building project archives")) {
				buildingError = msg.getSubLogMessages();
			}
		}
		
		assertNotNull("No building archive error was found in error log", buildingError);
		
		assertThat(buildingError.size(), Is.is(1));
		assertThat(buildingError.get(0).getMessage(), Is.is("Error creating output file " 
				+ location + " for node " + archive));
		
	}
	
}
