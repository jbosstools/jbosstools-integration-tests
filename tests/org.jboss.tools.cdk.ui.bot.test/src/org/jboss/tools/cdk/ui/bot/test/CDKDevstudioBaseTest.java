/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdk.ui.bot.test;

import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.cdk.reddeer.preferences.OpenShift3SSLCertificatePreferencePage;
import org.jboss.tools.cdk.reddeer.ui.CDEServer;
import org.jboss.tools.cdk.reddeer.ui.CDEServersView;
import org.jboss.tools.cdk.reddeer.ui.wizard.NewServerContainerWizardPage;
import org.jboss.tools.docker.reddeer.ui.ConnectionItem;
import org.jboss.tools.docker.reddeer.ui.DockerExplorer;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift2Connection;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;


/**
 * Basic Devstudio and CDK integration test
 * @author odockal
 *
 */
public class CDKDevstudioBaseTest {

	private CDEServersView serversView;
	
	private CDEServer server;
	
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	private static final String SERVER_NAME = "Red Hat Container Development Kit";
	
	private static final String SERVER_ADAPTER = "Container Development Environment";
	
	private static final String SERVER_TYPE = "Red Hat JBoss Middleware";
	
	private static final String OPENSHIFT_USER_NAME = "openshift-dev";
	
	private static final String OPENSHIFT_SERVER = "https://10.1.2.2:8443";
	
	private static final String DOCKER_DAEMON_CONNECTION = SERVER_ADAPTER;
	
	private static final Logger log = Logger.getLogger(CDKDevstudioBaseTest.class);
	
	private static String USERNAME;
	
	private static String PASSWORD;
	
	// TODO: add validation of vagrant path
	private static String VAGRANTFILE_PATH;
	
	static {
		String username = System.getProperty("developers.username");
		if (!(username == null || username.equals("") || username.startsWith("${"))){
			USERNAME = username;
		} else {
			USERNAME = null;
		}
		String password = System.getProperty("developers.password");
		if (!(password == null || password.equals("") || password.startsWith("${"))){
			PASSWORD = password;
		} else {
			PASSWORD = null;
		}
		String vagrantfile = System.getProperty("vagrantfile.path");
		if (!(vagrantfile == null || vagrantfile.equals("") || vagrantfile.startsWith("${"))){
			VAGRANTFILE_PATH = vagrantfile;
		} else {
			VAGRANTFILE_PATH = null;
		}	
	}
	
	private static void checkCredentials() {
		if (USERNAME == null || PASSWORD== null) {
			throw new RedDeerException("Credentials for Red Hat Developers were not set properly");
		}
		log.info("Red Hat Developers username " + USERNAME + " and given password are set");
		if (VAGRANTFILE_PATH == null) {
			throw new RedDeerException("Path Vagrantfile path was not specified");
		}
		log.info("Vagrantfile path is set to " + VAGRANTFILE_PATH);
	}
	
	@BeforeClass
	public static void setUpEnvironemnt() {
		log.info("Checking given program arguments");
		checkCredentials();
		log.info("Adding new Container Development Environment server adapter");
		addNewCDEServer();
	}
	
	@AfterClass
	public static void tearDownEnvironment() {
		log.info("Deleting Container Development Environment server adapter");
		ServersView servers = new ServersView();
		servers.open();
		try {
			servers.getServer(SERVER_ADAPTER).delete(true);
		} catch (EclipseLayerException exc) {
			exc.printStackTrace();
		} 
	}
	
	@Before
	public void setUpServers() {
		log.info("Open Servers view tab");
		serversView = new CDEServersView();
		serversView.open();
		log.info("Getting server object from Servers View with name: " + SERVER_ADAPTER);
		server = (CDEServer)serversView.getServer(SERVER_ADAPTER);
		new WaitUntil(new JobIsRunning(), TimePeriod.SHORT, false);
	}
	
	@After
	public void tearDownServers() {
		
		if (server.getLabel().getState() == ServerState.STARTED) {
			server.stop();
		}
		// remove SSL Certificate to be added at next server start at method annotated with before
		deleteCertificates();
		server = null;
		serversView.close();
	}
	
