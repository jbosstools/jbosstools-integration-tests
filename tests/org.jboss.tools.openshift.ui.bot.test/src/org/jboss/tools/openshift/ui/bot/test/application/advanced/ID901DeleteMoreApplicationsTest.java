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
package org.jboss.tools.openshift.ui.bot.test.application.advanced;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.openshift.reddeer.condition.v2.OpenShiftApplicationExists;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift2Application;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of deleting more applications at once.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID901DeleteMoreApplicationsTest {

	private String firstApplicationName = "fdiy" + System.currentTimeMillis();
	private String secondApplicationName = "sdiy" + System.currentTimeMillis();
	
	private boolean firstApplicationExists = false;
	private boolean secondApplicationExists = false;
	
	@Before
	public void createApplications() {
		new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, false).
			createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, firstApplicationName, false, true, true);
		new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, false).
			createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, secondApplicationName, false, true, true);
	}
	
	@Test
	public void testDeleteMoreApplicationsAtOnce() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift2Application firstApplication = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
				getDomain(DatastoreOS2.DOMAIN).getApplication(firstApplicationName);
		firstApplicationExists = true;
		OpenShift2Application secondApplication = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
				getDomain(DatastoreOS2.DOMAIN).getApplication(secondApplicationName);
		secondApplicationExists = true;
		
		new DefaultTree().selectItems(firstApplication.getTreeItem(), secondApplication.getTreeItem());
		
		try {
			new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_APPLICATION).select();
		} catch (SWTLayerException ex) {
			fail("There is no context menu item to delete more application at once.");
		}
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_APP), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Shell for confirmation of application removal has not been opened.");
		}
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_APP);	
		new OkButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		assertApplicationDoesNotExists(firstApplicationName);
		assertApplicationDoesNotExists(secondApplicationName);
	}
	
	private void assertApplicationDoesNotExists(String applicationName) {
		if (new OpenShiftApplicationExists(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, 
					DatastoreOS2.DOMAIN, applicationName).test()) {
			fail("Application " + applicationName + " is still presented in OpenShift explorer.");
		}
	}
	
	@After
	public void deleteAdaptersAndProjects() {
		DeleteUtils deleteFirst = new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
				firstApplicationName, firstApplicationName);
		DeleteUtils deleteSecond = new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
				secondApplicationName, secondApplicationName);
		
		if (firstApplicationExists) {
			deleteFirst.deleteProject();
			deleteFirst.deleteServerAdapter();
		}
		
		if (secondApplicationExists) {
			deleteSecond.deleteProject();
			deleteSecond.deleteServerAdapter();
		}
	}
}
