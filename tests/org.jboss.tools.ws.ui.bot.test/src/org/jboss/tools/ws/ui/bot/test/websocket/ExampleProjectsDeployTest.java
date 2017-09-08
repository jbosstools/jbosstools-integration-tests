/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.ws.ui.bot.test.websocket;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.browser.BrowserEditor;
import org.eclipse.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.eclipse.reddeer.swt.impl.browser.InternalBrowser;
import org.eclipse.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.eclipse.reddeer.workbench.impl.editor.DefaultEditor;
import org.eclipse.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.central.reddeer.api.JavaScriptHelper;
import org.jboss.tools.central.reddeer.wait.CentralIsLoaded;
import org.jboss.tools.central.reddeer.wizards.NewProjectExamplesWizardDialogCentral;
import org.jboss.tools.ws.ui.bot.test.WSTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ServersViewHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.jboss.tools.ws.ui.bot.test.websocket.ExampleProjectsDeployTest.Constants.*;

/**
 * Test copies projects from central CI and deploys them.
 * 
 * @author Jan Novak
 */
@AutoBuilding(false)
public class ExampleProjectsDeployTest extends WSTestBase {

	private static JavaScriptHelper jsHelper = JavaScriptHelper.getInstance();

	@Before
	public void setup() {
		//activate central editor
		new DefaultToolItem(new WorkbenchShell(), RH_CENTRAL).click();
		new DefaultEditor(RH_CENTRAL);
		new WaitUntil(new CentralIsLoaded(), TimePeriod.VERY_LONG);
		jsHelper.setBrowser(new InternalBrowser());

		jsHelper.clearSearch();
	}

	@After
	public void tearDown() {
		deleteAllProjects();
	}

	@Test
	public void helloProjectTest() {
		setWsProjectName(PROJECT_HELLO_NAME);
		cloneProjectFromRHCentral();
		cleanAllProjects();

		ServersViewHelper.runProjectOnServer("jboss-" + getWsProjectName());
		ServersViewHelper.waitForDeployment("jboss-" + getWsProjectName(), getConfiguredServerName());

		BrowserEditor browserEditor = new BrowserEditor("WebSocket: Say Hello");
		Assert.assertTrue(browserEditor.getText().contains("This is a simple example of a WebSocket implementation."));
	}

	@Test
	public void endpointProjectTest() {
		setWsProjectName(PROJECT_ENDPOINT_NAME);
		cloneProjectFromRHCentral();
		cleanAllProjects();

		ServersViewHelper.runProjectOnServer("jboss-" + getWsProjectName());
		ServersViewHelper.waitForDeployment("jboss-" + getWsProjectName(), getConfiguredServerName());

		BrowserEditor browserEditor = new BrowserEditor("Bidding");
		Assert.assertTrue(browserEditor.getText().contains("resources/gfx/redfedora1.jpg"));
	}

	private void cloneProjectFromRHCentral() {
		jsHelper.searchFor(getWsProjectName());
		jsHelper.clickExample(getWsProjectName());
		NewProjectExamplesWizardDialogCentral wizardDialog = new NewProjectExamplesWizardDialogCentral();
		wizardDialog.finish(getWsProjectName());
	}


	static class Constants {
		static final String RH_CENTRAL = "Red Hat Central";

		static final String PROJECT_HELLO_NAME = "websocket-hello";
		static final String PROJECT_ENDPOINT_NAME = "websocket-endpoint";
	}
}
