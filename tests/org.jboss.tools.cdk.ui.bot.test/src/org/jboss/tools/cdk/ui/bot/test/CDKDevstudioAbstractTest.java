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

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.WidgetIsFound;
import org.jboss.reddeer.core.matcher.WithMnemonicTextMatcher;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.wst.server.ui.cnf.DefaultServer;
import org.jboss.reddeer.eclipse.wst.server.ui.cnf.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.cnf.ServersView2;
import org.jboss.reddeer.eclipse.wst.server.ui.cnf.ServersViewEnums.ServerState;
import org.jboss.reddeer.eclipse.wst.server.ui.cnf.ServersViewException;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.clabel.DefaultCLabel;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.core.condition.JobIsRunning;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.cdk.reddeer.preferences.OpenShift3SSLCertificatePreferencePage;
import org.jboss.tools.cdk.reddeer.ui.CDEServersView;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class CDKDevstudioAbstractTest {

	protected ServersView2 serversView;
	
	protected Server server;
	
	private static final Logger log = Logger.getLogger(CDKDevstudioAbstractTest.class);
	
	protected static final String USERNAME;
	
	protected static final String PASSWORD;
	
	private static final String CREDENTIALS_DOMAIN = "access.redhat.com"; //$NON-NLS-1$
	
	static {
		USERNAME = getSystemProperty("developers.username"); //$NON-NLS-1$
		PASSWORD = getSystemProperty("developers.password"); //$NON-NLS-1$
	}
	
	protected static String getSystemProperty(String systemProperty) {
		String property = System.getProperty(systemProperty);
		if (!(property == null || property.equals("") || property.startsWith("${"))) { //$NON-NLS-1$ //$NON-NLS-2$
			return property;
		}
		return null;
	}
	
	protected abstract Server getCDEServer();
	
	protected abstract ServersView2 getServersView();
	
	protected abstract void setServersView(ServersView2 view);
	
	protected abstract void setCDEServer(Server server);
	
	protected abstract String getServerAdapter();
	
	private static void checkCredentials() {
		if (USERNAME == null || PASSWORD== null) {
			throw new RedDeerException("Credentials for Red Hat Developers were not set properly"); //$NON-NLS-1$
		}
		log.info("Red Hat Developers username " + USERNAME + " and given password are set"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@BeforeClass
	public static void setUpEnvironemnt() {
		log.info("Checking given program arguments"); //$NON-NLS-1$
		checkCredentials();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG, false);
	}
	
	@Before
	public void setUpServers() {
		log.info("Open Servers view tab"); //$NON-NLS-1$
		setServersView(new CDEServersView());
		getServersView().open();
		log.info("Getting server object from Servers View with name: " + getServerAdapter()); //$NON-NLS-1$
		setCDEServer(getServersView().getServer(getServerAdapter()));
		new WaitUntil(new JobIsRunning(), TimePeriod.DEFAULT, false);
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
	
	private void checkAvailableServers() {
		for (Server serverItem : getServersView().getServers()) {
			String serverName = serverItem.getLabel().getName();
			log.info(serverName);
		}
		assertTrue(getCDEServer().getLabel().getName().contains(getServerAdapter()));
	}
	
	private static void printCertificates() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		OpenShift3SSLCertificatePreferencePage preferencePage = new OpenShift3SSLCertificatePreferencePage();
		dialog.select(preferencePage);
		preferencePage.printCertificates();
		dialog.ok();
	}
	
	private static void deleteCertificates() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		OpenShift3SSLCertificatePreferencePage preferencePage = new OpenShift3SSLCertificatePreferencePage();
		dialog.select(preferencePage);
		preferencePage.deleteAll();
		preferencePage.apply();
		dialog.ok();
	}
	
	// removes access redhat com credentials used for first cdk run
	protected static void removeAccessRedHatCredentials() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		dialog.select("JBoss Tools", "Credentials"); //$NON-NLS-1$ //$NON-NLS-2$
        try {
	        new WaitUntil(new WidgetIsFound(org.eclipse.swt.custom.CLabel.class, 
	        		new WithMnemonicTextMatcher("Credentials"))); //$NON-NLS-1$
	        new DefaultCLabel("Credentials"); //$NON-NLS-1$
	        DefaultTree tree = new DefaultTree(1);
	        TreeItem item = TreeViewerHandler.getInstance().getTreeItem(tree, new String[]{CREDENTIALS_DOMAIN, USERNAME});
	        item.select();
	        new PushButton(new WithTextMatcher("Remove User")).click(); //$NON-NLS-1$
	        new WaitUntil(new JobIsRunning(), TimePeriod.DEFAULT, false);
        } catch (WaitTimeoutExpiredException exc) {
        	log.error("JBoss Tools - Credentials preferences page has timed out"); //$NON-NLS-1$
        	exc.printStackTrace();
        } catch (JFaceLayerException exc) {
        	log.error("JBoss Tools - Credentials does not contain required username to be deleted"); //$NON-NLS-1$
        	exc.printStackTrace();
        } finally {
	        dialog.ok();
		}
	}
	
}
