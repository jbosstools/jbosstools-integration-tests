/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.reddeer.utils;

import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.platform.OS;
import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.tools.vpe.editor.util.VpePlatformUtil;
import org.jboss.tools.vpe.reddeer.preferences.VisualPageEditorPreferencePage;
/**
 * Handles web engine switching
 * 
 * @author vlado pakan
 */
public class WebEngineSwitchingManager {
	
	protected final static Logger log = Logger.getLogger(WebEngineSwitchingManager.class);
	/**
	 * Sets Do Not Show Browser Engine Dialog property of VPE
	 * Working only on Linux with GTK2 otherwise doesn't make sense
	 * @param enable
	 */
	private static void setDoNotShowBrowserEngineDialogProperty (boolean enable){
		if (RunningPlatform.isOperationSystem(OS.LINUX)){
			if (!VpePlatformUtil.isGTK3()){
				if (!Platform.getPreferencesService()
						.getBoolean("org.jboss.tools.jst.web.ui", "Remember visual editor engine", false, null)){
					WorkbenchPreferenceDialog preferencesDialog = new WorkbenchPreferenceDialog();
					preferencesDialog.open();
					VisualPageEditorPreferencePage visualPageEditorPreferencePage = new VisualPageEditorPreferencePage();
					preferencesDialog.select(visualPageEditorPreferencePage);
					visualPageEditorPreferencePage.setDoNotShowBrowserEngineDialog(true);
					preferencesDialog.ok();
				}
				else{
					log.debug("Do Not Show Browser Engine Dialog property is already set to true");
				}
			}
			else{
				log.debug("Do Not Show Browser Engine Dialog property not set because running system is using GTK3");
			}
		}
		else {
			log.debug("Do Not Show Browser Engine Dialog property not set because running system is not Linux");
		}		
	}
	/**
	 * Checks Do Not Show Browser Engine Dialog property of VPE
	 */
	public static void checkDoNotShowBrowserEngineDialogProperty (){
		setDoNotShowBrowserEngineDialogProperty(true);
	}
	/**
	 * Unchecks Do Not Show Browser Engine Dialog property of VPE
	 */
	public static void uncheckDoNotShowBrowserEngineDialogProperty (){
		setDoNotShowBrowserEngineDialogProperty(false);
	}
}
