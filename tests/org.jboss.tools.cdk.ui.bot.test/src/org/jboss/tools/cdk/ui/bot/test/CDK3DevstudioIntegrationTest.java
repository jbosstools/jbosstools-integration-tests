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
package org.jboss.tools.cdk.ui.bot.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.tools.cdk.reddeer.requirements.DisableSecureStorageRequirement.DisableSecureStorage;
import org.jboss.tools.cdk.reddeer.ui.CDEServer;
import org.jboss.tools.cdk.reddeer.ui.wizard.NewCDK3ServerContainerWizardPage;
import org.jboss.tools.docker.reddeer.ui.DockerExplorerView;
import org.jboss.tools.docker.reddeer.ui.resources.DockerConnection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Testing CDK3 server adapter with minishift using vm driver passed via system property
 * @author odockal
 *
 */
@RunWith(RedDeerSuite.class)
@DisableSecureStorage
public class CDK3DevstudioIntegrationTest extends CDKDevstudioAbstractTest {

	private static final String SERVER_NAME = "Red Hat Container Development Kit 3 (Tech Preview)"; //$NON-NLS-1$
	
	private static final String SERVER_ADAPTER = "Container Development Environment 3"; //$NON-NLS-1$
	
	private static final String SERVER_TYPE = "Red Hat JBoss Middleware"; //$NON-NLS-1$
	
	private static final String SERVER_HOST = "localhost"; //$NON-NLS-1$
	
	private static final String OPENSHIFT_USER_NAME = "developer"; //$NON-NLS-1$
	
	private static final String OPENSHIFT_PROJECT_NAME = "My Project"; //$NON-NLS-1$
	
	private static final String DOCKER_DAEMON_CONNECTION = SERVER_ADAPTER;
	
	private static final String MINISHIFT_HYPERVISOR;
	
	private static final String MINISHIFT_PATH;
	
	private static Logger log = Logger.getLogger(CDK3DevstudioIntegrationTest.class);
	
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	static {
		MINISHIFT_PATH = getSystemProperty("minishift.path"); //$NON-NLS-1$
		MINISHIFT_HYPERVISOR = getSystemProperty("hypervisor"); //$NON-NLS-1$
	}
	
	private static void checkMinishiftParams() {
		if (MINISHIFT_PATH == null) {
			throw new RedDeerException("Minishift binary path was not specified"); //$NON-NLS-1$
		}
		log.info("Minishift binary file is " + MINISHIFT_PATH); //$NON-NLS-1$		
		if (MINISHIFT_HYPERVISOR == null) {
			log.info("Default hypervisor will be set"); //$NON-NLS-1$	
		} else {
			log.info(MINISHIFT_HYPERVISOR + " is set as hypervisor"); //$NON-NLS-1$		
		}
	}

	@Override
	protected Server getCDEServer() {
		return this.server;
	}

	@Override
	protected ServersView getServersView() {
		return this.serversView;
	}

	@Override
	protected void setServersView(ServersView view) {
		this.serversView = view;
	}

	@Override
	protected void setCDEServer(Server server) {
		this.server = (CDEServer)server;
		
	}

	@Override
	protected String getServerAdapter() {
		return SERVER_ADAPTER;
	}
	
	@BeforeClass
	public static void setup() {
		checkMinishiftParams();
		log.info("Adding new Container Development Environment server adapter"); //$NON-NLS-1$
		addNewCDEServer();
	}
	
	private static void addNewCDEServer() {
		// call new server dialog from servers view
		ServersView view = new ServersView();
		view.open();
		NewServerWizardDialog dialog = view.newServer();
		NewServerWizardPage page = new NewServerWizardPage();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL, false);
		// set first dialog page
		page.selectType(SERVER_TYPE, SERVER_NAME);
		page.setHostName(SERVER_HOST);
		page.setName(SERVER_ADAPTER);
		dialog.next();
		
