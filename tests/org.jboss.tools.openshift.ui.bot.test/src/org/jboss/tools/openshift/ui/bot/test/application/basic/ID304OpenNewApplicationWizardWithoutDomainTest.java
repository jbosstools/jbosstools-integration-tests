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
import org.jboss.reddeer.common.wait.WaitWhile;
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
import org.jboss.tools.openshift.ui.bot.test.domain.ID203DeleteDomainTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test capabilities of opening new application wizard without domain on the connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID304OpenNewApplicationWizardWithoutDomainTest {
	
	@BeforeClass
	public static void deleteDomain() {
		ID203DeleteDomainTest.deleteDomain(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		ID203DeleteDomainTest.deleteDomain(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.SECOND_DOMAIN);
		
		new OpenShiftExplorerView().getOpenShift2Connection(DatastoreOS2.USERNAME).refresh();
	}
	
	@Test
	public void testOpenNewApplicationWizardViaExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_OS2_APPLICATION).select();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
			new CancelButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
		} catch (RedDeerException ex) {
			fail("Create domain shell has not been opened.");
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
			
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
			new CancelButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
		} catch (RedDeerException ex) {
			fail("Create domain shell has not been opened.");
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
			
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
			new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
			new CancelButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN),
					TimePeriod.LONG);
		} catch (RedDeerException ex) {
			fail("Create domain shell has not been opened.");
		}
	}
	
	@AfterClass
	public static void addDomain() {
		ID203DeleteDomainTest.createDomain(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		ID203DeleteDomainTest.createDomain(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.SECOND_DOMAIN);
	}
}
