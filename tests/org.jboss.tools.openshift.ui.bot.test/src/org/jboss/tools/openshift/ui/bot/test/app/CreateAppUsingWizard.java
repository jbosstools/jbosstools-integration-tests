package org.jboss.tools.openshift.ui.bot.test.app;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateAppUsingWizard {

	public static final String APP_NAME = "diy" + System.currentTimeMillis();
	public static final String APP_TYPE = OpenShiftLabel.AppType.DIY;
	
	private static Logger logger = new Logger(CreateAppUsingWizard.class);
	
	private boolean appCreated = false;
	
	@Before
	public void waiting() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new DefaultShell().setFocus();
	}
	
	@Test
	public void createApplicationUsingOpenShiftWizard() {
		AbstractWait.sleep(5000);
		new ShellMenu("File", "New", "OpenShift Application").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("New OpenShift Application"), TimePeriod.LONG);

		new DefaultCombo().setSelection(0);
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)));
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new DefaultShell("New OpenShift Application").setFocus();
		
		AbstractWait.sleep(10000);
		
		new LabeledText("Name:").setText(APP_NAME);
		logger.info("*** Application name set ***");
		new DefaultCombo(1).setSelection(APP_TYPE);
		logger.info("*** Application type selected ***");
		new PushButton(OpenShiftLabel.Button.NEXT).click();
	
		if (!new CheckBox(1).isChecked()) {
			new CheckBox(1).click();
		}	
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		// BCS of DIY app
		new WaitUntil(new ShellWithTextIsAvailable("Embedded Cartridges"), TimePeriod.VERY_LONG);
		new DefaultShell("Embedded Cartridges").setFocus();
		new PushButton(OpenShiftLabel.Button.OK).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Question"), TimePeriod.VERY_LONG);
		DefaultShell question = new DefaultShell("Question");
		question.setFocus();
		new PushButton(OpenShiftLabel.Button.YES).click();
		
		appCreated = true;
		
		new WaitUntil(new ShellWithTextIsAvailable("Publish " + APP_NAME + "?"), TimePeriod.VERY_LONG);
		new DefaultShell("Publish " + APP_NAME + "?").setFocus();
		new PushButton(OpenShiftLabel.Button.YES).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

		// check successful build after auto git push
		ConsoleView consoleView = new ConsoleView();
		assertTrue(!consoleView.getConsoleText().isEmpty());
		
		ServersView serverView = new ServersView();
		serverView.open();
		
		// WORKAROUND bcs SERVER IN REDDEER DOES NOT WORK
		List<TreeItem> servers = new DefaultTree().getItems();
		TreeItem server = null;
		for (TreeItem item: servers) {
			String serverName = item.getText().split("\\[")[0];
			if (serverName.equals(APP_NAME + " at OpenShift  ")) {
				server = item;
				break;
			}
		}
		
		server.select();
		new ContextMenu("Publish").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Publish " + APP_NAME + "?"), TimePeriod.VERY_LONG);	
		new DefaultShell("Publish " + APP_NAME + "?").setFocus();
		new PushButton(OpenShiftLabel.Button.YES).click();
	
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		serverView.open();
		assertTrue("Adapter does not exists", 
				serverView.getServer(APP_NAME + " at OpenShift") != null);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@After
	public void deleteApplication() {
		if (appCreated) {
			OpenShiftBotTest.deleteOpenShiftApplication(APP_NAME, APP_TYPE);
		}
	}
}

