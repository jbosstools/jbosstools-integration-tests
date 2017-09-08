/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.swt.api.Menu;
import org.eclipse.reddeer.swt.api.MenuItem;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.menu.ContextMenuItem;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.ws.reddeer.jaxrs.core.RESTfulLabel;
import org.jboss.tools.ws.reddeer.ui.preferences.JAXRSValidatorPreferencePage;

public class RESTfulHelper {

	public void enableRESTValidation() {
		modifyRESTValidation(true);
	}

	public void disableRESTValidation() {
		modifyRESTValidation(false);
	}

	public void modifyRESTValidation(boolean enableRestSupport) {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		JAXRSValidatorPreferencePage page = new JAXRSValidatorPreferencePage(dialog);
		dialog.select(page);

		page.setEnableValidation(enableRestSupport);
		
		page.apply();
		
		new WaitUntil(new ShellIsAvailable("Validator Settings Changed"), TimePeriod.SHORT, false);
		if(new ShellIsAvailable("Validator Settings Changed").test()) {
			new DefaultShell("Validator Settings Changed");
			new PushButton("Yes").click();
			new WaitWhile(new ShellIsAvailable("Validator Settings Changed"), TimePeriod.DEFAULT);
		}

		dialog.ok();
		
		new WaitUntil(new JobIsRunning(), TimePeriod.DEFAULT, false);
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(20), false);
	}

	public void addRestSupport(String wsProjectName) {
		configureRestSupport(wsProjectName, true);
	}

	public void removeRestSupport(String wsProjectName) {
		configureRestSupport(wsProjectName, false);
	}

	public boolean isRestSupportEnabled(String wsProjectName) {
		Project project = new ProjectExplorer().getProject(wsProjectName);
		return project.containsResource(RESTfulLabel.REST_WS_NODE)
				|| project.containsResource(RESTfulLabel.REST_WS_BUILD_NODE);
	}

	private void configureRestSupport(String wsProjectName,
			boolean enableRestSupport) {
		new ProjectExplorer().getProject(wsProjectName).select();

		MenuItem menu = new ContextMenuItem("Configure",
				enableRestSupport ? RESTfulLabel.ADD_REST_SUPPORT : RESTfulLabel.REMOVE_REST_SUPPORT);
		menu.select();

		new WaitUntil(new JobIsRunning(), TimePeriod.DEFAULT, false);
	}
}
