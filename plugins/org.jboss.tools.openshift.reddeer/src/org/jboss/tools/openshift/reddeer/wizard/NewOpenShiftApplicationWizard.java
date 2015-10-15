package org.jboss.tools.openshift.reddeer.wizard;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;

/**
 * General wizard for a new OpenShift application.
 * 
 * @author mlabuda@redhat.com
 */
public class NewOpenShiftApplicationWizard {
	
	protected TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	protected String server;
	protected String username;
	
	public NewOpenShiftApplicationWizard(String server, String username) {
		this.server = server;
		this.username = username;
	}
	
	/**
	 * Opens new application wizard via shell menu File - New. There has to be 
	 * an existing connection in OpenShift explorer, otherwise method fails.
	 */
	public void openWizardFromShellMenu() {
		new WorkbenchShell().setFocus();
		
		new ShellMenu("File", "New", "Other...").select();
		
		new DefaultShell("New").setFocus();
		
		new DefaultTreeItem("OpenShift", "OpenShift Application").select();
		
		new NextButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		for (String comboItem: new DefaultCombo(0).getItems()) {
			if (comboItem.contains(username) && comboItem.contains(server)) {
				new DefaultCombo(0).setSelection(comboItem);
				break;
			}
		}
		
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ButtonWithTextIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
	}
	
	/**
	 * Opens a new OpenShift application wizard from JBoss Central.
	 */
	public void openWizardFromCentral() {
		new DefaultToolItem(new WorkbenchShell(), OpenShiftLabel.Others.JBOSS_CENTRAL).click();
		
		new InternalBrowser().execute(OpenShiftLabel.Others.OPENSHIFT_CENTRAL_SCRIPT);
	
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
		new WaitUntil(new ButtonWithTextIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();
	}
	
}
