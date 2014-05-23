package org.jboss.tools.openshift.ui.bot.test.adapter;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;

import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Create adapter from OpenShift explorer view. At first application is created 
 * without server adapter. After that OpenShift server adapter is created from
 * OpenShift explorer view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateAdapterFromExplorer {

	private final String DIY_APP = "diyapp" + new Date().getTime();
	
	@Before
	public void createDIYApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.DIY, DIY_APP, false, true, false);
	}
	
	@Test
	public void canCreateAdapterFromExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();

		TreeItem connection = explorer.getConnection();
		connection.select();
		
		new ContextMenu(OpenShiftLabel.Labels.REFRESH);

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		explorer.getApplication(DIY_APP).select();
		
		new ContextMenu("New", "Server Adapter...").select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ADAPTER), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.ADAPTER).setFocus();
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		new DefaultCombo(1).setSelection(0);
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		ServersView serverView = new ServersView();
		serverView.open();
		List<Server> servers = serverView.getServers();
		boolean adapterExists = false;
		for (Server server: servers) {
			if (server.getLabel().getName().equals(DIY_APP + " at OpenShift")) {
				adapterExists = true;
			}
		}
		assertTrue(adapterExists);

		Logger logger = new Logger(this.getClass());
		logger.info("*** OpenShift RedDeer Tests: OpenShift Server Adapter created. ***");
	}

	@After
	public void deleteDIYApp() {
		new DeleteApplication(DIY_APP).perform();
	}
}