	private void startServerAdapter() {
		log.info("Starting server adapter");
		try {
			server.start();
		} catch (ServersViewException e) {
			e.printStackTrace();
		}
		printCertificates();
		checkAvailableServers();
		assertEquals(ServerState.STARTED, server.getLabel().getState());
	}
	
	@Test
	public void testCDEStop() {
		startServerAdapter();
		server.stop();
		assertEquals(ServerState.STOPPED, server.getLabel().getState());
	}
	
	@Test
	public void testCDERestart() {
		startServerAdapter();
		server.restart();
		assertEquals(ServerState.STARTED, server.getLabel().getState());
	}
	
	// TODO: rewrite with using OpenShift3Connection object with parameters passed to program
	@Test
	public void testOpenShiftConnection() {
		startServerAdapter();
		OpenShiftExplorerView osExplorer = new OpenShiftExplorerView();
		osExplorer.open();
		try {
			OpenShift3Connection connection = osExplorer.getOpenShift3Connection(OPENSHIFT_SERVER, OPENSHIFT_USER_NAME);
			// usually, when server adapter is not started, openshift connection after refresh should cause 
			// problem occurs dialog
			connection.refresh();
			try {
				new WaitUntil(new ShellWithTextIsAvailable("Problem occurred"), TimePeriod.getCustom(30));
				fail("Problem dialog occured when refreshing OpenShift connection");
			} catch (WaitTimeoutExpiredException ex) {
				// no dialog appeared, which is ok
			}
			try {
				treeViewerHandler.getTreeItem(connection.getTreeItem(), "OpenShift sample project");
			} catch (JFaceLayerException ex) {
				fail("Could not find deployed sample OpenShift project");
			}
		} catch (RedDeerException ex) {
			fail("Could not open OpenShift connection for " + OPENSHIFT_USER_NAME +
					" ended with exception: " + ex.getMessage());
		}
	}
	
	@Test
	public void testDockerDaemonConnection() {
		startServerAdapter();
		DockerExplorer dockerExplorer = new DockerExplorer();
		dockerExplorer.open();
		ConnectionItem connection = null;
		try {
			connection = dockerExplorer.getConnection(DOCKER_DAEMON_CONNECTION);
		} catch (EclipseLayerException ex) {
			fail("Could not find Docker connection " + DOCKER_DAEMON_CONNECTION);
		}
		connection.select();
		dockerExplorer.enableConnection(DOCKER_DAEMON_CONNECTION);
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(30));
		try {
			connection.expand();
			assertTrue("Docker connection does not contain any other connections", connection.getChildrenConnection().size() > 0);
		} catch (WaitTimeoutExpiredException ex) {
			fail("WaitTimeoutExpiredException occurs when expanding"
					+ " Docker connection " + DOCKER_DAEMON_CONNECTION);
		}
 	}
	
	private static void addNewCDEServer() {
		ServersView view = new ServersView();
		view.open();
		NewServerWizardDialog dialog = view.newServer();
		NewServerWizardPage page = new NewServerWizardPage();
		
		page.selectType(SERVER_TYPE, SERVER_NAME);
		page.setHostName("localhost");
		dialog.next();
		
		NewServerContainerWizardPage containerPage = new NewServerContainerWizardPage();
		containerPage.setCredentials(USERNAME, PASSWORD);
		containerPage.setFolder(VAGRANTFILE_PATH);
		dialog.finish();
	}
	
	private void checkAvailableServers() {
		for (Server server : serversView.getServers()) {
			String serverName = server.getLabel().getName();
			log.info(serverName);
		}
		assertTrue(server.getLabel().getName().contains(SERVER_ADAPTER));
	}
	
	// TODO: Log active Console/Terminal-Console information
	private void logConsoleOutput() {
		ConsoleView console = new ConsoleView();
		console.open();
		log.info(console.getConsoleText());
	}
	
	private void printCertificates() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		OpenShift3SSLCertificatePreferencePage preferencePage = new OpenShift3SSLCertificatePreferencePage();
		dialog.select(preferencePage);
		preferencePage.printCertificates();
		dialog.ok();
	}
	
	private void deleteCertificates() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		OpenShift3SSLCertificatePreferencePage preferencePage = new OpenShift3SSLCertificatePreferencePage();
		dialog.select(preferencePage);
		preferencePage.deleteAll();
		preferencePage.apply();
		dialog.ok();		
	}

}
