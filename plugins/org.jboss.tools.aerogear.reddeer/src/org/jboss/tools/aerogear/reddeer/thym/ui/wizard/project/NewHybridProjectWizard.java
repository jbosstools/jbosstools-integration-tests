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
package org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;


/**
 * Reddeer implementation for Hybrid Mobile (Cordova) Application Project wizard.
 * @author Pavol Srna
 *
 */
public class NewHybridProjectWizard extends NewMenuWizard {

	/**
	 * Constructs the wizard with Mobile > Hybrid Mobile (Cordova) Application Project
	 */
	public NewHybridProjectWizard() {
		super("Hybrid Mobile (Cordova) Application Project", "Mobile", "Hybrid Mobile (Cordova) Application Project");
	}
	
	public void finish(){
		this.finish(TimePeriod.LONG);
	}

}
