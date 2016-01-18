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

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.condition.v2.OpenShiftApplicationExists;
import org.jboss.tools.openshift.reddeer.exception.OpenShiftToolsException;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.ServerAdapter;
import org.jboss.tools.openshift.reddeer.view.ServerAdapter.Version;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID903ApplicationMarkersTest;
import org.jboss.tools.openshift.ui.bot.test.application.advanced.ID905ManageSnapshotsTest;
import org.jboss.tools.openshift.ui.bot.test.application.cartridge.ID601EmbedCartridgeTest;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID408ApplicationPropertiesTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID705TailFilesTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID706PortForwardingTest;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID707HandleEnvironmentVariablesTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test capabilities of server adapter handling basic and advanced operations.
 * Similar as on application.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID803ServerAdapterHandlingTest {

	private static String applicationName = "eap" + System.currentTimeMillis();
	
	@BeforeClass
	public static void createApplication() {
		new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, false).
				createSimpleApplicationOnBasicCartridges(OpenShiftLabel.Cartridge.JBOSS_EAP, applicationName,
						false, true, true);
	}
	
	@Test
	public void testServerAdapterShowInBrowser() {
		new ServerAdapter(Version.OPENSHIFT2, applicationName).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_BROWSER).select();
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(DatastoreOS2.USERNAME, 
					DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName, "OpenShift"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been shown successfully in browser.");
		}
		
	}
	
	@Test
	public void testServerAdapterTailingFiles() {
		ID705TailFilesTest.tailFilesTest(new ServersView(), new ServerAdapter(Version.OPENSHIFT2, 
				applicationName).getTreeItem(), false, "OpenShift", OpenShiftLabel.ContextMenu.TAIL_FILES);
	}
	
	@Ignore // failing due to JBIDE-18147
	@Test
	public void testServerAdapterPortForwarding() {
		ID706PortForwardingTest.portForwardingTest(new ServersView(), new ServerAdapter(Version.OPENSHIFT2, 
				applicationName).getTreeItem(), "OpenShift", OpenShiftLabel.ContextMenu.PORT_FORWARD);
	}
	
	@Test
	public void testServerAdapterEnvironmentVariablesManaging() {
		ID707HandleEnvironmentVariablesTest.environmentVariablesHandling(new ServersView(), 
				new ServerAdapter(Version.OPENSHIFT2, applicationName).getTreeItem(), 
				OpenShiftLabel.Shell.MANAGE_ENV_VARS + applicationName,
				new String[] {"OpenShift", OpenShiftLabel.ContextMenu.EDIT_ENV_VARS},
				new String[] {"OpenShift", OpenShiftLabel.ContextMenu.SHOW_ENV_VARS});	
	}
	
	@Test
	public void testServerAdapterRestartApplication() {
		new ServerAdapter(Version.OPENSHIFT2, applicationName).select();
		
		new ContextMenu("OpenShift", OpenShiftLabel.ContextMenu.RESTART_APPLICATION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESTART_APPLICATION),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.RESTART_APPLICATION);
		new YesButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESTART_APPLICATION),
				TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
					DatastoreOS2.DOMAIN, applicationName, "OpenShift"), TimePeriod.VERY_LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been restarted successfully.");
		}
	}
	
	@Test
	public void testServerAdapterEmbedCartridge() {
		ID601EmbedCartridgeTest.embedCartridge(new ServersView(), new ServerAdapter(Version.OPENSHIFT2, 
				applicationName).getTreeItem(), applicationName, "OpenShift",
				OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE);
	}
	
	@Test
	public void testApplicationDetails() {
		ID408ApplicationPropertiesTest.applicationDetails(new ServersView(), new ServerAdapter(Version.OPENSHIFT2, 
				applicationName).getTreeItem(), applicationName, 
				"JBoss Enterprise Application Platform 6 (jbosseap-6)", "OpenShift", 
				OpenShiftLabel.ContextMenu.APPLICATION_PROPERTIES);
	}
	
	@Test
	public void testApplicationMarkers() {
		ID903ApplicationMarkersTest.markersTest(new ServersView(), new ServerAdapter(Version.OPENSHIFT2, 
				applicationName).getTreeItem(), applicationName, 
				OpenShiftLabel.ContextMenu.CONFIGURE_MARKERS);
	}
	
	@Test
	public void testManageSnapshots() {
		String[] contextMenuPath = new String[] {"OpenShift", OpenShiftLabel.ContextMenu.SAVE_SNAPSHOT[0],
				OpenShiftLabel.ContextMenu.SAVE_SNAPSHOT[1]};
		ID905ManageSnapshotsTest.manageSnapshotsTest(new ServersView(), new ServerAdapter(Version.OPENSHIFT2, 
				applicationName).getTreeItem(), applicationName, contextMenuPath);
	}
	
	@AfterClass
	public static void deleteProject() {
		deleteApplicationViaServerAdapter();
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, applicationName, applicationName).
			deleteProject();
	}
	
	private static void deleteApplicationViaServerAdapter() {
		new ServerAdapter(Version.OPENSHIFT2, applicationName).select();
		new ContextMenu("OpenShift", OpenShiftLabel.ContextMenu.DELETE_APPLICATION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_SERVER_REMOVE),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.APPLICATION_SERVER_REMOVE);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.APPLICATION_SERVER_REMOVE),
				TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			new WaitWhile(new OpenShiftApplicationExists(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN,
				applicationName), TimePeriod.NORMAL);
			fail("Application should be removed and no longer visible in OpenShift explorer.");
		} catch (WaitTimeoutExpiredException ex) {
			// PASS
		}
		
		try {
			new ServerAdapter(Version.OPENSHIFT2, applicationName);
			fail("Server adapter should not longer be presented in servers view.");
		} catch (OpenShiftToolsException ex) {
			// PASS
		}
	}
}
