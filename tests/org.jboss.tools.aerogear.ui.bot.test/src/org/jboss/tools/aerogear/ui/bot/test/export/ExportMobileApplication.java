/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.ui.bot.test.export;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.tools.aerogear.reddeer.ui.wizard.export.ExportMobileApplicationPage;
import org.jboss.tools.aerogear.reddeer.ui.wizard.export.ExportMobileApplicationWizard;
import org.junit.Test;
/**
 * Checks export of Hybrid Mobile project as Mobile application 
 * @author Vlado Pakan
 *
 */
public class ExportMobileApplication extends ExportMobileTest {
	@Test
	public void testExportMobileApplication() {
	  new ProjectExplorer().selectProjects(CORDOVA_PROJECT_NAME);
	  ExportMobileApplicationWizard exportMobileApplicationWizard = new ExportMobileApplicationWizard();
	  exportMobileApplicationWizard.open();
	  ExportMobileApplicationPage exportMobileApplicationPage = new ExportMobileApplicationPage();
	  assertTrue("Project " + CORDOVA_PROJECT_NAME + " has to be selected",
	    exportMobileApplicationPage.isProject(CORDOVA_PROJECT_NAME));
	  exportMobileApplicationPage.setPlatform("Android",true);
	  final String expectedExportFileName = CORDOVA_APP_NAME + "-release-unsigned.apk";
	  final boolean isExpectedDirectory = false;
	  final String outputDirectory = prepareOutputDirectory(expectedExportFileName , isExpectedDirectory);
	  exportMobileApplicationPage.setOutputDirectory(outputDirectory);
	  exportMobileApplicationWizard.finish();
	  assertExportedFileExists (expectedExportFileName,outputDirectory,isExpectedDirectory);
	  cleanOutputDirectory(expectedExportFileName, outputDirectory, isExpectedDirectory);
	}
}
