/*******************************************************************************
 * Copyright (c) 2007-2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v 1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
import org.junit.After;
import org.junit.Test;

public class ID416CreateApplicationOnSourceCodeFromGithubTest {

	private String sourceCodeURL = "https://github.com/openshift/kitchensink-example";
	private String applicationName = "kitchensink" + System.currentTimeMillis();
	
	private boolean applicationCreated = false;
	
	@Test
	public void testCreateApplicationOnSourceCodeFromGithub() {
		NewOpenShift2ApplicationWizard wizard = new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME, 
				DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		wizard.openWizardFromExplorer();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.JBOSS_AS, 
				applicationName, false, true, false, false, sourceCodeURL, null, 
				true, null, null, null, (String[]) null);
		
		wizard.postCreateSteps(false);
		
		applicationCreated = true;
	
		OpenShiftExplorerView explorer = new OpenShiftExplorerView(); 
		
		explorer.reopen();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME).getDomain(DatastoreOS2.DOMAIN).
		getApplication(applicationName).select();
		 
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_BROWSER).select();
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(DatastoreOS2.USERNAME,
					DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName, "Welcome to JBoss AS 7!"),
					TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application from github source code has not been created successfully.");
		}
	}
	
	@After
	public void deleteApplication() {
		if (applicationCreated) {
			new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
					applicationName, "jboss-as-kitchensink").perform();
		}
	}
}
