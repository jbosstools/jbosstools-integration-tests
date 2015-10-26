package org.jboss.tools.openshift.ui.bot.test.application.handle;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.workbench.api.View;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShift2Application;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of port forwarding of an application.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID706PortForwardingTest {

	private String applicationName = "eap" + System.currentTimeMillis();
	
	@Before
	public void createEAPApplication() {
		new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, false).
			createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.JBOSS_EAP, applicationName, false, true, true);
	}
	
	@Test
	public void testPortForwarding() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShift2Application application = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
				getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName);
	
		new WaitUntil(new ApplicationIsDeployedSuccessfully(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, 
				DatastoreOS2.DOMAIN, applicationName, "OpenShift"), TimePeriod.LONG);
		
		portForwardingTest(explorer, application.getTreeItem(), OpenShiftLabel.ContextMenu.PORT_FORWARD);
	}
	
	public static void portForwardingTest(View viewOfItem, TreeItem itemToHandle, String... contextMenuPath) {
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.PORTS_FORWARDING), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.PORTS_FORWARDING);
		
		assertTrue("Localhost 127.0.0.1 should be used by default for all services.",
				new CheckBox(0).isChecked());
		
		for (TableItem item: new DefaultTable().getItems()) {
			assertTrue("Default local address has not been used for some service.",
					item.getText(1).equals("127.0.0.1"));
		}
		
		
		new CheckBox(1).toggle(true);
		for (TableItem item: new DefaultTable().getItems()) {
			assertTrue("Free port has not been used for some service.",
					Integer.valueOf(item.getText(2)).intValue() > 9999);
		}
		new CheckBox(1).toggle(false);
		
		new PushButton(OpenShiftLabel.Button.START_ALL).click();
		
		new WaitUntil(new ButtonWithTextIsEnabled(new OkButton()), TimePeriod.LONG);
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.PORTS_FORWARDING), TimePeriod.LONG);
		
		ConsoleView console = new ConsoleView();
		console.open();
		
		assertTrue("Console does not contain information about started port forwarding. "
				+ "Port forwarding could not been executed.",
				console.getConsoleText().contains("Starting port-forwarding..."));
		
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.PORTS_FORWARDING), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.PORTS_FORWARDING);
		new PushButton(OpenShiftLabel.Button.STOP_ALL).click();
		
		new WaitUntil(new ButtonWithTextIsEnabled(new OkButton()), TimePeriod.LONG);
		
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.PORTS_FORWARDING), TimePeriod.LONG);
		
		console.open();
		assertTrue("Console does not contain information about started port forwarding. "
				+ "Port forwarding could not been executed.",
				console.getConsoleText().contains("Stopping port-forwarding..."));
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
				applicationName, applicationName).perform();
	}
}
