package org.jboss.tools.openshift.ui.view.openshift;

import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.RedDeerException;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;

/**
 * 
 * OpenShift explorer view implemented with RedDeer.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class OpenShiftExplorerView extends WorkbenchView {

	private TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
	
	public OpenShiftExplorerView() {
		super("JBoss Tools", "OpenShift Explorer");
	}
	
	/**
	 * Opens a new connection shell through tool item located in the top right corner of
	 * OpenShift Explorer.
	 */
	public void openConnectionShellViaToolItem() {
		open();
		DefaultToolItem connectionButton = new DefaultToolItem(OpenShiftLabel.Others.CONNECT_TOOL_ITEM);
		connectionButton.click();
	}
	
	/**
	 * Opens a new connection shell through context menu in OpenShift explorer.
	 */
	public void openConnectionShell() {
		open();
		// there is either a link or context menu
		try {
			new ContextMenu(OpenShiftLabel.ContextMenu.NEW_CONNECTION).select();
		} catch (SWTLayerException ex) {
			new DefaultLink(OpenShiftLabel.TextLabels.NEW_CONNECTION).click();
		}
	}
	
	/**
	 * Connects to OpenShift server. OpenShift connection shell has to be opened at the 
	 * moment of method invocation.
	 * @param server URL of a server
	 * @param username
	 * @param password
	 * @param storePassword whether password should be stored or not in security storage
	 */
	public void connectToOpenShift(String server, String username, String password, boolean storePassword, boolean useDefaultServer) {
		new DefaultShell("");
		if (new CheckBox(0).isChecked() != useDefaultServer) {
			new CheckBox(0).click();
		}
		
		if (!useDefaultServer) {
			new DefaultCombo(1).setText(server);
		}
		new DefaultText(0).setText(username);
		new DefaultText(1).setText(password);
		
		if (new CheckBox(1).isChecked() != storePassword) {
			new CheckBox(1).click();
		}
		
		new WaitUntil(new ButtonWithTextIsActive(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(""), TimePeriod.LONG);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}	
	
	/**
	 * Removes connection from OpenShift explorer view.
	 *
	 * @param username user name
	 */
	public void removeConnection(String username) {
		getConnection(username).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.REMOVE_CONNECTION).select();
		
		new DefaultShell(OpenShiftLabel.Shell.REMOVE_CONNECTION);
		new OkButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	/**
	 * Finds out whether connection with specified username exists or not.
	 * 
	 * @param username user name
	 * @return true if connection exists, false otherwise
	 */
	public boolean connectionExists(String username) {
		try {
			getConnection(username);
			return true;
		} catch (RedDeerException ex) {
			return false;
		}
	}
	
	/**
	 * Gets connection with specified user name.
	 * 
	 * @param username user name 
	 * @return tree item representing connection
	 */
	public TreeItem getConnection(String username) {
		open();
		return treeViewerHandler.getTreeItem(new DefaultTree(), username);
	}
	
	/** 
	 * Gets domain with specified user name and domain name.
	 * 
	 * @param username user name
	 * @param domain domain name
	 * @return tree item representing domain
	 */
	public TreeItem getDomain(String username, String domain) {
		TreeItem connectionItem = getConnection(username);
		connectionItem.select();
		expand(connectionItem, TimePeriod.NORMAL);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		return treeViewerHandler.getTreeItem(connectionItem, domain);
	}

	/**
	 * Get application as a TreeItem. This method is useful for a further processing 
	 * of applications (e.g. opening a context menu). Applications are shown in 
	 * OpenShift explorer with their types. This method require only application name
	 * WITHOUT type. <br><br> Example: application "diyapp <i>Do-It-Yourself 0.1 (diy-0.1)"</i>
	 * is chosen by calling this method with an argument <i>"diyapp"</i>.
	 * 
	 * @param username user name on the connection
	 * @param domain domain name
	 * @param name name of application without application type 
	 * @return tree item of an application represented by the given name
	 */
	public TreeItem getApplication(String username, String domain, String name) {
		TreeItem domainItem = getDomain(username, domain);
		domainItem.select();
		expand(domainItem, TimePeriod.LONG);
		
		return treeViewerHandler.getTreeItem(domainItem, name);
	}
	
	/**
	 * Finds an application with specified name in OpenShift explorer and selects it.
	 * 
	 * @param user user name
	 * @param domain domain of the application
	 * @param name name of the application with specified name
	 */
	public void selectApplication(String user, String domain, String name) {
		getApplication(user, domain, name).select();
	}
	
	/**
	 * Finds out whether an application with specified name exists or not.
	 * 
	 * @param user user name
	 * @param domain domain of the application
	 * @param name name of the application with specified name
	 * @return true if application exists, false otherwise
	 */
	public boolean applicationExists(String user, String domain, String name) {
		try {
			getApplication(user, domain, name);
			return true;
		} catch (JFaceLayerException ex) {
			return false;
		}
	}
	
	/**
	 * Reopens OpenShift view explorer. Workaround for issue with missing submenus.
	 */
	public void reopen() {
		close();
		open();
	}
	
	 // Workaround for not expanded tree items, if it is 
	 // failing, uncomment this method and use it
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
