package org.jboss.tools.openshift.reddeer.view;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftToolsException;

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
	 * Override open method, because of https://issues.jboss.org/browse/JBIDE-20014.
	 */
	public void reopen() {
		if (isOpened()) {
			close();
		}
		super.open();
	}
	
	/**
	 * Opens a new connection shell through context menu in OpenShift explorer.
	 */
	public void openConnectionShell() {
		open();
		// there is either a link or context menu
		try {
			new ContextMenu(OpenShiftLabel.ContextMenu.NEW_CONNECTION).select();
		} catch (CoreLayerException ex) {
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
	 * @param useDefaultServer
	 */
	public void connectToOpenShift2(String server, String username, String password, boolean storePassword, boolean useDefaultServer, boolean certificateShown) {
		connectToOpenShift(server, username, password, storePassword, useDefaultServer, ServerType.OPENSHIFT_2, AuthenticationMethod.DEFAULT, certificateShown);
	}
	
	/**
	 * Connects to OpenShift server. OpenShift connection shell has to be opened at the 
	 * moment of method invocation.
	 * @param server URL of a server
	 * @param username
	 * @param password
	 * @param storePassword whether password should be stored or not in security storage
	 * @param useDefaultServer
	 */
	public void connectToOpenShift3Basic(String server, String username, String password, boolean storePassword, boolean useDefaultServer) {
		connectToOpenShift(server, username, password, storePassword, useDefaultServer, ServerType.OPENSHIFT_3, AuthenticationMethod.BASIC, true);
	}
	
	/**
	 * Connects to OpenShift server. OpenShift connection shell has to be opened at the 
	 * moment of method invocation.
	 * @param server URL of a server
	 * @param token
	 * @param storeToken whether password should be stored or not in security storage
	 * @param useDefaultServer
	 */
	public void connectToOpenShift3OAuth(String server, String token, boolean storeToken, boolean useDefaultServer) {
		connectToOpenShift(server, null, token, storeToken, useDefaultServer, ServerType.OPENSHIFT_3, AuthenticationMethod.OAUTH, true);
	}
	
	public void connectToOpenShift(String server, String username, String password, boolean storePassword, boolean useDefaultServer, 
			ServerType serverType, AuthenticationMethod authMethod, boolean certificateShown) {
		new DefaultShell("");
		
		new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(serverType.toString());
				
		if (new CheckBox(0).isChecked() != useDefaultServer) {
			new CheckBox(0).click();
		}
		
		if (!useDefaultServer) {
			new LabeledCombo(OpenShiftLabel.TextLabels.SERVER).setText(server);
		}
		
		if (ServerType.OPENSHIFT_3.equals(serverType)) {
			new LabeledCombo(OpenShiftLabel.TextLabels.SERVER_TYPE).setSelection(serverType.toString());
			new LabeledCombo(OpenShiftLabel.TextLabels.PROTOCOL).setSelection(authMethod.toString());
		}
		
		if (ServerType.OPENSHIFT_2.equals(serverType) || (ServerType.OPENSHIFT_3.equals(serverType)
				&& AuthenticationMethod.BASIC.equals(authMethod))) {
			new LabeledText(OpenShiftLabel.TextLabels.USERNAME).setText(username);
			new LabeledText(OpenShiftLabel.TextLabels.PASSWORD).setText(password);			
		} else {
			if (ServerType.OPENSHIFT_3.equals(serverType) && AuthenticationMethod.OAUTH.equals(authMethod)) {
				new LabeledText(OpenShiftLabel.TextLabels.TOKEN).setText(password);
			}
		}
	
		if (ServerType.OPENSHIFT_2.equals(serverType) || 
				(ServerType.OPENSHIFT_3.equals(serverType) && AuthenticationMethod.BASIC.equals(authMethod))) { 
			if (new CheckBox(OpenShiftLabel.TextLabels.STORE_PASSWORD).isChecked() != storePassword) {
				new CheckBox(OpenShiftLabel.TextLabels.STORE_PASSWORD).click();
			}
		} else {
			if (new CheckBox(OpenShiftLabel.TextLabels.STORE_TOKEN).isChecked() != storePassword) {
				new CheckBox(OpenShiftLabel.TextLabels.STORE_TOKEN).click();
			}
		}
				
		new WaitUntil(new ButtonWithTextIsEnabled(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		if (certificateShown) {
			try {
				new DefaultShell("Untrusted SSL Certificate");
				new PushButton("Yes").click();
			} catch (RedDeerException ex) {
				fail("Aceptance of SSL certificate failed.");
			}
		}
			
		new WaitWhile(new ShellWithTextIsAvailable(""), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}	
	
	/**
	 * Finds out whether connection with specified username exists or not.
	 * 
	 * @param username user name
	 * @return true if connection exists, false otherwise
	 */
	public boolean connectionExists(String username) {
		return connectionExists(username, null);
	}
	
	/**
	 * Finds out whether connection with specified username and server exists or not.
	 * @param username user name
	 * @param server server
	 * @return true if connection exists, false otherwise
	 */
	public boolean connectionExists(String username, String server) {
		try {
			getConnectionItem(username, server);
			return true;
		} catch (RedDeerException ex) {
			return false;
		}
	}
	
	/**
	 * Gets OpenShift 2 connection for a specified user.
	 * 
	 * @param username user name
	 * @return OpenShift 2 connection
	 */
	public OpenShift2Connection getOpenShift2Connection(String username) {
		return new OpenShift2Connection(getConnectionItem(username, null));
	}
	
	/**
	 * Gets OpenShift 2 connection for a specified server and user.
	 * @param username user name
	 * @param server server
	 * @return OpenShift 2 connection
	 */
	public OpenShift2Connection getOpenShift2Connection(String username, String server) {
		return new OpenShift2Connection(getConnectionItem(username, server));
	}
	
	/**
	 * Gets OpenShift 3 connection for a specified user.
	 * 
	 * @param username user name
	 * @return OpenShift 3 connection
	 */
	public OpenShift3Connection getOpenShift3Connection(String username) {
		return new OpenShift3Connection(getConnectionItem(username, null));
	}
	
	/**
	 * Gets OpenShift 3 connection for a specified server and user.
	 * 
	 * @param username user name
	 * @param server server
	 * @return OpenShift 3 connection
	 */
	public OpenShift3Connection getOpenShift3Connection(String username, String server) {
		return new OpenShift3Connection(getConnectionItem(username, server));
	}
	
	private TreeItem getConnectionItem(String username, String server) {
		open();
		TreeItem connectionItem = treeViewerHandler.getTreeItem(new DefaultTree(), username);
		if (server != null) {
			if (treeViewerHandler.getStyledTexts(connectionItem)[0].equals(server)) {
				return connectionItem;
			} else {
				throw new OpenShiftToolsException("There is no connection with specified username " + username +
						" and server " + server);
			}
		} else {
			return connectionItem;
		}
	}

	public enum ServerType {
		
		OPENSHIFT_2("OpenShift 2"), 
		OPENSHIFT_3("OpenShift 3");
		
		private final String text;
		
		private ServerType(String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	
	public enum AuthenticationMethod {
		
		DEFAULT(""), 
		BASIC("Basic"),
		OAUTH("OAuth");
		
		private final String text;
		
		private AuthenticationMethod(String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
}
