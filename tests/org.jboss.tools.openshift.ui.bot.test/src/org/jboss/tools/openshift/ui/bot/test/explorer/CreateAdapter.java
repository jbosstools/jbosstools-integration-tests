package org.jboss.tools.openshift.ui.bot.test.explorer;

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
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateAdapter {

	private final String DYI_APP = "diyapp" + new Date().getTime();
	
	@Before
	public void createDIYApp() {
		OpenShiftBotTest.createOpenShiftApplicationWithoutAdapter(DYI_APP, OpenShiftLabel.AppType.DIY);
	}
	
	@Test
	public void canCreateAdapterViaServers() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		TreeItem connection = new DefaultTree().getItems().get(0);
		connection.select();
		new ContextMenu(OpenShiftLabel.Labels.REFRESH);

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.select();
		TreeItem domainItem = connection.getItems().get(0);
		domainItem.select();
		if (!domainItem.isExpanded()) {
			domainItem.doubleClick();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
				
		domainItem.getItem(DYI_APP + " " + OpenShiftLabel.AppType.DIY_TREE).select();
		new ContextMenu(OpenShiftLabel.Labels.EXPLORER_ADAPTER).select();
		
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
			if (server.getLabel().getName().equals(DYI_APP + " at OpenShift")) {
				adapterExists = true;
			}
		}
		assertTrue(adapterExists);

		Logger logger = new Logger(this.getClass());
		logger.info("*** OpenShift RedDeer Tests: OpenShift Server Adapter created. ***");
	}

	@After
	public void deleteDIYApp() {
		OpenShiftBotTest.deleteOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY_TREE);
	}
}
