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
package org.jboss.tools.openshift.ui.bot.test.application.basic;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.ssh.ID151RemoveSSHKeyTest;
import org.jboss.tools.openshift.ui.bot.test.ssh.ID152AddExistingSSHKeyTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test capabilities of opening a new application wizard without having uploaded 
 * public SSH key.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID303OpenNewApplicationWizardWithoutSSHKeyTest {

	@BeforeClass
	public static void removeSSHKey() {
		ID151RemoveSSHKeyTest.removeSSHKey();
	}
	
	@Test
	public void testOpenNewApplicationWizardViaExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
			getDomain(DatastoreOS2.DOMAIN).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_OS2_APPLICATION).select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NO_SSH_KEY),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.NO_SSH_KEY);
			new CancelButton().click();
		} catch (RedDeerException ex) {
			fail("No SSH key shell has not been opened.");
		}
	}
	
	@Test
	public void testOpenNewApplicationWizardViaMenu() {
		new ShellMenu(OpenShiftLabel.Others.NEW_APP_MENU).select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
			
			new NextButton().click();
			
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NO_SSH_KEY),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.NO_SSH_KEY);
			new CancelButton().click();
		} catch (RedDeerException ex) {
			fail("No SSH key shell has not been opened.");
		}
	}
	
	@Test
	public void testOpenNewApplicationWizardViaCentral() {
		new DefaultToolItem(new WorkbenchShell(), OpenShiftLabel.Others.RED_HAT_CENTRAL).click();
		
		new InternalBrowser().execute(OpenShiftLabel.Others.OPENSHIFT_CENTRAL_SCRIPT);

		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
			
			new NextButton().click();
			
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NO_SSH_KEY),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.NO_SSH_KEY);
			new CancelButton().click();
		} catch (RedDeerException ex) {
			fail("No SSH key shell has not been opened.");
		}
	}
	
	@AfterClass
	public static void addSSHKey() {
		ID152AddExistingSSHKeyTest.addExistingSSHKey(DatastoreOS2.USERNAME, DatastoreOS2.SERVER);
	}
	
}
