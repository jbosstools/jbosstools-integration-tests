package org.jboss.tools.openshift.ui.bot.test.application;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.customizedexplorer.CustomizedProjectExplorer;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ImportApplicationThroughServersView {

	private String APP_NAME = "diyapp" + System.currentTimeMillis(); 
	
	@Before
	public void createApp() {
		new NewApplicationTemplates(false).createApplicationWithoutImportingIntoWorkspace(
				OpenShiftLabel.AppType.DIY, APP_NAME, false, true);
	}
	
	
	@Test
	public void importAppFromServersView() {
		ServersView servers = new ServersView();
		servers.open();

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
		
		// Wait until data are processed
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.BACK)), TimePeriod.LONG);
		
		new DefaultLink("Import this application").click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Import OpenShift Application"),
				TimePeriod.NORMAL);
		
		new DefaultShell("Import OpenShift Application").setFocus();
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)), TimePeriod.LONG);
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.FINISH)), TimePeriod.LONG);
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		new NewApplicationWizard().postCreateSteps(APP_NAME, false, true);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		CustomizedProjectExplorer explorer = new CustomizedProjectExplorer();
		explorer.open();
		
		assertTrue("Project has not been imported", 
				explorer.containsProject(APP_NAME));
	}
	
	@After
	public void deleteApp() {
		new DeleteApplication(APP_NAME).perform();
	}
	
}
