package org.jboss.tools.openshift.ui.bot.test.application.wizard;

import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.openshiftexplorer.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;

/**
 * Open new application wizard from a specified location. 
 * 
 * @author mlabuda@redhat.com
 *
 */
public class OpenNewApplicationWizard {
	
	/**
	 * Open new application wizard from OpenShift explorer.
	 * Choose first domain in order. Domains are in alphanumeric order.
	 */
	public static void openWizardFromExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		// workaround bcs of missing submenus
		explorer.close();
		explorer.open();

		DefaultTree connection = new DefaultTree(0);
		connection.setFocus();
		connection.getItems().get(0).expand();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		connection.getItems().get(0).select();
		new ContextMenu("New", "Application...").select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
	}
	
	public static void openWizardFromShellMenu() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WorkbenchShell().setFocus();
		
		new ShellMenu("File", "New", "OpenShift Application").select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);

		new DefaultCombo().setSelection(0);
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.NEXT)));
		
		new PushButton(OpenShiftLabel.Button.NEXT).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
	}
	
}
