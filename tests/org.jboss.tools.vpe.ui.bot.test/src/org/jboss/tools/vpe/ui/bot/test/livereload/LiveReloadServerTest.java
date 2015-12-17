/*******************************************************************************
 * Copyright (c) 2013 - 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.bot.test.livereload;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.jboss.ide.eclipse.as.reddeer.server.wizard.NewServerWizardDialog;
import org.jboss.ide.eclipse.as.reddeer.server.wizard.page.NewServerWizardPageWithErrorCheck;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.requirements.openperspective.OpenPerspectiveRequirement.OpenPerspective;
import org.jboss.tools.common.reddeer.perspectives.JBossPerspective;
import org.junit.After;
import org.junit.Test;

/**
 * Tests creating and running LiveReload Server
 * 
 * @author Vladimir Pakan
 *
 */
@OpenPerspective(JBossPerspective.class)
public class LiveReloadServerTest {
	private static final String SERVER_NAME = "LiveReloadTestServer";
	private static final String SERVER_HOST = "localhost";
	private static final int SERVER_PORT = 35729;
	private ServersView servers;

	/**
	 * Creates and runs LiveReload Server
	 */
	@Test
	public void testLiveReloadServer() {
		servers = new ServersView();
		servers.open();
		NewServerWizardDialog serverWizard = new NewServerWizardDialog();
		try {
			serverWizard.open();
			NewServerWizardPageWithErrorCheck sp = new NewServerWizardPageWithErrorCheck();
			sp.selectType("Basic", "LiveReload Server");
			sp.setName(LiveReloadServerTest.SERVER_NAME);
			sp.checkErrors();
			serverWizard.finish();
		} catch (RuntimeException e) {
			serverWizard.cancel();
			throw e;
		} catch (AssertionError e) {
			serverWizard.cancel();
			throw e;
		}
		// LiveReload server is defined
		assertTrue(
				"LiveReload Server with label " + LiveReloadServerTest.SERVER_NAME + " is not present in Server View",
				isLiveReloadServerDefined());
		// no LiveReload server is listening
		assertFalse("LiverReload server is listening on " + LiveReloadServerTest.SERVER_HOST + ":"
				+ LiveReloadServerTest.SERVER_PORT, isLiveReloadServerListening());
		Server liveReloadServer = servers.getServer(LiveReloadServerTest.SERVER_NAME);
		liveReloadServer.start();
		ServerState serverState = liveReloadServer.getLabel().getState();
		// LiveReload server status is Started
		assertTrue("LiveReload Server with label " + LiveReloadServerTest.SERVER_NAME + " does not have status Started",
				serverState.equals(ServerState.STARTED));
		// LiveReload server is listening
		assertTrue("LiverReload server is not listening on " + LiveReloadServerTest.SERVER_HOST + ":"
				+ LiveReloadServerTest.SERVER_PORT, isLiveReloadServerListening());
		liveReloadServer.stop();
		serverState = liveReloadServer.getLabel().getState();
		// LiveReload server status is Stopped
		assertTrue("LiveReload Server with label " + LiveReloadServerTest.SERVER_NAME + " does not have status Stopped",
				serverState.equals(ServerState.STOPPED));
		// no LiveReload server is listening
		assertFalse("LiverReload server is listening on " + LiveReloadServerTest.SERVER_HOST + ":"
				+ LiveReloadServerTest.SERVER_PORT, isLiveReloadServerListening());
		liveReloadServer.delete();
		// LiveReload server is not defined
		assertFalse("LiveReload Server with label " + LiveReloadServerTest.SERVER_NAME + " is present in Server View",
				isLiveReloadServerDefined());
	}

	/**
	 * Final cleanup
	 */
	@After
	public void tearDown() {
		// Stops LiveReload server if is running
		if ((servers != null) && isLiveReloadServerDefined()) {
			Server liveReloadServer = servers.getServer(LiveReloadServerTest.SERVER_NAME);
			if (liveReloadServer.getLabel().getState().equals(ServerState.STARTED)) {
				liveReloadServer.stop();
			}
			liveReloadServer.delete();
		}
	}

	/**
	 * Returns true if LiveReload server is listening on locahost and default
	 * port
	 * 
	 * @return
	 */
	private boolean isLiveReloadServerListening() {
		boolean result = false;
		try {
			Socket socket = new Socket(LiveReloadServerTest.SERVER_HOST, LiveReloadServerTest.SERVER_PORT);
			result = true;
			socket.close();
		} catch (UnknownHostException e) {
			result = false;
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	/**
	 * Returns true when test LiveReload server is present in serverView
	 */
	private boolean isLiveReloadServerDefined() {
		boolean result = false;
		try {
			servers.getServer(LiveReloadServerTest.SERVER_NAME);
			result = true;
		} catch (EclipseLayerException ele) {
			result = false;
		}

		return result;
	}
}