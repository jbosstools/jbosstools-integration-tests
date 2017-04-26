/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.aerogear.reddeer.feedhenry.ui.cordova.wizards;

import org.jboss.reddeer.eclipse.topmenu.ImportMenuWizard;

/**
 * Reddeer implementation for Import Cordova Application wizard.
 * @author Pavol Srna
 *
 */
public class CordovaImportWizard extends ImportMenuWizard {

	/**
	 * Constructs the wizard with FeedHenry > Import Cordova Application
	 */
	public CordovaImportWizard() {
		super("Import", "FeedHenry", "Import Cordova Application");
	}
}
