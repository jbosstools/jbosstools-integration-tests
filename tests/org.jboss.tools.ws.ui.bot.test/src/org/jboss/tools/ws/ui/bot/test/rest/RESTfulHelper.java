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

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.swt.api.Menu;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
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
		JAXRSValidatorPreferencePage page = new JAXRSValidatorPreferencePage();
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

		Menu menu = new ContextMenu("Configure",
				enableRestSupport ? RESTfulLabel.ADD_REST_SUPPORT : RESTfulLabel.REMOVE_REST_SUPPORT);
		menu.select();

		new WaitUntil(new JobIsRunning(), TimePeriod.DEFAULT, false);
	}
}
