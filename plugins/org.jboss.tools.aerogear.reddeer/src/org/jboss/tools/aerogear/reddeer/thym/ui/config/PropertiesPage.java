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
package org.jboss.tools.aerogear.reddeer.thym.ui.config;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.aerogear.reddeer.thym.ui.wizard.project.CordovaPluginWizard;

public class PropertiesPage {
	
	
	public CordovaPluginWizard addPlugin() {
		new PushButton("Add...").click();
		CordovaPluginWizard wizard =  new CordovaPluginWizard();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		return wizard;
	}

}
