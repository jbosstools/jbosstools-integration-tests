/******************************************************************************* 
 * Copyright (c) 2017 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdk.ui.bot.test.server.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewException;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.cdk.reddeer.preferences.OpenShift3SSLCertificatePreferencePage;
import org.jboss.tools.cdk.reddeer.server.ui.CDEServer;
import org.jboss.tools.cdk.reddeer.server.ui.CDEServersView;
import org.jboss.tools.cdk.reddeer.server.ui.wizard.NewCDK3ServerContainerWizardPage;
import org.jboss.tools.cdk.reddeer.server.ui.wizard.NewCDKServerContainerWizardPage;
import org.jboss.tools.cdk.ui.bot.test.CDKAbstractTest;
import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Abstract class working with CDK server adapter
 * @author odockal
 *
 */
public abstract class CDKServerAdapterAbstractTest extends CDKAbstractTest {

	protected ServersView serversView;
	
	protected Server server;
	
	private static final Logger log = Logger.getLogger(CDKServerAdapterAbstractTest.class);
	
	public Server getCDEServer() {
		return this.server;
	}

	protected ServersView getServersView() {
		return this.serversView;
	}

	protected void setServersView(ServersView view) {
		this.serversView = view;
	}

	protected void setCDEServer(Server server) {
		this.server = (CDEServer)server;
	}
	
	protected abstract String getServerAdapter();
	
	protected abstract boolean isCDK3();
	
	@BeforeClass
	public static void setUpEnvironemnt() {
		log.info("Checking given program arguments"); //$NON-NLS-1$
		checkDevelopersParameters();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
	}
	
	@Before
	public void setUpServers() {
		log.info("Open Servers view tab"); //$NON-NLS-1$
		setServersView(new CDEServersView(isCDK3()));
		getServersView().open();
		log.info("Getting server object from Servers View with name: " + getServerAdapter()); //$NON-NLS-1$
		setCDEServer(getServersView().getServer(getServerAdapter()));
		new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
	}
	
	@After
	public void tearDownServers() {
		if (getCDEServer().getLabel().getState() == ServerState.STARTED) {
			getCDEServer().stop();
		}
		// remove SSL Certificate to be added at next server start at method annotated with before
		deleteCertificates();
		setCDEServer(null);
		getServersView().close();
	}
	
	protected void startServerAdapter() {
		log.info("Starting server adapter"); //$NON-NLS-1$
		try {
			getCDEServer().start();
		} catch (ServersViewException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} 
		printCertificates();
		checkAvailableServers();
		assertEquals(ServerState.STARTED, getCDEServer().getLabel().getState());
	}
	
	protected void checkAvailableServers() {
		for (Server serverItem : getServersView().getServers()) {
			String serverName = serverItem.getLabel().getName();
			log.info(serverName);
		}
		assertTrue(getCDEServer().getLabel().getName().contains(getServerAdapter()));
	}
	
	protected static void printCertificates() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		OpenShift3SSLCertificatePreferencePage preferencePage = new OpenShift3SSLCertificatePreferencePage();
		dialog.select(preferencePage);
		preferencePage.printCertificates();
        dialog.ok();
	}
	
	protected static void deleteCertificates() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		OpenShift3SSLCertificatePreferencePage preferencePage = new OpenShift3SSLCertificatePreferencePage();
		dialog.select(preferencePage);
		preferencePage.deleteAll();
		preferencePage.apply();
        dialog.ok();
	}
	
	public static void addNewCDK3Server(String serverName, String serverAdapter, String hypervisor, String path) {
		NewServerWizardDialog dialog = CDKTestUtils.openNewServerWizardDialog();
		NewServerWizardPage page = new NewServerWizardPage();
		// set first dialog page
		page.selectType(SERVER_TYPE_GROUP, serverName);
		page.setHostName(SERVER_HOST);
		page.setName(serverAdapter);
		dialog.next();
		
		// set second new server dialog page
		NewCDK3ServerContainerWizardPage containerPage = new NewCDK3ServerContainerWizardPage();
		containerPage.setCredentials(USERNAME, PASSWORD);
		if (hypervisor != null && !hypervisor.isEmpty()) {
			log.info("Setting hypervisor"); //$NON-NLS-1$
			containerPage.setHypervisor(hypervisor);
		}
		log.info("Setting minishift binary file folder"); //$NON-NLS-1$
		containerPage.setMinishiftBinary(path);
		new WaitUntil(new WidgetIsEnabled(new FinishButton()), TimePeriod.NORMAL);
		log.info("Finishing Add new server dialog"); //$NON-NLS-1$
		if (!(new FinishButton().isEnabled())) {
			log.error("Finish button was not enabled"); //$NON-NLS-1$
		}
		dialog.finish();
	}
	
	public static void addNewCDKServer(String serverName, String serverAdapter, String path) {
		NewServerWizardDialog dialog = CDKTestUtils.openNewServerWizardDialog();
		NewServerWizardPage page = new NewServerWizardPage();
		// set first dialog page
		page.selectType(SERVER_TYPE_GROUP, serverName);
		page.setHostName(SERVER_HOST);
		page.setName(serverAdapter);
		dialog.next();
		
		// set second new server dialog page
		NewCDKServerContainerWizardPage containerPage = new NewCDKServerContainerWizardPage();
		containerPage.setCredentials(CDKServerAdapterAbstractTest.USERNAME, PASSWORD);
		// set cdk 2.x fields
		log.info("Setting vagrant file folder"); //$NON-NLS-1$
		containerPage.setFolder(path);
		new WaitUntil(new WidgetIsEnabled(new FinishButton()), TimePeriod.NORMAL);
		log.info("Finishing Add new server dialog"); //$NON-NLS-1$
		if (!(new FinishButton().isEnabled())) {
			log.error("Finish button was not enabled"); //$NON-NLS-1$
		}
		dialog.finish();
	}
	
}
