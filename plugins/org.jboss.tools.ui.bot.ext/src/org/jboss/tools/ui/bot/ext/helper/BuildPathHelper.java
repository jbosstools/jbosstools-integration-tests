/*******************************************************************************
 * Copyright (c) 2007-2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ui.bot.ext.helper;

import static org.junit.Assert.assertTrue;

import java.io.File;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.preferences.BuildPathsPropertyPage;
import org.jboss.reddeer.eclipse.ui.dialogs.ExplorerItemPropertyDialog;
/**
 * Helper for managing project build path
 * 
 * @author Vlado Pakan
 * 
 */
public class BuildPathHelper {
	
	/**
	 * Add External Jar File to Project Build Path. If External Jar File already
	 * exists method will fail
	 * 
	 * @param externalJarLocation
	 * @param projectName
	 * @return
	 */
	public static String addExternalJar(final String externalJarLocation,
			final String projectName) {

		return addExternalJar(externalJarLocation, projectName, false);

	}
	
	/**
	 * Adds External Jar File to Project Build Path. If External Jar File already
	 * exists and 'overwriteIfExists' parameter is set to true, it is overwritten
	 * 
	 * @param externalJarLocation
	 * @param projectName
	 * @return
	 */
	public static String addExternalJar(final String externalJarLocation,
			final String projectName, boolean overwriteIfExists) {
		assertTrue("External Jar Location cannot be empty but is " + externalJarLocation, 
				externalJarLocation != null && externalJarLocation.length() > 0);
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		ExplorerItemPropertyDialog propertyDialog = new ExplorerItemPropertyDialog(pe.getProject(projectName));
		propertyDialog.open();
		BuildPathsPropertyPage buildPathsPropertyPage = new BuildPathsPropertyPage();
		propertyDialog.select(buildPathsPropertyPage);
		String jarFileName = new File(externalJarLocation).getName();
		String result = buildPathsPropertyPage.addVariable(jarFileName.toUpperCase() + "_LOCATION", externalJarLocation, overwriteIfExists);
		propertyDialog.ok();
		return result;
	}

	/**
	 * Removes variable from project classpath
	 * 
	 * @param variableLabel
	 * @param removeGlobaly
	 */
	public static void removeVariable(String projectName, String variableLabel,
			boolean removeGlobaly) {
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		ExplorerItemPropertyDialog propertyDialog = new ExplorerItemPropertyDialog(pe.getProject(projectName));
		propertyDialog.open();
		BuildPathsPropertyPage buildPathsPropertyPage = new BuildPathsPropertyPage();
		propertyDialog.select(buildPathsPropertyPage);
		buildPathsPropertyPage.removeVariable(variableLabel, removeGlobaly);
		propertyDialog.ok();
	}

}
