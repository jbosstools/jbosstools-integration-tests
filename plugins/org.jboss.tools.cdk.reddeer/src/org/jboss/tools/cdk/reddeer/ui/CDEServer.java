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
package org.jboss.tools.cdk.reddeer.ui;

import org.jboss.reddeer.common.condition.AbstractWaitCondition;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersViewEnums.ServerState;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.cdk.reddeer.condition.ServerHasState;

public class CDEServer extends Server {
	
	private static Logger log = Logger.getLogger(CDEServer.class);
	
	private static final TimePeriod TIMEOUT = TimePeriod.VERY_LONG;
	
	public CDEServer(TreeItem item, ServersView view) {
		super(item, view);
	}
	
	@Override
	protected void operateServerState(String menuItem, final ServerState resultState) {
		log.debug("Operate server's state: '" + menuItem + "'");
		select();
		new ContextMenu(menuItem).select();
		new WaitUntil(new JobIsRunning(), TIMEOUT);
		if (resultState == ServerState.STARTING || resultState == ServerState.STARTED) {
			confirmSSLCertificateDialog();
		}
		new WaitUntil(new ServerHasState(this, resultState), TIMEOUT);
		new WaitWhile(new JobIsRunning(), TIMEOUT);
		log.debug("Operate server's state finished, the result server's state is: '" + getLabel().getState() + "'");
	}
	
	/**
	 * Methods waits for SSL Certificate dialog shell to appear and then confirms dialog, 
	 * it might happen that certificate is already in place and no dialog is shown,
	 * then WaitTimeoutExpiredException is logged but not raised
	 */
	private void confirmSSLCertificateDialog() {
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Untrusted SSL Certificate"), TimePeriod.VERY_LONG);
			new DefaultShell("Untrusted SSL Certificate");
			new PushButton("Yes").click();
			new WaitWhile(new ShellWithTextIsAvailable("Untrusted SSL Certificate"));
		} catch (WaitTimeoutExpiredException ex) {
			log.info("WaitTimeoutExpiredException occured when handling Certificate dialog. "
					+ "Dialog has not been shown");
		}
	}
}
