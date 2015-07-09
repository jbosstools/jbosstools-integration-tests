package org.jboss.tools.openshift.reddeer.wizard.v2;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;

/**
 * Opens new application wizard as desired. 
 * 
 * @author mlabuda@redhat.com
 *
 */
public class OpenNewApplicationWizard {
	
	/**
	 * Opens new application wizard from OpenShift explorer.
	 */
	public static void openWizardFromExplorer(String username, String domain) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		if (explorer.isOpened()) {
			explorer.close();
		}
		explorer.open();

		explorer.getConnection(username).select();
		// temporarily turn off creation on domains
		//TreeItem domainItem = explorer.getDomain(username, domain);
		//domainItem.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_APPLICATION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
	}
	
	/**
	 * Opens new application wizard via shell menu. There has to be 
	 * existing connection in OpenShift explorer, otherwise method fails.
	 */
	public static void openWizardFromShellMenu(String username) {
		new WorkbenchShell().setFocus();
		
		new ShellMenu("File", "New", "Other...").select();
		
		new DefaultShell("New").setFocus();
		
		new DefaultTreeItem("OpenShift", "OpenShift Application").select();
		
		new NextButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		for (String comboItem: new DefaultCombo(0).getItems()) {
			if (comboItem.contains(username)) {
				new DefaultCombo(0).setSelection(comboItem);
				break;
			}
		}
		
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		// to be sure, it is processed
		new WaitUntil(new ButtonWithTextIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
	}
	
	public static void openWizardFromCentral(String username) {
		new DefaultToolItem(new WorkbenchShell(), OpenShiftLabel.Others.JBOSS_CENTRAL).click();
		
		new DefaultHyperlink(OpenShiftLabel.Others.OPENSHIFT_APP).activate();
	
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		for (String comboItem: new DefaultCombo(0).getItems()) {
			if (comboItem.contains(username)) {
				new DefaultCombo(0).setSelection(comboItem);
				break;
			}
		}
		
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		// to be sure, it is processed
		new WaitUntil(new ButtonWithTextIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
	}
	
}