		// set second new server dialog page
		NewCDK3ServerContainerWizardPage containerPage = new NewCDK3ServerContainerWizardPage();
		log.info("Setting credentials"); //$NON-NLS-1$
		containerPage.setCredentials(USERNAME, PASSWORD);
		if (MINISHIFT_HYPERVISOR != null && !MINISHIFT_HYPERVISOR.isEmpty()) {
			log.info("Setting hypervisor"); //$NON-NLS-1$
			containerPage.setHypevisor(MINISHIFT_HYPERVISOR);
		}
		log.info("Setting minishift binary file folder"); //$NON-NLS-1$
		containerPage.setMinishiftBinary(MINISHIFT_PATH);
		new WaitUntil(new WidgetIsEnabled(new FinishButton()), TimePeriod.NORMAL);
		log.info("Finishing Add new server dialog"); //$NON-NLS-1$
		if (!(new FinishButton().isEnabled())) {
			log.error("Finish button was not enabled"); //$NON-NLS-1$
		}
		dialog.finish();
	}
	
	@AfterClass
	public static void tearDownEnvironment() {
		log.info("Deleting Container Development Environment server adapter"); //$NON-NLS-1$
		ServersView servers = new ServersView();
		servers.open();
		try {
			servers.getServer(SERVER_ADAPTER).delete(true);
		} catch (EclipseLayerException exc) {
			log.error(exc.getMessage());
			exc.printStackTrace();
		} 
		removeAccessRedHatCredentials();
	}
	
	@Test
	public void testCDEStop() {
		startServerAdapter();
		getCDEServer().stop();
		assertEquals(ServerState.STOPPED, getCDEServer().getLabel().getState());
	}
	
	@Test
	public void testCDERestart() {
		startServerAdapter();
		getCDEServer().restart();
		assertEquals(ServerState.STARTED, getCDEServer().getLabel().getState());
	}
	
	@Test
	public void testOpenShiftConnection() {
		startServerAdapter();
		OpenShiftExplorerView osExplorer = new OpenShiftExplorerView();
		osExplorer.open();
		try {
			OpenShift3Connection connection = osExplorer.getOpenShift3Connection(null, OPENSHIFT_USER_NAME);
			// usually, when server adapter is not started, openshift connection after refresh should cause 
			// problem occurs dialog
			connection.refresh();
			try {
				new WaitUntil(new ShellWithTextIsAvailable("Problem occurred"), TimePeriod.getCustom(30)); //$NON-NLS-1$
				fail("Problem dialog occured when refreshing OpenShift connection"); //$NON-NLS-1$
			} catch (WaitTimeoutExpiredException ex) {
				// no dialog appeared, which is ok
				log.debug("Expected WaitTimeoutExpiredException occured"); //$NON-NLS-1$
				ex.printStackTrace();
			}
			try {
				this.treeViewerHandler.getTreeItem(connection.getTreeItem(), OPENSHIFT_PROJECT_NAME);
			} catch (JFaceLayerException ex) {
				ex.printStackTrace();
				fail("Could not find deployed sample OpenShift project"); //$NON-NLS-1$
			}
		} catch (RedDeerException ex) {
			ex.printStackTrace();
			fail("Could not open OpenShift connection for " + OPENSHIFT_USER_NAME + //$NON-NLS-1$
					" ended with exception: " + ex.getMessage()); //$NON-NLS-1$
		}
	}
	
	@Test
	public void testDockerDaemonConnection() {
		startServerAdapter();
		DockerExplorerView dockerExplorer = new DockerExplorerView();
		dockerExplorer.open();
		DockerConnection connection =  dockerExplorer.getDockerConnectionByName(DOCKER_DAEMON_CONNECTION);
		if (connection == null) {
			fail("Could not find Docker connection " + DOCKER_DAEMON_CONNECTION); //$NON-NLS-1$
		}
		connection.select();
		connection.enableConnection();
		connection.refresh();
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(30));
		try {
			assertTrue("Docker connection does not contain any images", connection.getImagesNames().size() > 0); //$NON-NLS-1$
		} catch (WaitTimeoutExpiredException ex) {
			ex.printStackTrace();
			fail("WaitTimeoutExpiredException occurs when expanding" //$NON-NLS-1$
					+ " Docker connection " + DOCKER_DAEMON_CONNECTION); //$NON-NLS-1$
		} catch (JFaceLayerException jFaceExc) {
			jFaceExc.printStackTrace();
			fail(jFaceExc.getMessage());
		}
	}
	
}
