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
import org.jboss.tools.aerogear.reddeer.ui.wizard.export.ExportNativePlatformProjectPage;
import org.jboss.tools.aerogear.reddeer.ui.wizard.export.ExportNativePlatformProjectWizard;
import org.junit.Test;
/**
 * Checks export of Hybrid Mobile project as Mobile project 
 * @author Vlado Pakan
 *
 */
public class ExportNativePlatformProject extends ExportMobileTest {
	  @Test
	  public void testExportNativePlatformProject() {
	    new ProjectExplorer().selectProjects(CORDOVA_PROJECT_NAME);
	    ExportNativePlatformProjectWizard exportNativePlatformProjectWizard = new ExportNativePlatformProjectWizard();
	    exportNativePlatformProjectWizard.open();
	    ExportNativePlatformProjectPage exportNativePlatformProjectPage = 
	      (ExportNativePlatformProjectPage)exportNativePlatformProjectWizard.getCurrentWizardPage();
	    assertTrue("Project " + CORDOVA_PROJECT_NAME + " has to be selected",
	        exportNativePlatformProjectPage.isProject(CORDOVA_PROJECT_NAME));
	    exportNativePlatformProjectPage.setPlatform("Android",true);
	    final String expectedExportFileName = CORDOVA_PROJECT_NAME;
	    final boolean isExpectedDirectory = true;
	    final String outputDirectory = prepareOutputDirectory(expectedExportFileName , isExpectedDirectory);
	    exportNativePlatformProjectPage.setOutputDirectory(outputDirectory);
	    exportNativePlatformProjectWizard.finish();
	    assertExportedFileExists (expectedExportFileName,outputDirectory,isExpectedDirectory);
	    cleanOutputDirectory(expectedExportFileName, outputDirectory, isExpectedDirectory);
	  }
}
