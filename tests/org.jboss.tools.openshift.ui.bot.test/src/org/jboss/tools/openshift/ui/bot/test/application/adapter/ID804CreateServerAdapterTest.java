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
package org.jboss.tools.openshift.ui.bot.test.application.adapter;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsKilled;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter;
import org.jboss.tools.openshift.reddeer.view.resources.ServerAdapter.Version;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test capabilities of creating a server adapter via servers view and OpenShiftExplorer view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID804CreateServerAdapterTest {

	private static String applicationName = "diy" + System.currentTimeMillis();
	
	@BeforeClass
	public static void createApplication() {
		new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, false).
			createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, false);
	}
	
	@Test
	public void createAdapterViaServersView() {
		ServersView servers = new ServersView();
		servers.open();
		
		NewServerWizardDialog dialog = new NewServerWizardDialog();
		NewServerWizardPage page = new NewServerWizardPage();
		
		dialog.open();
		// Wait till tree is generated
		AbstractWait.sleep(TimePeriod.getCustom(5));
		page.selectType(OpenShiftLabel.Others.OS2_SERVER_ADAPTER);
		dialog.next();
		
		
		for (String connection: new LabeledCombo("Connection:").getItems()) {
			if (connection.contains(DatastoreOS2.USERNAME)) {
				new LabeledCombo("Connection:").setSelection(connection);
				break;
			}
		}
		
		new LabeledCombo("Domain Name:").setSelection(DatastoreOS2.DOMAIN);
		new LabeledCombo("Application Name:").setSelection(applicationName);
		new LabeledCombo("Deploy Project:").setSelection(applicationName);
		
		new FinishButton().click();
		
		// Kill refreshing server adapter list job
		new WaitUntil(new JobIsKilled("Refreshing server adapter list"), TimePeriod.LONG, false);
				
		new WaitWhile(new ShellWithTextIsAvailable("New Server"), TimePeriod.NORMAL);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	
		try {
			ServerAdapter serverAdapter = new ServerAdapter(Version.OPENSHIFT2, applicationName);
			serverAdapter.delete();
		} catch (RedDeerException ex) {
			fail("Server adapter has not been created.");
		}
	}
	
	@Test
	public void createAdapterViaExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).getDomain(DatastoreOS2.DOMAIN).
			getApplication(applicationName).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_SERVER_ADAPTER).select();
		
		new WaitUntil(new ShellWithTextIsAvailable("New Server"), TimePeriod.LONG);

		new DefaultShell("New Server");
		
		new NextButton().click();
		
		new WaitUntil(new WidgetIsEnabled(new FinishButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		// Kill refreshing server adapter list job
		new WaitUntil(new JobIsKilled("Refreshing server adapter list"), TimePeriod.LONG, false);
		
		new WaitWhile(new ShellWithTextIsAvailable("New Server"), TimePeriod.LONG);
		
		ServersView servers = new ServersView();
		servers.open();
		
		try {
			ServerAdapter serverAdapter = new ServerAdapter(Version.OPENSHIFT2, applicationName);
			serverAdapter.delete();
		} catch (RedDeerException ex) {
			fail("Server adapter has not been created successfully.");
		}
	}
	
	@AfterClass
	public static void deleteApplication() {
		DeleteUtils deleteApplication = 
				new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
						applicationName, applicationName);
		deleteApplication.deleteProject();
		deleteApplication.deleteOpenShiftApplication();
	}
}
