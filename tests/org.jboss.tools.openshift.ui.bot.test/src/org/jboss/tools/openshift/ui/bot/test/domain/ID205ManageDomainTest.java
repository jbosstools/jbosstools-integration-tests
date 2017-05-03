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
package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.openshift.reddeer.condition.TableIsEnabled;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test domains managment via Manage domains shell.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID205ManageDomainTest {

	@Test
	public void testManageDomains() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.MANAGE_DOMAINS).select();;	
		
		testCreateNewDomain();
		
		testEditDomain();
		
		testDeleteDomain();
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_DOMAINS);
		
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.MANAGE_DOMAINS), TimePeriod.LONG);
	}
	
	private void testCreateNewDomain() {
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_DOMAINS);	
		
		int domainsCount = new DefaultTable().getItems().size();
		
		new PushButton(OpenShiftLabel.Button.CREATE_DOMAIN).click();
		
		new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
		
		new LabeledText(OpenShiftLabel.TextLabels.DOMAIN_NAME).setText(DatastoreOS2.X_DOMAIN);
	
		new WaitUntil(new ControlIsEnabled(new FinishButton()));
		
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN), TimePeriod.LONG);
		
		assertTrue("New domain has not been created successfully.",
				domainsCount + 1 == new DefaultTable().getItems().size());
	}
	
	private void testEditDomain() {
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_DOMAINS);	
		
		new DefaultTable().getItem(DatastoreOS2.X_DOMAIN).select();
		
		new PushButton(OpenShiftLabel.Button.EDIT_DOMAIN).click();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_DOMAIN);
		
		DatastoreOS2.X_DOMAIN = "xqetestjbds" + new Random().nextInt(100);
		
		new LabeledText(OpenShiftLabel.TextLabels.DOMAIN_NAME).setText(DatastoreOS2.X_DOMAIN);
		
		new WaitUntil(new ControlIsEnabled(new FinishButton()));
		
		new FinishButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.EDIT_DOMAIN), 
				TimePeriod.LONG);
		
		try {
			new DefaultShell(OpenShiftLabel.Shell.MANAGE_DOMAINS);	
			new DefaultTable().getItem(DatastoreOS2.X_DOMAIN);
			// pass
		} catch (SWTLayerException ex) {
			fail("Domain does not exists in table.");
		}
	}
	
	private void testDeleteDomain() {
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_DOMAINS);	
		
		new DefaultTable().getItem(DatastoreOS2.X_DOMAIN).select();
		
		new WaitUntil(new ControlIsEnabled(new PushButton(OpenShiftLabel.Button.REMOVE)));
		
		new PushButton(OpenShiftLabel.Button.REMOVE).click();
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_DOMAIN);
		
		new CheckBox(0).click();
		new OkButton().click();
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.DELETE_DOMAIN),
				TimePeriod.LONG);
		
		new WaitUntil(new TableIsEnabled(new DefaultTable()), TimePeriod.LONG);
		
		new WaitWhile(new JobIsRunning());
		
		try {
			new DefaultTable().getItem(DatastoreOS2.X_DOMAIN);
			fail("Domain should not exists in table.");
		} catch (CoreLayerException ex) {
			// PASS
		}
	}
}
