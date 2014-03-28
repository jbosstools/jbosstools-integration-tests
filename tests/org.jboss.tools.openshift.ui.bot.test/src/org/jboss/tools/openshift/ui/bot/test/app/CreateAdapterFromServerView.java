package org.jboss.tools.openshift.ui.bot.test.app;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateAdapterFromServerView {

	private final String DYI_APP = "diyapp" + new Date().getTime();
	
	@Before
	public void createApplication() {
		OpenShiftBotTest.createOpenShiftApplicationWithoutAdapter(DYI_APP, OpenShiftLabel.AppType.DIY);
	}
	
	@Test
	public void createAdapterFromServerView() {
		ServersView serverView = new ServersView();
		serverView.open();
		
		new ContextMenu("New", "Server").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("New Server"), TimePeriod.NORMAL);
		
		new DefaultShell("New Server").setFocus();
		List<TreeItem> items = new DefaultTree().getAllItems();
		TreeItem openShiftItem = null;
		for (TreeItem item: items) {
			String label = item.getText();
			if (label.equals("OpenShift Server")) {
				openShiftItem = item;
				break;
			}
		}
		openShiftItem.select();
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)), TimePeriod.LONG);
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		serverView.open();
		
		List<TreeItem> servers = new DefaultTree().getItems();
		TreeItem server = null;
		for (TreeItem item: servers) {
			String serverName = item.getText().split(" ")[0];
			if (serverName.equals(DYI_APP)) {
				server = item;
				break;
			}
		}	
		
		assertFalse("Adapter was not created", server == null);
	}
	
	@After
	public void deleteApp() {
		OpenShiftBotTest.deleteOpenShiftApplication(DYI_APP, OpenShiftLabel.AppType.DIY_TREE);
	}
}
