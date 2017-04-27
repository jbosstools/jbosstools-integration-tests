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
package org.jboss.tools.openshift.ui.bot.test.application.importing;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.jboss.reddeer.swt.condition.ControlIsEnabled;
import org.jboss.reddeer.swt.condition.ShellIsAvailable;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.core.condition.JobIsKilled;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of importing an application via OpenShift server adapter.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID503ImportApplicationViaAdapterTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Before
	public void createApplicationDeleteProject() {
		ID501ImportApplicationViaExplorerTest.createApplicationWithoutServerAdapterAndProject(applicationName);
	}
	
	@Test
	public void testImportApplicationViaServerAdapter() {
		ServersView2 servers = new ServersView2();
		servers.open();
		
		try {
			new DefaultTree();
			new ContextMenu("New", "Server").select();
		} catch (CoreLayerException ex) {
			// There is no server, so create server via link
			new DefaultLink("No servers are available. Click this link to create a new server...").click();
		}
		
		new DefaultShell("New Server");
		
		AbstractWait.sleep(TimePeriod.getCustom(5));
		new DefaultTreeItem(OpenShiftLabel.Others.OS2_SERVER_ADAPTER).select();
		
		new WaitUntil(new ControlIsEnabled(new NextButton()), TimePeriod.LONG);
		
		new NextButton().click();
		
		for (String connection: new LabeledCombo("Connection:").getItems()) {
			if (connection.contains(DatastoreOS2.USERNAME)) {
				new LabeledCombo("Connection:").setSelection(connection);
				break;
			}
		}
		
		new LabeledCombo("Domain Name:").setSelection(DatastoreOS2.DOMAIN);
		
		new WaitUntil(new ControlIsEnabled(new CancelButton()));
		
		new DefaultLink("Import this application").click();
		
		new WaitUntil(new ShellIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		
		new FinishButton().click();
		
		// Kill refreshing server adapter list job
		new WaitUntil(new JobIsKilled("Refreshing server adapter list"), TimePeriod.LONG, false);
		
		new WaitWhile(new ShellIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertTrue("There is no project in project explorer. Importing was no successfull.",
				new ProjectExplorer().containsProject(applicationName));
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName,
				applicationName).perform();
	}
}
