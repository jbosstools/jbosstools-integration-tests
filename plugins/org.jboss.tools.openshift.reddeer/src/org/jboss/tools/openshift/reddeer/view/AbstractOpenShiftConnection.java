package org.jboss.tools.openshift.reddeer.view;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;

public abstract class AbstractOpenShiftConnection extends AbstractOpenShiftExplorerItem {

	public AbstractOpenShiftConnection(TreeItem connectionItem) {
		super(connectionItem);
	}
	
	/**
	 * Removes connection from OpenShift explorer view.
	 */
	public void remove() {
		item.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.REMOVE_CONNECTION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.REMOVE_CONNECTION));
		
		new DefaultShell(OpenShiftLabel.Shell.REMOVE_CONNECTION);
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.REMOVE_CONNECTION));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	
}
