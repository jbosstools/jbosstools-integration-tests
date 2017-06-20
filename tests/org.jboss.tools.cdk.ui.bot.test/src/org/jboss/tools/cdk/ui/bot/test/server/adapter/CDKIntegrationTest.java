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
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.tools.cdk.reddeer.requirements.DisableSecureStorageRequirement.DisableSecureStorage;
import org.jboss.tools.cdk.ui.bot.test.utils.CDKTestUtils;
import org.jboss.tools.docker.reddeer.ui.DockerExplorerView;
import org.jboss.tools.docker.reddeer.ui.resources.DockerConnection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.view.resources.OpenShift3Connection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Basic Devstudio and CDK integration test
 * Requires Secure Storage disabled
 * @author odockal
 *
 */
@DisableSecureStorage
public class CDKIntegrationTest extends CDKServerAdapterAbstractTest {
	
	private static final String OPENSHIFT_USER_NAME = "openshift-dev"; //$NON-NLS-1$
	
	private static final String OPENSHIFT_PROJECT_NAME = "OpenShift sample project"; //$NON-NLS-1$
	
	private static final String DOCKER_DAEMON_CONNECTION = SERVER_ADAPTER;
	
	private static Logger log = Logger.getLogger(CDK3IntegrationTest.class);
	
	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	@Override
	protected String getServerAdapter() {
		return SERVER_ADAPTER;
	}
	
	@Override
	protected boolean isCDK3() {
		return false;
	}
	
	@BeforeClass
	public static void setup() {
		checkVagrantfileParameters();
		CDKTestUtils.deleteCDEServer(SERVER_ADAPTER);
		addNewCDKServer(CDK_SERVER_NAME, SERVER_ADAPTER, VAGRANTFILE_PATH);
	}
	

	@AfterClass
	public static void tearDownEnvironment() {
		CDKTestUtils.deleteCDEServer(SERVER_ADAPTER);
		CDKTestUtils.removeAccessRedHatCredentials(CREDENTIALS_DOMAIN, USERNAME);
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
