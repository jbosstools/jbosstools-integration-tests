package org.jboss.tools.openshift.ui.bot.test.openshiftexplorer;

import java.util.List;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftToolsException;

/**
 * 
 * OpenShift explorer view implemented with RedDeer.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class OpenShiftExplorerView extends WorkbenchView {

	public OpenShiftExplorerView() {
		super("JBoss Tools", "OpenShift Explorer");
	}
	
	/**
	 * Open a new connection shell through tool item located in the top right corner of
	 * OpenShift Explorer.
	 */
	public void openConnectionShell() {
		open();
		DefaultToolItem connectionButton = new DefaultToolItem("Connect to OpenShift");
		connectionButton.click();
	}
	
	/**
	 * Connect to OpenShift server 
	 * @param server URL of a server
	 * @param username
	 * @param password
	 * @param storePassword whether password should be stored or not in security storage
	 */
	public void connectToOpenShift(String server, String username, String password, boolean storePassword) {
		new DefaultShell("").setFocus();
		if (new CheckBox(0).isChecked()) {
			new CheckBox(0).click();
		}
		
		new DefaultCombo(1).setText(server);
		new DefaultText(0).setText(username);
		new DefaultText(1).setText(password);
		
		boolean checkedSavePassword = new CheckBox(1).isChecked();
		if ((checkedSavePassword && !storePassword) || 
		    (!checkedSavePassword) && storePassword) {
			new CheckBox(1).click();
		}
		
		new PushButton("Finish").click();
	}	
	
	public TreeItem getConnection() {
		open();
		return new DefaultTree().getItems().get(0);
	}

	/**
	 * Get application as a TreeItem. This method is useful for a further processing 
	 * of applications (e.g. opening a context menu). Applications are shown in 
	 * OpenShift explorer with their types. This method require only application name
	 * WITHOUT type. <br><br> E.g. application <i>"diyapp Do-It-Yourself 0.1 (diy-0.1)"</i>
	 * is chosen by calling this method with an argument <i>"diyapp"</i>.
	 * 
	 * @param name name of application without application type 
	 * @return tree item of an application represented by the given name
	 */
	public TreeItem getApplication(String name) {
		TreeItem connection = getConnection();
		connection.select();
		expand(connection, TimePeriod.NORMAL);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		TreeItem domain = connection.getItems().get(0);
		domain.select();
		expand(domain, TimePeriod.LONG);
		
		List<TreeItem> applications = domain.getItems();
		for (TreeItem application: applications) {
			if (name.equals(application.getText().split(" ")[0])) {
				return application;
			}
		}
		
		throw new OpenShiftToolsException("OpenShift application " + 
				name + " does not exist!"); 
	}
	
	/**
	 * Find an application in OpenShift explorer and select it
	 * @param appName name of an application
	 */
	public void selectApplication(String appName) {
		getApplication(appName).select();
	}
	
	/**
	 * Find out whether an application with the given name exists
	 * @param appName name of an application
	 * @return true if application exists, false otherwise
	 */
	public boolean applicationExists(String appName) {
		try {
			getApplication(appName);
			return true;
		} catch (OpenShiftToolsException ex) {
			return false;
		}
	}
	
	/**
	 * Verify that changes has been deployed successfully on OpenShift.
	 * 
	 * @param appName application which should be verified
	 * @param text required text in web browser
	 * @return true in case of success, false otherwise
	 */
	public boolean verifyApplicationInBrowser(String appName, String text) {
		getApplication(appName).select();
				
		AbstractWait.sleep(TimePeriod.getCustom(15));
		
		new ContextMenu("Show in Web Browser").select();
		
		// To be sure that page is loaded
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		AbstractWait.sleep(TimePeriod.getCustom(20));
				
		String browserText = new InternalBrowser().getText();
		return browserText.contains(text);
	}
	
	private void expand(TreeItem item, TimePeriod period) {
		item.select();
		if (!item.isExpanded()) {
			// workaround for tree items 
			item.expand();
			new WaitWhile(new JobIsRunning(), period);
			item.collapse();
			item.expand();
			new WaitWhile(new JobIsRunning(), period);
		}
	}
}
