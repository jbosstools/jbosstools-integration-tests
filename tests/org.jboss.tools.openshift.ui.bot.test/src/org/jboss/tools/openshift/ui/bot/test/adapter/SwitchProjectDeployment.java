package org.jboss.tools.openshift.ui.bot.test.adapter;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SwitchProjectDeployment {

	private String APP_NAME = "diyapp" + System.currentTimeMillis();
	
	@Before
	public void createApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.DIY, APP_NAME, false, true, true);
	}
	
	@Test
	public void switchProjectDeploymentAndVerify() {
		ServersView serversView = new ServersView();
		serversView.open();
		
		List<TreeItem> servers = new DefaultTree().getItems();
		TreeItem server = null;
		for (TreeItem item: servers) {
			String serverName = item.getText().split(" ")[0];
			if (serverName.equals(APP_NAME)) {
				server = item;
				break;
			}
		}	
		
		server.select();
		
		String label = null;
		new ContextMenu("Properties").select();
		
		// Shell has in name [Started] sometimes, hard to say when
		try {
			label = "Properties for " + APP_NAME + " at OpenShift  [Started]";
			new WaitUntil(new ShellWithTextIsAvailable(label), TimePeriod.NORMAL);
			new DefaultShell(label).setFocus();
		} catch(Exception ex) {
			new DefaultShell("Properties for " + APP_NAME + " at OpenShift")
				.setFocus();
		} 

		new PushButton("Switch Location").click();
		AbstractWait.sleep(TimePeriod.getCustom(2));
		assertTrue("Location was not switched to Servers, location was:" +
			new DefaultLabel(9).getText(), 
			new DefaultLabel(9).getText().equals("/Servers/" +
				APP_NAME + " at OpenShift.server"));
		
		new PushButton("Switch Location").click();
		AbstractWait.sleep(TimePeriod.getCustom(2));
		assertTrue("Location was not switched to default, location was:" +
			new DefaultLabel(9).getText(), 
			new DefaultLabel(9).getText().equals("[workspace metadata]"));
		
		new PushButton(OpenShiftLabel.Button.OK).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
	}
	
	@After
	public void deleteApp() {
		new DeleteApplication(APP_NAME).perform();
	}
}
