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
import org.jboss.tools.cdk.reddeer.ui.wizard.NewCDKServerContainerWizardPage;
import org.jboss.tools.docker.reddeer.ui.DockerExplorerView;
import org.jboss.tools.docker.reddeer.ui.resources.DockerConnection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Basic Devstudio and CDK integration test
 * Requires Secure Storage disabled
 * @author odockal
 *
 */
@RunWith(RedDeerSuite.class)
@DisableSecureStorage
public class CDKDevstudioIntegrationTest extends CDKDevstudioAbstractTest {
	
	private static final String VAGRANTFILE_PATH;
	
	private static final String SERVER_NAME = "Red Hat Container Development Kit"; //$NON-NLS-1$
	
	private static final String SERVER_ADAPTER = "Container Development Environment"; //$NON-NLS-1$
	
	private static final String SERVER_TYPE = "Red Hat JBoss Middleware"; //$NON-NLS-1$
	
	private static final String SERVER_HOST = "localhost"; //$NON-NLS-1$
	
	private static final String OPENSHIFT_USER_NAME = "openshift-dev"; //$NON-NLS-1$
	
	private static final String OPENSHIFT_PROJECT_NAME = "OpenShift sample project"; //$NON-NLS-1$
	
	private static final String DOCKER_DAEMON_CONNECTION = SERVER_ADAPTER;
	
	private static Logger log = Logger.getLogger(CDK3DevstudioIntegrationTest.class);
	
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();

	static {
		VAGRANTFILE_PATH = getSystemProperty("vagrantfile.path"); //$NON-NLS-1$
	}
	
	private static void checkVagrantfilePath() {
		if (VAGRANTFILE_PATH == null) {
			throw new RedDeerException("Path Vagrantfile path was not specified"); //$NON-NLS-1$
		}
		log.info("Vagrantfile path is set to " + VAGRANTFILE_PATH); //$NON-NLS-1$
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
		checkVagrantfilePath();
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
		dialog.next();
		
		// set second new server dialog page
		NewCDKServerContainerWizardPage containerPage = new NewCDKServerContainerWizardPage();
		log.info("Setting credentials"); //$NON-NLS-1$
		containerPage.setCredentials(CDKDevstudioAbstractTest.USERNAME, PASSWORD);
		// set cdk 2.x fields
		log.info("Setting vagrant file folder"); //$NON-NLS-1$
		containerPage.setFolder(VAGRANTFILE_PATH);
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
